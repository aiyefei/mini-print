package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 17:27
 */
public interface UserRepository extends MyRepository<TUser, Long> {

    TUser findByMobile(String phoneNo);

    /**
     * 原生SQL根据关注人的id查找被关注人的所有信息
     * @aram uid 用户ID
     */
    @Query(nativeQuery = true,value = "SELECT * FROM tm_user WHERE id IN (SELECT to_uid FROM tm_user_follow WHERE from_uid = ?1 )")
    List<TUser> findAllById(Long uid);

    TUser findByIdAndStatus(Long uid, String s);
}
