package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TUserPwd;
import com.delicloud.platform.common.data.repository.MyRepository;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 17:27
 */
public interface UserPwdRepository extends MyRepository<TUserPwd, Long> {

    /**
     * 根据用户 id 查询用户密码对象
     * @param uid 用户 id
     * @param pwdType 用户 id
     * @return
     */
    TUserPwd findByUidAndPwdType(Long uid, String pwdType);
}
