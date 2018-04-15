package com.hey.service.impl;

import com.hey.dao.VideoDao;
import com.hey.service.VideoService;
import com.hey.utils.RandomNumberGenerator;
import com.hey.utils.VideoUtil;
import com.hey.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hey on 2018/3/23.
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

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
            map.put("stream_address", VideoUtil.getStreamAddress(code));
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
        return videoDao.getWords();
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
}
