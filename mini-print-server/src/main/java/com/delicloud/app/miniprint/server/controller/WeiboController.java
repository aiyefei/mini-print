package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.QueryWeiboPageDto;
import com.delicloud.app.miniprint.core.dto.SendWeiboDto;
import com.delicloud.app.miniprint.core.dto.WeiboDto;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.core.vo.WeiboPageVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.service.IWeiboService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: dy
 * @Description: 微博前端控制器
 * @Date: 2018/9/11 11:31
 */
@RestController
@RequestMapping("/weibo")
public class WeiboController {

    @Autowired
    IWeiboService weiboService;

    @ApiOperation("发微博")
    @PostMapping("/send")
    public RespBase send (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                          @RequestBody SendWeiboDto sendWeiboDto) {

        weiboService.sendWeibo(uid, sendWeiboDto);
        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("点赞微博")
    @PostMapping("/goodWeibo")
    public RespBase goodWeibo (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                               @RequestBody WeiboDto dto) {

        weiboService.goodWeibo(uid, dto);
        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("删除微博")
    @PostMapping("/delete")
    public RespBase delete (@RequestBody WeiboDto dto) {

        weiboService.deleteWeibo(dto);
        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("分页查询我的微博列表")
    @PostMapping("/queryPage")
    public RespBase<PageVo<WeiboPageVo>> queryPage(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                   @RequestBody QueryWeiboPageDto queryWeiboPageDto){

        return new ResponseVo().ok(weiboService.queryPage(uid, queryWeiboPageDto));
    }

    @ApiOperation("根据微博 id 查询文件列表")
    @PostMapping("/queryByWbId")
    public RespBase queryByWbId(@RequestBody WeiboDto dto) {

        return new ResponseVo().ok(weiboService.queryByWbId(dto));
    }

    @ApiOperation("更新用户浏览数")
    @PostMapping("/updateViews")
    public RespBase updateViews(@RequestBody WeiboDto dto){

        weiboService.updateViews(dto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("分页查询所有微博列表（推荐/关注人）")
    @PostMapping("/queryAllWeibo")
    public RespBase<PageVo<WeiboPageVo>> queryAllWeibo(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                       @RequestBody QueryWeiboPageDto dto){

        return new ResponseVo().ok(weiboService.queryAllWeibo(uid,dto));
    }

    @ApiOperation("分页根据UId查询该用户所有的微博")
    @PostMapping("queryAllWeiboByUid")
    public RespBase<PageVo<WeiboPageVo>> queryAllWeiboByUid (@RequestBody QueryWeiboPageDto dto){

        return new ResponseVo().ok(weiboService.queryAllWeiboByUid(dto));
    }

    @ApiOperation("分页根据cgId查询该分组所有的微博")
    @PostMapping("queryAllWeiboByCgId")
    public RespBase<PageVo<WeiboPageVo>> queryAllWeiboByCgId (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                              @RequestBody QueryWeiboPageDto dto){

        return new ResponseVo().ok(weiboService.queryAllWeiboByCgId(uid,dto));
    }

}
