package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.dto.AppVersionRequestDto;
import com.delicloud.app.miniprint.core.vo.PrinterFirmwareVo;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.PrinterFirmwareRepository;
import com.delicloud.app.miniprint.server.service.IPrinterFirmwareService;
import com.delicloud.platform.cloudapp.gateway.sdk.DeliGatewayApiClient;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliJsSignRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliProductInfoQueryRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.resp.DeliProductInfoQueryResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.bo.DeliResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.model.DeliProduct;
import com.delicloud.platform.common.lang.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class PrinterFirmwareServiceImpl implements IPrinterFirmwareService {

    @Autowired
    PrinterFirmwareRepository printerFirmwareRepository;

    @Resource(name = "printerDeliCloudGatewayAppClient")
    private DeliGatewayApiClient deliCloudGatewayAppClient;

    @Override
    public PrinterFirmwareVo getPrinterVersion(AppVersionRequestDto dto) {

        String printerType = dto.getType();
        String firmwareVersion = dto.getVersion();
        if (null == printerType)
            throw new AppException(-1,"型号不能为空");
        if (null == firmwareVersion)
            throw new AppException(-1,"版本号不能为空");

        DeliProductInfoQueryRequest request = new DeliProductInfoQueryRequest();
        request.setModel(printerType);

        log.info("Type= {}  ProductInfo param= {}", dto.getType(), JsonUtil.getJsonFromObject(request));
        DeliResponse<DeliProductInfoQueryResponse> response = deliCloudGatewayAppClient.queryProductInfo(request);
        log.info("Type= {}  ProductInfo output= {}", dto.getType(), JsonUtil.getJsonFromObject(response));

        if (null == response)
            throw new AppException(-1,"服务器异常,请稍后再试");
        if (!response.isSuccess())
            throw new AppException(-1, response.getMsg());
        DeliProductInfoQueryResponse data = response.getData();

        if (null == data)
            throw new AppException(-1,"服务器异常,请稍后再试");
        String version = data.getFirmware().getVersion();
        String firmwareType = data.getModel();
        String firmwareMd5 = data.getFirmware().getChecksum();
        String firmwareUrl = data.getFirmware().getFilePath();


        PrinterFirmwareVo printerFirmwareVo = new PrinterFirmwareVo();
        printerFirmwareVo.setFirmwareVersion(version);//版本号
        printerFirmwareVo.setPrinterType(firmwareType);//型号
        printerFirmwareVo.setFirmwareMD5(firmwareMd5);
        printerFirmwareVo.setFirmwareUrl(firmwareUrl);//下载地址
        return printerFirmwareVo;
    }
}
