package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TUserFollow;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface UserFollowRepository extends MyRepository<TUserFollow,Long> {

    /**
     * 根据关注人 id 和被关注人 id 查询关注信息
     * @param uid 关注人 id
     * @param  checkedUid 被关注人 id
     * @return
     */
    TUserFollow findByFromUidAndToUid(Long uid, Long checkedUid);

    @Query(value = "select to_uid from tm_user_follow where from_uid = ?1", nativeQuery = true)
    List<BigInteger> findToUidByFromUid(Long uid);
}
