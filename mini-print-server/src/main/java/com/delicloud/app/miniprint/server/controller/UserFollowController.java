package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.FollowDto;
import com.delicloud.app.miniprint.core.dto.GetFollowsOrFollowersDto;
import com.delicloud.app.miniprint.core.dto.QueryFansOrFollowByNickNameDto;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.core.vo.UserFollowPageVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.service.IUserFollowService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: dy
 * @Description: 用户关注前端控制器
 * @Date: 2018/8/30 16:11
 */
@RestController
@RequestMapping("/user/follow")
public class UserFollowController {

    @Autowired
    private IUserFollowService userFollowService;

    @ApiOperation("关注")
    @PostMapping("/")
    public RespBase follow (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                            @RequestBody FollowDto dto) {

        userFollowService.follow(uid, dto);

        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("取消关注")
    @PostMapping("/cancel")
    public RespBase cancel (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                            @RequestBody FollowDto dto) {

        userFollowService.cancel(uid, dto);

        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("分页查询我的粉丝/关注列表")
    @PostMapping("/getFollowsOrFollowers")
    public RespBase<PageVo<UserFollowPageVo>> getFollowsOrFollowers(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                                    @RequestBody GetFollowsOrFollowersDto dto){

        return new ResponseVo().ok(userFollowService.getFollowsOrFollowers(uid, dto));
    }

    @ApiOperation("查询粉丝/关注")
    @PostMapping("/queryFansOrFollowByNickName")
    public RespBase<List<UserFollowPageVo>> queryFansOrFollowByNickName(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                                        @RequestBody QueryFansOrFollowByNickNameDto dto){

        return new ResponseVo().ok(userFollowService.queryFansOrFollowByNickName(uid, dto));
    }
}
