package com.hey.utils;

import com.hey.utils.String2Map;

import java.util.Map;
import java.util.Random;


/**
 * Created by hey on 2018/1/15.
 */
public class WxUtil {

    private static final String APP_SECRET = "3db482bb7be32ce0fd08b4b75d6334e8";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String GRANT_TYPE_USER = "snsapi_userinfo";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    private static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
    private static final String LANG = "zh_CN";
    private static final String CODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
    public static final String XIAO_APP_ID = "wx0e7cf8fd6042800f";//小程序appid

    //获取ACCESS_TOKEN(code换取)
    public static Map getWxAccessToken(String code){
        String result =  HttpRequestUtil.sendGet(TOKEN_URL,"js_code="+code+"&grant_type="+GRANT_TYPE+"&appid="+XIAO_APP_ID+"&secret="+APP_SECRET);
        Map map = String2Map.getValueGson(result);
        return map;
    }



    //获取用户信息
    public static Map getUserInfo(String accessToken,String openid){
        String result = HttpRequestUtil.sendGet(USER_INFO_URL,"access_token="+accessToken+"&openid="+openid+"&lang="+LANG);
        Map infoMap = String2Map.getValueGson(result);
        return infoMap;
    }

    //红包算法
    public static double getRandomMoney(int remainSize,double remainMoney,double min) {
        // remainSize 剩余的红包数量
        // remainMoney 剩余的钱
        if (remainSize == 1) {
            remainSize--;
            return (double) Math.round(remainMoney * 100) / 100;
        }
        Random r     = new Random();
        double max   = remainMoney / remainSize * 2;//最多剩余的金额除以个数的2倍
        double money = r.nextDouble() * max;
        money = money <= min ? 1.00: money;
        money = Math.floor(money * 100) / 100;
        remainSize--;
        remainMoney -= money;

        if(remainMoney/remainSize<1){
            money = getRandomMoney(remainSize+1,remainMoney+money,min);
        }
        return money;
    }

//    //生成小程序二维码，返回图片路径
//    public static String getCode(String token,String scene,Integer flag){
//        String url = CODE_URL+token;
//        Map parMap = new HashMap();
//        String page;
//        if(flag==0){
//            page = "pages/commandDetail/commandDetail";
//        }else if(flag==1){
//            page = "pages/answerDetail/answerDetail";
//        }else if(flag==2){
//            page = "pages/imageDetail/imageDetail";
//        }else {
//            page = "pages/hitDetail/hitDetail";
//        }
////        parMap.put("\"page\"","\""+page+"\"");
//        parMap.put("\"scene\"","\""+scene+"\"");
//        System.out.print(parMap.toString());
//        String result = HttpRequestUtil.httpsRequest(url,"POST",parMap.toString().replace("={",":{").replace("=\"",":\""));
//        return result;
//    }




    //获取随机订单号
    public static String getTimeStamp() {
        return "redpack"+String.valueOf(System.currentTimeMillis() / 1000);
    }




}
