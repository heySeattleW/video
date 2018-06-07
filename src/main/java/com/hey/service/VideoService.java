package com.hey.service;

import java.util.List;
import java.util.Map;

/**
 * Created by hey on 2018/3/23.
 */
public interface VideoService {

    /**
     * 注册用户
     * @param map
     * @return
     * @throws Exception
     */
    Map addUser(Map map)throws Exception;

    /**
     * 返回用户信息
     * @param uid
     * @return
     * @throws Exception
     */
    Map getUserInfo(Long uid)throws Exception;

    /**
     * 判断用户是否有权限使用接口
     * @param uid
     * @param openid
     * @return
     * @throws Exception
     */
    boolean userIsAuthed(Long uid, String openid)throws Exception;

    /**
     * 获取词条
     * @return
     * @throws Exception
     */
    List<Map> getWords()throws Exception;

    /**
     * 新增视频和音频
     * @param map
     * @throws Exception
     */
    void addVideoAndAudio(Map map)throws Exception;

    /**
     * 获取音频和视频
     * @param uid
     * @return
     * @throws Exception
     */
    List<Map> getVideoAndAudio(Long uid)throws Exception;

    /**
     * 添加句子
     * @param words
     * @return 返回句子和识别出语音的mp3
     * @throws Exception
     */
    Map addWords(String words)throws Exception;
}
