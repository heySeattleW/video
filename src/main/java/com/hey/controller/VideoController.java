package com.hey.controller;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.hey.service.VideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hey on 2018/3/23.
 */
@RestController
public class VideoController {

    @Autowired
    private VideoService videoService;

    protected static final int ERROR_CODE = 0;//错误码,level=error
    protected static final int SUCCESS_CODE = 1;//成功码,level=success

    protected static final String ERROR_MESSAGE = "服务器异常";//错误消息
    protected static final String SUCCESS_MESSAGE = "成功";//成功消息

    @PostMapping(value = "/register")
    @ApiOperation(value = "注册用户进入系统",httpMethod = "POST")
    public Object addUser(@ApiParam(name="code",value = "code",required = true)
                          @RequestParam(value = "code",required = true)String code,
                          @ApiParam(name="sex",value = "性别，1是男，2是女，0是未知",required = true)
                          @RequestParam(value = "sex",required = true)Integer sex,
                          @ApiParam(name="city",value = "城市",required = true)
                          @RequestParam(value = "city",required = true)String city,
                          @ApiParam(name="nickName",value = "昵称",required = true)
                          @RequestParam(value = "nickName",required = true)String nickName,
                          @ApiParam(name="head",value = "头像",required = true)
                          @RequestParam(value = "head",required = true)String head
    ){
        Map map = new HashMap();
        Map parMap = new HashMap();
        parMap.put("code",code);
        parMap.put("head",head);
        parMap.put("nickName",nickName);
        parMap.put("city",city);
        parMap.put("sex",sex);
        try {
            map.put("result",videoService.addUser(parMap));
            map.put("code",SUCCESS_CODE);
            map.put("msg",SUCCESS_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",ERROR_CODE);
            map.put("msg",ERROR_MESSAGE);
        }
        return map;
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "获取用户信息",httpMethod = "GET")
    public Object getUserInfo(@ApiParam(name="uid",value = "用户id",required = true)
                              @RequestParam(value = "uid",required = true)Long uid){
        Map map = new HashMap();
        try {
            map.put("result",videoService.getUserInfo(uid));
            map.put("code",SUCCESS_CODE);
            map.put("msg",SUCCESS_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",ERROR_CODE);
            map.put("msg",ERROR_MESSAGE);
        }
        return map;
    }

    @PostMapping(value = "/record/start")
    @ApiOperation(value = "点击录播，返回推流地址和播放地址",httpMethod = "POST")
    public Object startRecord(@ApiParam(name="code",value = "code",required = true)
                          @RequestParam(value = "code",required = true)String code,
                          @ApiParam(name="sex",value = "性别，1是男，2是女，0是未知",required = true)
                          @RequestParam(value = "sex",required = true)Integer sex,
                          @ApiParam(name="city",value = "城市",required = true)
                          @RequestParam(value = "city",required = true)String city,
                          @ApiParam(name="nickName",value = "昵称",required = true)
                          @RequestParam(value = "nickName",required = true)String nickName,
                          @ApiParam(name="head",value = "头像",required = true)
                          @RequestParam(value = "head",required = true)String head
    ){
        Map map = new HashMap();
        Map parMap = new HashMap();
        parMap.put("code",code);
        parMap.put("head",head);
        parMap.put("nickName",nickName);
        parMap.put("city",city);
        parMap.put("sex",sex);
        try {
            map.put("result",videoService.addUser(parMap));
            map.put("code",SUCCESS_CODE);
            map.put("msg",SUCCESS_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",ERROR_CODE);
            map.put("msg",ERROR_MESSAGE);
        }
        return map;
    }



}
