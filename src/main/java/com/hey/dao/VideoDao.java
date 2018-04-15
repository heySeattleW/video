package com.hey.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by hey on 2018/3/23.
 */
public interface VideoDao {

    /**
     * 添加用户之前判断用户是不是已经存在
     * @param openid
     * @return
     * @throws Exception
     */
    Map userIsExist(String openid)throws Exception;

    /**
     * 注册用户
     * @param map
     * @return
     * @throws Exception
     */
    Long addUser(Map map)throws Exception;

    /**
     * 判断用户是否有权限使用接口
     * @param uid
     * @param openid
     * @return
     * @throws Exception
     */
    Integer userIsAuthed(Long uid, String openid)throws Exception;

    /**
     * 返回用户信息
     * @param uid
     * @return
     * @throws Exception
     */
    Map getUserInfo(Long uid)throws Exception;

    /**
     * 更新用户信息
     * @param map
     * @throws Exception
     */
    void updateUserInfo(Map map)throws Exception;

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
     * @param map
     * @return
     * @throws Exception
     */
    List<Map> getVideoAndAudio(Map map)throws Exception;
}
