package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.FollowDto;
import com.delicloud.app.miniprint.core.dto.GetFollowsOrFollowersDto;
import com.delicloud.app.miniprint.core.dto.QueryFansOrFollowByNickNameDto;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.UserFollowPageVo;

import java.util.List;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:16
 */
public interface IUserFollowService {

    /**
     * 关注用户
     * @param followDto 入参
     */
    void follow(Long uid, FollowDto followDto);

    /**
     * 取消关注
     * @param followDto 入参
     */
    void cancel(Long uid, FollowDto followDto);

    /**
     * 分页查询我的粉丝/关注列表
     * @param getFollowsOrFollowersDto 入参 dto
     */
    PageVo<UserFollowPageVo> getFollowsOrFollowers(Long uid, GetFollowsOrFollowersDto getFollowsOrFollowersDto);

    /**
     * 查询我的粉丝
     * @param queryFansOrFollowByNickNameDto 入参
     * @return
     */
    List<UserFollowPageVo> queryFansOrFollowByNickName(Long uid, QueryFansOrFollowByNickNameDto queryFansOrFollowByNickNameDto);
}
