package com.hey.utils;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


/**
 * Created by hey on 2018/3/23.
 */
public class VideoUtil {

    protected static final String BIZ_ID="21749";
    protected static final String SAFE_KEY="4430cbb847d33780249633f2e30de07d";
    protected static final String API_KEY="7856ed5be83e57af1ba494488358a42e";
    protected static final String CALL_BACK_URL="";
    protected static final String APP_ID="1256242181";

    public static String getStreamAddress(String code){
        String address = "rtmp://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+"_";
        Long time = getTxTime();
        code = BIZ_ID+"_"+code;
        address += code+"?bizid="+BIZ_ID+"&"+Test.getSafeUrl(SAFE_KEY,code,time);

        return address;
    }

    public static String getPlayAddressRTMP(String code){
        String address = "rtmp://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+"_"+code;
        return address;
    }

    public static String getPlayAddressFLV(String code){
        String address = "http://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+"_"+code+".flv";
        return address;
    }

    public static String getPlayAddressHLS(String code){
        String address = "http://"+BIZ_ID+".livepush.myqcloud.com/live/"+BIZ_ID+"_"+code+".m3u8";
        return address;
    }

    public static String getRandomCode(){
        String code = "_12345";
        return code;
    }

    public static Long getTxTime(){
        Long time = new Date().getTime();
        time = time+86400;
        String timeString = String.valueOf(time);
        timeString = timeString.substring(0,timeString.length()-3);
        time = Long.valueOf(timeString);
        return time;
    }

    //推流
    public static void allowStream(String stream,int status,Long time){

        String url = "http://fcgi.video.qcloud.com/common_access";
        String api = "Live_Channel_SetStatus";

        String sign = getSign(time);
        String param = "appid="+APP_ID+"&interface="+api+"&Param.s.channel_id="+stream+"&Param.n.status="+status+"&t="+time+"&sign="+sign;
        //下面调用开始
        System.out.println(HttpRequestUtil.sendGet(url,param));
    }

    public static String getSign(Long time){
        String timeToString = String.valueOf(time);
        String sign1 = API_KEY+timeToString;
        String sign = Md5Util.MD5(sign1).toLowerCase();
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
        String sign = getSign(time);
        //开始发送请求
        return null;
    }

    public static void stopRecord(Long time,String stream,Long taskId){
        String url = "http://fcgi.video.qcloud.com/common_access";
        String api = "Live_Tape_Stop";
        int type = 1;
        String endTime = "";
        String sign = getSign(time);
        //开始发送请求

    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] data;
        try {
            data = readInputStream(inputStream);
            //文件保存位置
            File saveDir = new File(savePath);
            if(!saveDir.exists()){
                saveDir.mkdirs();
            }
            File file = new File(saveDir+File.separator+fileName);
            System.out.print(file.createNewFile());
            if (!file.exists()){
                new File(saveDir+File.separator+fileName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
            System.out.println("info:"+url+" download success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static void main(String[] args)throws Exception {
        String url = "http://1256242181.vod2.myqcloud.com/1049971fvodgzp1256242181/f8137d667447398155384996696/f0.mp4";
        String file = "f0.mp4";
        String path = "C:\\Users\\hey\\Desktop\\MP3";
        downLoadFromUrl(url,file,path);
    }
}
