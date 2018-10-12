package com.delicloud.app.miniprint.server.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.delicloud.app.miniprint.core.entity.TFileModel;
import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.app.miniprint.core.util.FileUtil;
import com.delicloud.app.miniprint.core.vo.FileRespVo;
import com.delicloud.app.miniprint.core.vo.FileVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.dto.FileTaskDto;
import com.delicloud.app.miniprint.server.repository.FileModelRepository;
import com.delicloud.app.miniprint.server.repository.UserRepository;
import com.delicloud.app.miniprint.server.service.IFileService;
import com.delicloud.platform.common.lang.exception.PlatformException;
import com.delicloud.platform.common.lang.util.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: dy
 * @Description: 微博业务层
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Value("${aliyun.oss.bucket.name}")
    private String ossBucketName;

    @Value("${aliyun.oss.endpoint}")
    private String ossEntryPoint;

    @Value("${aliyun.oss.accessKey}")
    private String ossAccessKey;

    @Value("${aliyun.oss.accessSecret}")
    private String ossAccessSecret;

    @Value("${aliyun.oss.domain:}")
    private String ossAccessDomain;

    private OSSClient client;

    @Autowired
    private FileModelRepository fileModelRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void start() {
        client = new OSSClient(ossEntryPoint, ossAccessKey, ossAccessSecret);
    }

    @PreDestroy
    public void stop() {
        client.shutdown();
    }

    @Override
    public void upload (MultipartFile file, Long uid, String type) {

        if (null == file)
            throw new AppException(-1, "文件不能为空");

        if (null == uid)
            throw new AppException(-1, "用户 id 不能为空");

        if (!FileUtil.isPicture(file.getOriginalFilename()))
            throw new AppException (-1, "请选择正确的图片格式");

        FileVo fileVo = uploadToOss(file, uid, type);
        if (fileVo != null) {
            TUser user = userRepository.findOne(uid);
            if (null == user)
                throw new AppException(-1, "用户信息不存在");
            if ("image".equals(type)) { // 头像
                user.setAvatarUrl(fileVo.getUrl());
            } else {    // 背景图
                user.setBackgroundUrl(fileVo.getUrl());
            }
            user.setUpdateTime(System.currentTimeMillis());
            userRepository.save(user);
        }
    }

