package com.hey.utils;

/**
 * Created by hey on 2018/3/23.
 */
public class VideoUtil {

    protected static final String BIZ_ID="21749";
    protected static final String SAFE_KEY="4430cbb847d33780249633f2e30de07d";
    protected static final String API_KEY="7856ed5be83e57af1ba494488358a42e";
    protected static final String CALL_BACK_URL="";
    protected static final String APP_ID="1256242181";

    public static String getStreamAddress(){
        String address = "rtmp://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID;
        String code = getRandomCode();
        Long time = getTxTime();
        address += code+"?bizid="+BIZ_ID+"&"+Test.getSafeUrl(SAFE_KEY,code,time);
        return address;
    }

    public static String getPlayAddressRTMP(String code){
        String address = "rtmp://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+code;
        return address;
    }

    public static String getPlayAddressFLV(String code){
        String address = "http://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+code+".flv";
        return address;
    }

    public static String getPlayAddressHLS(String code){
        String address = "http://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+code+".m3u8";
        return address;
    }

    public static String getRandomCode(){
        String code = "_12345";
        return code;
    }

    public static Long getTxTime(){
        Long time = System.currentTimeMillis();
        return time;
    }

    //推流
    public static void allowStream(Long time,String stream,int status){
        String url = "http://fcgi.video.qcloud.com/common_access";
        String api = "Live_Channel_SetStatus";
        String sign = getSign();
        //下面调用开始

    }

    public static String getSign(){
        String sign = "";
        return sign;
    }

    /**
     * 开始录制任务,返回task_id
     * @param time
     * @param stream
     * @return
     */
    public static Long startRecord(Long time,String stream){
        String url = "http://fcgi.video.qcloud.com/common_access";
        String api = "Live_Tape_Start";
        int type = 1;
        String format = "mp4";
        String endTime = "";
        String sign = getSign();
        //开始发送请求
        return null;
    }

    public static void stopRecord(Long time,String stream,Long taskId){
        String url = "http://fcgi.video.qcloud.com/common_access";
        String api = "Live_Tape_Stop";
        int type = 1;
        String endTime = "";
        String sign = getSign();
        //开始发送请求

    }
}
