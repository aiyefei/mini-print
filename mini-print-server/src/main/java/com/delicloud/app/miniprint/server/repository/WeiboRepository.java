package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TWeibo;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface WeiboRepository extends MyRepository<TWeibo,Long> {

    /**
     * 原生SQL根据关注人Id查找所有被关注人的所有微博
     * @param uid 用户Id
     */
    @Query(nativeQuery = true,value = "SELECT * FROM tm_weibo  a where a.status = '1' and uid in (Select to_uid FROM tm_user_follow where from_uid = ?1 )")
    List<TWeibo> findAllByUid(Long uid);

    @Query(value = "select count(*) from tm_weibo WHERE uid = ?1", nativeQuery = true)
    BigInteger findCount(Long checkedUid);
}