//    @Override
//    public List<FileVo> uploadFiles(MultipartFile[] files, String type, Long uid) {
//
//        if (null == files || files.length == 0)
//            throw new AppException(-1, "请选择需要上传的文件");
//
//        List<FileVo> results = new ArrayList<>();
//        for (MultipartFile file : files) {
//            FileVo fileVo = uploadToOss(file, uid, type);
//            results.add(fileVo);
//        }
//
//        return results;
//    }

    @Override
    public TFileModel test (MultipartFile file, Long uid, String type) {

        BufferedInputStream bs = null;

        long size = file.getSize();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("出错");
        }

        // eg: fileName = qwe123.docx
        String fileName = file.getOriginalFilename();   // 文件名称 qwe123.docx

        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1); // 文件后缀 docx

        bs = (BufferedInputStream) inputStream;

        String inputFilePath = "/Users/dy/abc.pdf";  // 本地存放文件路径
        File inputFile = new File(inputFilePath);
        File inputFileToOperate = new File(inputFilePath);
        try {
            IOUtils.copy(bs, new FileOutputStream(inputFile));
        } catch (Exception e) {

        }

        String md5String = "";
        try {
            md5String = DigestUtils.md5Hex(new FileInputStream(inputFile));
        } catch (Exception e) {

        }


        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(inputFileToOperate);
            System.out.println(fileInputStream.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("upload file={}", fileName);

        TFileModel entity = new TFileModel();

        String uniqueName = null;
        try {

            long currentTimeMillis = System.currentTimeMillis();
            String contentType = FileUtil.getContentType(fileName);

            log.debug("Upload file md5: {}", md5String);

            entity.setCreateBy(uid);// 当前操作人
            entity.setCreateTime(currentTimeMillis);// 创建时间
            entity.setUpdateTime(currentTimeMillis);

            entity.setName(fileName);
            entity.setSize(size);
            entity.setFormat(fileExt);
            entity.setChecksum(md5String);

            // 生成文件的KEY
            String fileKey;
            fileKey = String.format("oss-%s-%s.%s", System.currentTimeMillis(), size, fileExt);

            // 生成文件的存放地址
            if (MyStringUtils.isEmpty(ossAccessDomain)) {
                ossAccessDomain = String.format("%s.%s", ossBucketName, ossEntryPoint);
            }

            String url = String.format("%s://%s/%s", "https", ossAccessDomain, fileKey);
            entity.setAbsolutePath(url);    // 绝对路径
            entity.setRelativePath(fileKey);    // 相对路径

            List<TFileModel> existFiles = fileModelRepository.findByChecksum(md5String);
            if (existFiles != null && existFiles.size() > 0) {
                TFileModel existFile = existFiles.get(0);
                return existFile;
            } else {
                // 文件不存在，发起上传
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(size);
                metadata.setCacheControl("no-cache");
                metadata.setHeader("Pragma", "no-cache");
                metadata.setContentEncoding("utf-8");
                metadata.setContentType(contentType);
                metadata.setContentDisposition("filename/filesize=" + fileName + "/" + size + "Byte.");

                String randomKey = UUID.randomUUID().toString();
                log.debug("File not exists, put file, name: {}, md5: {}, random key: {}", uniqueName, md5String,
                        randomKey);
                client.putObject(ossBucketName, randomKey, inputFileToOperate, metadata);
                log.debug(
                        "After put file, do copy, name: {}, md5: {}, srcOssBucketName: {}, srcKey: {}, destOssBucketName: {}, destKey: {}",
                        uniqueName, md5String, ossBucketName, randomKey, ossBucketName, fileKey);
                client.copyObject(ossBucketName, randomKey, ossBucketName, fileKey);
                log.debug(
                        "Finish copy file, name: {}, md5: {}, srcOssBucketName: {}, srcKey: {}, destOssBucketName: {}, destKey: {}",
                        uniqueName, md5String, ossBucketName, randomKey, ossBucketName, fileKey);
                entity.setStorageId(randomKey);
            }
            fileModelRepository.save(entity);
            entity.setStorageId(fileKey);

            return entity;
        } catch (Exception ex) {
            log.error("upload failed.", ex);

            if (!MyStringUtils.isEmpty(uniqueName)) {
                log.error("upload file failed, but still return file info.");
                entity.setId(-1L);
                return entity;
            }

            throw new PlatformException(ex);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*    @Override
    public List<FileRespVo> uploadFiles(MultipartFile[] files, Long uid) {

        int length = files.length;

        if (null == files || files.length == 0)
            throw new AppException(-1, "请选择要上传的文件");

        List<FileRespVo> data = new ArrayList<>();
        int i = 0;
        while (i < length) {
            FileRespVo respVo = new FileRespVo();
            MultipartFile file = files[i];
            String filename = file.getOriginalFilename();
            int index = filename.lastIndexOf(".");
            log.info("文件名称: {}", filename);
            respVo.setFileName(filename);
            String fileExtension = filename.substring(index + 1);
            if ("zip".equalsIgnoreCase(fileExtension)) {  // 工程文件
                MultipartFile imageFile = files[i + 1];
                String first = filename.subSequence(0, 1).toString();
                String type = "";
                if ("p".equals(first)) {
                    type = "p_project";
                } else if ("d".equals(first)) {
                    type = "d_project";
                } else {
                    type = "unknow_project";
                }
                FileVo projectVo = uploadToOss(file, uid, type);
                FileVo imageVo = uploadToOss(imageFile, uid, type);
                respVo.setType(type);
                respVo.setProjectUrl(projectVo.getUrl());
                respVo.setProjectSize(projectVo.getSize());
                respVo.setImageUrl(imageVo.getUrl());
                respVo.setImageSize(imageVo.getSize());
                i += 2;
            } else {    // 普通图片
                FileVo imageVo = uploadToOss(file, uid, "image");
                respVo.setType("image");
                respVo.setImageUrl(imageVo.getUrl());
                respVo.setImageSize(imageVo.getSize());
                i ++ ;
            }
            data.add(respVo);
        }
        return data;
    }*/
    public List<FileRespVo> uploadFiles(MultipartFile[] files, Long uid) {
    	 int length = files.length;

         if (null == files || files.length == 0)
             throw new AppException(-1, "请选择要上传的文件");
         int size = length/3+1;
         ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(size);
         //存储线程的返回值
         List<Future<FileTaskDto>> results = new LinkedList<Future<FileTaskDto>>();
         List<FileRespVo> data = new ArrayList<>();
         int i = 0;
         while (i < length) {
             MultipartFile file = files[i];
             String filename = file.getOriginalFilename();
             int index = filename.lastIndexOf(".");
             log.info("文件名称: {}", filename);
             String fileExtension = filename.substring(index + 1);
             if ("zip".equalsIgnoreCase(fileExtension)) {  // 工程文件
                 MultipartFile imageFile = files[i + 1];
                 String first = filename.subSequence(0, 1).toString();
                 String type = "";
                 if ("p".equals(first)) {
                     type = "p_project";
                 } else if ("d".equals(first)) {
                     type = "d_project";
                 } else {
                     type = "unknow_project";
                 }
                 FileTaskDto fileTaskDto = new FileTaskDto();
                 fileTaskDto.setMultipartFile(file);
                 fileTaskDto.setUId(uid);
                 fileTaskDto.setType(type);
                 FileRespVo respVo = new FileRespVo();
                 respVo.setType(type);
                 respVo.setFileName(filename);
                 fileTaskDto.setFileRespVo(respVo);
                 FileTask fileTask = new FileTask(fileTaskDto);
                 Future<FileTaskDto> result = executor.submit(fileTask);
                 results.add(result);
                 
                 FileTaskDto fileTaskDtoImage = new FileTaskDto();
                 fileTaskDtoImage.setMultipartFile(imageFile);
                 fileTaskDtoImage.setUId(uid);
                 fileTaskDtoImage.setType(type);
                 FileRespVo respVoImage = new FileRespVo();
                 respVoImage.setType(type);
                 respVoImage.setFileName(filename);
                 fileTaskDtoImage.setFileRespVo(respVoImage);
                 FileTask fileTaskImage = new FileTask(fileTaskDtoImage);
                 Future<FileTaskDto> resultImage = executor.submit(fileTaskImage);
                 results.add(resultImage);
                 i += 2;
             } else {    // 普通图片
            	 FileTaskDto fileTaskDto = new FileTaskDto();
            	 fileTaskDto.setMultipartFile(file);
            	 fileTaskDto.setUId(uid);
            	 fileTaskDto.setType("image");
                 FileRespVo respVo = new FileRespVo();
                 respVo.setType("image");
                 respVo.setFileName(filename);
                 fileTaskDto.setFileRespVo(respVo);
                 FileTask fileTask = new FileTask(fileTaskDto);
                 Future<FileTaskDto> result = executor.submit(fileTask);
                 results.add(result);
                 i ++ ;
             }
         }
         executor.shutdown();
         int j = 0;
         while (j < length) {
        	 try {
        		 Future<FileTaskDto> future = results.get(j);
        		 FileTaskDto fileTaskDto =  future.get();
        		 log.info("取出"+fileTaskDto.getFileRespVo().getImageUrl());
        		 MultipartFile multipartFile = fileTaskDto.getMultipartFile();
        		 String fileTaksName = multipartFile.getOriginalFilename();
        		 int indexTask = fileTaksName.lastIndexOf(".");
        		 log.info("文件名称: {}", fileTaksName);
        		 String fileTaskExtension = fileTaksName.substring(indexTask + 1);
        		 if ("zip".equalsIgnoreCase(fileTaskExtension)) {
        			 // 工程文件
        			 FileRespVo fileRespVo =  fileTaskDto.getFileRespVo();
        			 Future<FileTaskDto> futureImage  =  results.get(j+1);
        			 FileTaskDto fileTaskImageDto =  futureImage.get();
        			 log.info("取出"+fileTaskImageDto.getFileRespVo().getImageUrl());
        			 fileRespVo.setProjectSize(fileRespVo.getImageSize());
        			 fileRespVo.setProjectUrl(fileRespVo.getImageUrl());
        			 fileRespVo.setImageSize(fileTaskImageDto.getFileRespVo().getImageSize());
        			 fileRespVo.setImageUrl(fileTaskImageDto.getFileRespVo().getImageUrl());
        			 data.add(fileRespVo);
        			 j += 2;
        		 }else {
        			 FileRespVo fileRespVo =  fileTaskDto.getFileRespVo(); 
        			 data.add(fileRespVo);
        			 j ++ ;
        		 }
				
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
         }
         return data;
    }

    /**
     * 文件上传至 OSS 服务器
     * @param file
     * @return
     * @throws IOException
     */
    private FileVo uploadToOss(MultipartFile file, Long uid, String type) {
        String uniqueName = null;
        File tmpFile = null;
        TFileModel entity = new TFileModel();
        FileVo fileVo = new FileVo();
        fileVo.setType(type);
        try {
            String fileName = file.getOriginalFilename();
            long length = file.getSize();
            fileVo.setSize(length);
            InputStream inputStream = file.getInputStream();
            log.info("upload file={}", fileName);

            tmpFile = File.createTempFile("delicloud-fs", ".tmp");
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);

            String md5String = DigestUtils.md5Hex(new FileInputStream(tmpFile));

            List<TFileModel> existFiles = fileModelRepository.findByChecksum(md5String);
            if (existFiles != null && existFiles.size() > 0) {
                TFileModel existFile = existFiles.get(0);
                fileVo.setUrl(existFile.getAbsolutePath() == null ? "" : existFile.getAbsolutePath());
                return fileVo;
            }
            String contentType = FileUtil.getContentType(fileName);
            String fileExt = FilenameUtils.getExtension(fileName);

            log.debug("Upload file md5: {}", md5String);

            entity.setCreateBy(uid);// 当前操作人
            entity.setCreateTime(System.currentTimeMillis());// 创建时间
            entity.setName(fileName);
            entity.setSize(length);
            entity.setFormat(fileExt);
            entity.setChecksum(md5String);

            // 生成文件的KEY
            String fileKey = String.format("oss-%s-%s.%s", System.currentTimeMillis(), length, fileExt);

            // 生成文件的存放地址
            if (MyStringUtils.isEmpty(ossAccessDomain))
                ossAccessDomain = String.format("%s.%s", ossBucketName, ossEntryPoint);

            String url = String.format("%s://%s/%s", "https", ossAccessDomain, fileKey);
            entity.setAbsolutePath(url);    // 绝对路径
            entity.setRelativePath(fileKey);    // 相对路径

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(length);
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(contentType);
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + length + "Byte.");

            String randomKey = UUID.randomUUID().toString();
            log.debug("File not exists, put file, name: {}, md5: {}, random key: {}", uniqueName, md5String,
                    randomKey);
            client.putObject(ossBucketName, randomKey, tmpFile, metadata);
            log.debug(
                    "After put file, do copy, name: {}, md5: {}, srcOssBucketName: {}, srcKey: {}, destOssBucketName: {}, destKey: {}",
                    uniqueName, md5String, ossBucketName, randomKey, ossBucketName, fileKey);
            client.copyObject(ossBucketName, randomKey, ossBucketName, fileKey);
            log.debug(
                    "Finish copy file, name: {}, md5: {}, srcOssBucketName: {}, srcKey: {}, destOssBucketName: {}, destKey: {}",
                    uniqueName, md5String, ossBucketName, randomKey, ossBucketName, fileKey);
            entity.setStorageId(randomKey);
            fileModelRepository.save(entity);
            entity.setStorageId(fileKey);
            fileVo.setUrl(entity.getAbsolutePath() == null ? "" : entity.getAbsolutePath());
            return fileVo;
        } catch (Exception ex) {
            log.error("上传图片失败.", ex);

            if (!MyStringUtils.isEmpty(uniqueName)) {
                log.error("upload file failed, but still return file info.");
                entity.setId(-1L);
                fileVo.setUrl(entity.getAbsolutePath() == null ? "" : entity.getAbsolutePath());
                return fileVo;
            }

            throw new AppException(-100, "文件上传失败");
        } finally {
            tmpFile.delete();
        }
    }
    
    public class FileTask implements Callable<FileTaskDto> {

        private FileTaskDto fileTaskDto;
        
        public FileTask(FileTaskDto fileTaskDto) {
        	this.fileTaskDto = fileTaskDto;
        }

    	@Override
    	public FileTaskDto call() throws Exception {
    		FileVo projectVo  = uploadToOss(fileTaskDto.getMultipartFile(), fileTaskDto.getUId(), fileTaskDto.getType());
    		FileRespVo respVo = fileTaskDto.getFileRespVo();
    		respVo.setImageUrl(projectVo.getUrl());
    		respVo.setImageSize(projectVo.getSize());
    		fileTaskDto.setFileRespVo(respVo);
    		log.info("进入"+projectVo.getUrl());
    		return fileTaskDto;
    	}

    }
}
