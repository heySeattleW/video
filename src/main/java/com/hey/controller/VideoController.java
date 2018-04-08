package com.hey.controller;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.hey.service.VideoService;
import com.hey.utils.String2Map;
import com.hey.utils.UploadSomething;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

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

//    @PostMapping(value = "/record/start")
//    @ApiOperation(value = "点击开始录播",httpMethod = "POST")
//    public Object startRecord(@ApiParam(name="code",value = "code",required = true)
//                          @RequestParam(value = "code",required = true)String code,
//                          @ApiParam(name="sex",value = "性别，1是男，2是女，0是未知",required = true)
//                          @RequestParam(value = "sex",required = true)Integer sex,
//                          @ApiParam(name="city",value = "城市",required = true)
//                          @RequestParam(value = "city",required = true)String city,
//                          @ApiParam(name="nickName",value = "昵称",required = true)
//                          @RequestParam(value = "nickName",required = true)String nickName,
//                          @ApiParam(name="head",value = "头像",required = true)
//                          @RequestParam(value = "head",required = true)String head
//    ){
//        Map map = new HashMap();
//        Map parMap = new HashMap();
//        parMap.put("code",code);
//        parMap.put("head",head);
//        parMap.put("nickName",nickName);
//        parMap.put("city",city);
//        parMap.put("sex",sex);
//        try {
//            map.put("result",videoService.addUser(parMap));
//            map.put("code",SUCCESS_CODE);
//            map.put("msg",SUCCESS_MESSAGE);
//        }catch (Exception e){
//            e.printStackTrace();
//            map.put("code",ERROR_CODE);
//            map.put("msg",ERROR_MESSAGE);
//        }
//        return map;
//    }

//    @PostMapping(value = "/upload/video")
//    @ApiOperation(value = "上传视频",httpMethod = "POST")
//    public Object uploadVideo(@ApiParam(name="video",value = "视频",required = true)
//                                  @RequestBody(required = true)MultipartFile video,
//                              HttpServletRequest request){
//        Map map = new HashMap();
//        String voice_dir;
//        String voice_path;
//        voice_dir = "/public/video/";
//        voice_path = request.getServletContext().getRealPath("/public/video");
//        try {
//            map.put("result", UploadSomething.uploadVideo(voice_path, video, voice_dir));
//            map.put("code", SUCCESS_CODE);
//            map.put("msg", SUCCESS_MESSAGE);
//            //支付完成后走发送红包逻辑
//        }catch (Exception e){
//            e.printStackTrace();
//            map.put("code",ERROR_CODE);
//            map.put("msg",ERROR_MESSAGE);
//        }
//        return map;
//    }


    //腾讯云回调
    @PostMapping(value = "/callback")
    @ApiOperation(value = "回调接口",httpMethod = "POST")
    public Object tencentCallBack(HttpServletRequest request)throws Exception {
        InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");
            //String status = map.get("result_code").toString();
            LOGGER.info(result);
        Map retMap = String2Map.getValueGson(result);
        int eventType = (Integer)retMap.get("event_type");
        if(eventType==100){
            //回调时间为录制完成回调
            //拿到视频地址
            String videoUrl = retMap.get("video_url").toString();
        }
        return "{ \"code\":0 }";
    }

    public static void main(String[] args)throws Exception {
        String xx = "http://1256242181.vod2.myqcloud.com/1049971fvodgzp1256242181/f8137d667447398155384996696/f0.mp4";
        FileInputStream inputStream = new FileInputStream(xx);
        File file = new File(xx);
        System.out.println(file.exists());

    }

}
