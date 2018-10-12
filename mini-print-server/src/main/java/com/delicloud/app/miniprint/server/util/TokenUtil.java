package com.delicloud.app.miniprint.server.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.delicloud.app.miniprint.core.util.Md5Util;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.ThirdpartyUserRepository;
import com.delicloud.app.miniprint.server.repository.UserRepository;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.delicloud.app.miniprint.core.entity.TThirdpartyUser;
import com.delicloud.app.miniprint.core.entity.TToken;
import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.platform.cloudapp.gateway.sdk.DeliGatewayApiClient;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserAuthRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.resp.DeliUserAuthResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.bo.DeliResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.consts.EClientType;
import com.delicloud.platform.common.cache.util.RedisClient;
import com.delicloud.platform.common.lang.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenUtil {
	
	@Autowired
	private RedisClient redisClient;

	@Value("${delicloud.printer.appkey}")
	private String printerAppkey;

	@Value("${delicloud.printer.appid}")
	private String printerAppid;
	
    @Resource(name = "printerDeliCloudGatewayAppClient")
    private DeliGatewayApiClient deliCloudGatewayAppClient;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ThirdpartyUserRepository thirdpartyUserRepository;

	public void deleteToken(Long uid) {
		//校验该用户是否有token
		TToken tToken = (TToken)redisClient.get(Constants.PRE_REDIS_KEY_USER_TOKEN, uid);
		if (null != tToken)
			redisClient.delete(tToken);
	}

	/**
	 * 创建token
	 * @throws UnsupportedEncodingException 
	 */
	public  TToken createToken(String uid) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(String.valueOf(uid))) {
			return null;
		}
		Date date = new Date();
		String dateFromat = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT_FULL).replaceAll(" ", "");
		String token = UUID.randomUUID().toString().replace("-", "")+dateFromat;
		TToken tokenModle = new TToken();
		tokenModle.setUId(uid);
		tokenModle.setToken(Md5Util.encode(token));
		log.info("uid"+uid+"token:" + Md5Util.encode(token));
		log.info("uid"+uid+"token:" + Base64.getEncoder().encodeToString((uid+"|"+Md5Util.encode(token)).getBytes("utf-8")));
		redisClient.set(Constants.PRE_REDIS_KEY_USER_TOKEN, uid, tokenModle , 7 , TimeUnit.DAYS);
		return tokenModle;
	}

	public static Long getUid(String authentication) {
		try {
			if (StringUtils.isBlank(authentication)) {
				throw new AppException(-100, "token 不能为空");
			}
			authentication = new String(Base64.getDecoder().decode(authentication ), "utf-8");
			String[] param = authentication.split("\\|");
			if (StringUtils.isBlank(param[0]))
				throw new AppException(-100, "用户id不能为空");

			return Long.parseLong(param[0]);
		} catch (UnsupportedEncodingException e) {
			throw new AppException(-100, "token 信息有误");
		}
	}
	
	public TToken getToken(String authentication) {
		try {
			if (StringUtils.isBlank(authentication)) {
				return null;
			}
			String[] header = authentication.split("\\|");
			String bigSign = header[0];
			String time = header[1];
			String original = new String(Base64.getDecoder().decode(SignUtil.toBytes(bigSign)), "utf-8");
			String[] param = original.split("\\|");
			String userId = "";
			String token = "";
			String sign = "";
			if (param.length == 0) {
				return null;
			} else if (param.length > 1) {
				userId = param[0];
				token = param[1];
				sign = param[2];
			} else {
				sign = param[0];
			}
			TToken tokenModle = new TToken();
			tokenModle.setUId(userId);
			tokenModle.setToken(token);
			tokenModle.setTimestamp(time);
			tokenModle.setSign(sign);
			return tokenModle;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	 

	/**
	 * 数据库验证token是否正确，或者token错误，没有返回false
	 * 验证正确，返回true
	 */
//	public boolean checkToken(TToken tokenModle) {
//		if(null == tokenModle) {
//			return false;
//		}
//		String uid = tokenModle.getUId();
//		TToken persistenToken = (TToken) redisClient.get(Constants.PRE_REDIS_KEY_USER_TOKEN, uid);
//		if(null == persistenToken) {
//			log.info("uid :" + uid+"token not found");
//			return false;
//		}
//		if(!persistenToken.getToken().equals(tokenModle.getToken())) {
//			log.info("uid :" + uid+"token error");
//			return false;
//		}
//	     //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
//		redisClient.set(Constants.PRE_REDIS_KEY_USER_TOKEN, uid, tokenModle , 7 , TimeUnit.DAYS);
//		return true;
//	}

	public boolean checkTokenPlat(TToken tokenModle) {
		if(null == tokenModle) {
			return false;	
		}
		TUser tUser = userRepository.findOne(Long.valueOf(tokenModle.getUId()));
		if(null == tUser) {
			return false;
		}
		TThirdpartyUser thirdpartyUser = thirdpartyUserRepository.findByUid(Long.valueOf(tokenModle.getUId()));
		if(null == thirdpartyUser) {
			return false;
		}
		if(TThirdpartyUser.THIRDPARTY.equals(thirdpartyUser.getThirdparty())) {
			DeliUserAuthRequest userAuthRequest = new DeliUserAuthRequest();
			userAuthRequest.setClientId(printerAppid);
			userAuthRequest.setClientType(EClientType.app);
			userAuthRequest.setToken(tokenModle.getToken());
			userAuthRequest.setUserId(thirdpartyUser.getThirdpartyUid());
			log.info("UserId = {} , Token = {} userAuth param= {}", tokenModle.getUId(), tokenModle.getToken() ,JsonUtil.getJsonFromObject(userAuthRequest));
			DeliResponse<DeliUserAuthResponse> deliResponse =  deliCloudGatewayAppClient.userAuth(userAuthRequest);
			log.info("UserId = {} , Token = {} userAuth output= {}", tokenModle.getUId(), tokenModle.getToken() ,JsonUtil.getJsonFromObject(deliResponse));
			if(null == deliResponse) {
				log.error("平台异常");
				return false;
			}
			if(!deliResponse.isSuccess()) {
				log.error("平台异常");
				return false;
			}
			tokenModle.setUId(String.valueOf(tUser.getId()));
			return true;
			
		}else {
			log.error("平台异常");
			return false;
		}
	}

	public String[] getSign(String authentication) {
		try {
			if (StringUtils.isBlank(authentication)) {
				return null;
			}
			String[] header = authentication.split("\\|");
			String bigSign = header[0];
			String original = new String(Base64.getDecoder().decode(SignUtil.toBytes(bigSign)), "utf-8");
			String[] param = original.split("\\|");
			String sign = "";
			if (param.length > 1) {
				sign = param[2];
			} else {
				sign = param[0];
			}
			String time = header[1];
			return new String[]{sign, time};
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 校验 token
	 * @param signAndTime 签名 + 时间戳
	 * @return
	 */
	public boolean checkSign(String[] signAndTime, String requestURI) {
		// Base64(uid|token|md5(uri+key))|timestamp

		long currentTimeMillis = System.currentTimeMillis();

		String timestamp = signAndTime[1];
		long beginTimeMillis = Long.parseLong(timestamp);

		if (null == signAndTime[0])
			return false;

		if (!signAndTime[0].equals(Md5Util.encode(requestURI + printerAppkey)))
			return false;

		if ((currentTimeMillis - beginTimeMillis) > 60000)
			return false;
		return true;
	}
}
