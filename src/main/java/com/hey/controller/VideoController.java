package com.hey.controller;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.hey.service.VideoService;
import com.hey.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
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

    @GetMapping(value = "/info/video")
    @ApiOperation(value = "获取视频和音频信息",httpMethod = "GET")
    public Object getVideoInfo(@ApiParam(name="uid",value = "用户id",required = false)
                              @RequestParam(value = "uid",required = false)Long uid){
        Map map = new HashMap();
        try {
            map.put("result",videoService.getVideoAndAudio(uid));
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
//    public Object startRecord(@ApiParam(name="uid",value = "uid",required = true)
//                            @RequestParam(value = "uid",required = true)Long uid
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
        System.out.println(result);
        Map retMap = String2Map.getValueGson(result);
//        Map retMap = new HashMap();
//        retMap.put("event_type",100);
//        retMap.put("stream_id","10742359");
//        retMap.put("video_url","http://1256242181.vod2.myqcloud.com/1049971fvodgzp1256242181/f8137d667447398155384996696/f0.mp4");
        double eventType = (Double)retMap.get("event_type");
        //从streamid中获取用户id
        String streamId = retMap.get("stream_id").toString();
        if(eventType==100){
            Map map = new HashMap();
            //回调类型为录制完成回调
            //拿到视频地址
            String videoUrl = retMap.get("video_url").toString();
            String savePath = request.getServletContext().getRealPath("./public/video/");
            File file = new File(savePath);
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = streamId;
            String targetPath = savePath+fileName;
            //将视频下载到自己服务器
            VideoUtil.downLoadFromUrl(videoUrl,fileName+".mp4",savePath);

            //从视频中提取音频
            String videoPath = targetPath+".mp4";
            String audioPath = targetPath+".mp3";
            AudioConverter.getAudioFromVideo(videoPath,audioPath);
            //将音频进行语音识别
            //首先拿到音频的时间看需不需要切割
            int time = AudioConverter.getAudioTime(audioPath);
            String words = "";
            if (time>60){
                //音频时长大于60，切割,获取识别后的文字
                words = AudioConverter.splitAudio(audioPath,targetPath);
            }else {
                //时长小于60s，直接识别
                //先将mp3转成wav
                boolean flag = AudioConverter.AudioConverter(audioPath,targetPath+".wav", "wav");
                if(flag){
                    words = VoiceDistinguish.getVoiceString(targetPath+".wav");
                }
            }
            map.put("words",words);
            map.put("audioTime",time);
            map.put("uid",streamId.split("_")[0]);
            //将视频和音频的地址存进数据库
            map.put("audio",audioPath);
            map.put("video",videoPath);
            videoService.addVideoAndAudio(map);
        }
        return "{\n" +
                "    \"code\": 0,\n" +
                "    \"message\": \"\"\n" +
                "}";
    }

}
