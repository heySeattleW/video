package com.hey.utils;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hey on 2018/1/16.
 */
public class VoiceDistinguish {

    //设置APPID/AK/SK（自己的）
//    public static final String APP_ID = "10646219";
//    public static final String API_KEY = "7GgtkTnC60QZXrB0GpXFs8o4";
//    public static final String SECRET_KEY = "Px7C08okGvGfcYauWOfcdL8X7uOQSApw";

    //客户的
    public static final String APP_ID = "11393593";
    public static final String API_KEY = "M5q40liMRUY2a8KXNpRxPKoO";
    public static final String SECRET_KEY = "arM8TXY7eldOUylTj0ET7e95QR1UvxzV";
//    public static final String REAL_PATH = "C:\\apache-tomcat-8.0.48\\webapps\\redpack";
    public static final String REAL_PATH = "/usr/local/tomcat/webapps/redpack";

    public static final int PURE_PUTONGHUA = 1536;
    public static final int PUTONGHUA = 1537;
    public static final int ENGLISH = 1737;
    public static final int YUEYU = 1637;
    public static final int SICHUANHUA = 1837;
    public static final int PUTONGHUA_YUANCHANG = 1936;

    public static String getVoiceString(String path,int devPid){
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        HashMap options = new HashMap<>();
        if(!getDevPid(devPid)){
            devPid=PUTONGHUA;
        }
        options.put("dev_pid",devPid);
        JSONObject res = client.asr(path, "wav", 16000, options);
        String resString;
        try {
             resString = res.get("result").toString();
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }
        //String[] arrays = resString.split("，");
        //String result = arrays[0];
//        System.out.print("result来了"+result);
//        System.out.print("C:\\apache-tomcat-8.0.48\\webapps\\redpack"+path+"路径来了");
//        System.out.print(res.toString()+"内容来了");
        resString = resString.replace("\\","");
        return resString;
    }

    public static boolean getDevPid(int flag){
        if(flag!=PURE_PUTONGHUA&&flag!=PUTONGHUA&&flag!=ENGLISH&&flag!=YUEYU&&flag!=SICHUANHUA&&flag!=PUTONGHUA_YUANCHANG){
            return false;
        }
        return true;
    }

    //语音合成
    public static void synthesis(AipSpeech client, String txt,String path,String name,String per)throws Exception{

        // 设置可选参数
//        path = "C:\\Users\\hey\\Desktop\\MP3";
//        name = "2.mp3";
        HashMap<String, Object> options = new HashMap();
//        options.put("spd", "5");
//        options.put("pit", "5");
        options.put("per",per);
        TtsResponse res = client.synthesis(txt, "zh", 1, options);
        System.out.println(res.getResult());
        JSONObject result = res.getResult();    //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        byte[] data = res.getData();            //生成的音频数据
        byte2File(data,path,name);
    }
     public static void main(String[] args){
        try {
            String txt = "我不知道年少轻狂，我只懂得胜者为王";
//            synthesis(new AipSpeech(APP_ID,API_KEY,SECRET_KEY),txt);
        }catch (Exception e){
            e.printStackTrace();
        }
     }

    public static void byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
