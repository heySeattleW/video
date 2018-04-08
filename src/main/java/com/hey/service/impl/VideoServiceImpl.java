package com.hey.service.impl;

import com.hey.dao.VideoDao;
import com.hey.service.VideoService;
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
        String openid = tokenMap.get("openid").toString();
        String session_key = tokenMap.get("session_key").toString();
        //Map infoMap = WxUtil.getUserInfo(accessToken,openid);
        //Map infoMap = WxUtil.getUserInfo(accessToken,openid);

        map.put("openid",openid);
        map.put("session_key",session_key);
        map.put("id",0);
        //添加用户之前判断用户是不是已经添加
        Map flag = videoDao.userIsExist(openid);
        Long uid;
        if(flag!=null){
            //用户存在不做任何操作，返回用户id
            uid = Long.valueOf(flag.get("id").toString());
            retMap.put("uid",uid);
            retMap.put("openid",openid);
        }else {
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
}
