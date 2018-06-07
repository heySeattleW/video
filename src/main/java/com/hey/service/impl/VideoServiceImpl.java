package com.hey.service.impl;

import com.baidu.aip.speech.AipSpeech;
import com.hey.dao.VideoDao;
import com.hey.service.VideoService;
import com.hey.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hey.utils.VoiceDistinguish.API_KEY;
import static com.hey.utils.VoiceDistinguish.APP_ID;
import static com.hey.utils.VoiceDistinguish.SECRET_KEY;

/**
 * Created by hey on 2018/3/23.
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    private static final String url = "https://www.airobin.com.cn/audio/";
    private static final String path = "/root/public/audio/";
//    private static final String path = "C:\\Users\\er\\Desktop\\";

    @Override
    public Map addUser(Map map) throws Exception {
        Map retMap = new HashMap();
        Map tokenMap = WxUtil.getWxAccessToken(map.get("code").toString());
//        Map tokenMap = new HashMap();
//        tokenMap.put("openid","openid");
//        tokenMap.put("session_key","session_key");
        String openid = tokenMap.get("openid").toString();
        String session_key = tokenMap.get("session_key").toString();

        map.put("openid",openid);
        map.put("session_key",session_key);
        //添加用户之前判断用户是不是已经添加
        Map flag = videoDao.userIsExist(openid);
        Long uid;
        if(flag!=null){
            //用户存在不做任何操作，返回用户id
            uid = Long.valueOf(flag.get("id").toString());
            retMap.put("uid",uid);
            retMap.put("openid",openid);
        }else {
            String code = RandomNumberGenerator.generateNumber2();
            map.put("id", code);
            map.put("stream_address", TecentCloudUtils.getPushUrl(code).get("realPushUrl").toString());
            map.put("flv_address", VideoUtil.getPlayAddressFLV(code));
            map.put("hls_address", VideoUtil.getPlayAddressHLS(code));
            map.put("rtmp_address", VideoUtil.getPlayAddressRTMP(code));
            videoDao.addUser(map);
            uid = Long.valueOf(map.get("id").toString());
            retMap.put("uid",uid);
            retMap.put("openid",openid);
        }
        return retMap;
    }

    @Override
    public Map getUserInfo(Long uid) throws Exception {
        return videoDao.getUserInfo(uid);
    }

    @Override
    public boolean userIsAuthed(Long uid, String openid) throws Exception {
        Integer flag = videoDao.userIsAuthed(uid, openid);
        if(flag>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取词条
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> getWords()throws Exception{
        List<Map> list =  videoDao.getWords();
        //从list中随机取出六条
        List tempList;
        Collections.shuffle(list);
        tempList = list.subList(0,6);
        return tempList;
    }

    /**
     * 新增视频和音频
     * @param map
     * @throws Exception
     */
    public void addVideoAndAudio(Map map)throws Exception{
        videoDao.addVideoAndAudio(map);
    }

    public List<Map> getVideoAndAudio(Long uid)throws Exception{
        Map map = new HashMap();
        map.put("uid",uid);
        return videoDao.getVideoAndAudio(map);
    }

    /**
     * 添加句子
     * @param words
     * @return 返回句子和识别出语音的mp3
     * @throws Exception
     */
    public Map addWords(String words)throws Exception{
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String name = sdf.format(new Date())+".mp3";
        //语音合成
        VoiceDistinguish.synthesis(client,words,path,name);
        //合成之后将数据存在数据库
        String fileName = path+name;
        int audio_time = AudioConverter.getAudioTime(fileName);
        String audio = url+name;
        Map map = new HashMap();
        map.put("words",words);
        map.put("audio",audio);
        map.put("audio_time",audio_time);
        videoDao.addWords(map);
        return map;
    }
}
