package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TThirdpartyUser;
import com.delicloud.platform.common.data.repository.MyRepository;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 17:27
 */
public interface ThirdpartyUserRepository extends MyRepository<TThirdpartyUser, Long> {
	
	TThirdpartyUser findByThirdpartyUidAndThirdparty(String thirdpartyUid , String thirdparty);

    /**
     * 根据用户 id 查询三方用户信息
     * @param uid 用户 id
     * @return
     */
    TThirdpartyUser findByUid(Long uid);
}
