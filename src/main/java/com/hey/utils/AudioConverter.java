package com.hey.utils;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

/**
 * Created by hey on 2018/1/16.
 */
public class AudioConverter {

    //格式转换
    public static boolean AudioConverter(String source,String target,String format)throws Exception{
        AudioAttributes audio = new AudioAttributes();
        EncodingAttributes attrs = new EncodingAttributes();
        audio.setSamplingRate(16000);
        audio.setChannels(1);
        //audio.setBitRate(16000);
        //audio.setCodec("wavpack");

        attrs.setFormat(format);
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        boolean flag = false;
        try {
            File sourceFile = new File(source);
            File targetFile = new File(target);
            encoder.encode(sourceFile,targetFile,attrs);
            flag = targetFile.exists();
            if(flag){
                sourceFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
    public static void main(String[] args){
        try {
//            File source = new File("C:\\apache-tomcat-8.0.48\\webapps\\redpack\\public\\voice\\86B307E8FACE7549A77C23B2CDCA8535.mp3");
//            File target = new File("C:\\apache-tomcat-8.0.48\\webapps\\redpack\\public\\voice\\886B307E8FACE7549A77C23B2CDCA8535.wav");
//            //AudioConverter(source,target);
//            source.delete();
            //getAudioFromVideo();
            //splitAudio();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //从视频中分离出音频
    public static void getAudioFromVideo(){

        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        audio.setSamplingRate(16000);
        audio.setChannels(1);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        File source = new File("C:\\Users\\hey\\Downloads\\video.mp4");
        File target = new File("C:\\Users\\hey\\Desktop\\a.mp3");
        try {
            encoder.encode(source,target,attrs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //分割音频成小段
    public static void splitAudio(String sourcePath,String targetPath){
        long beginTime = System.currentTimeMillis();
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        audio.setSamplingRate(16000);
        audio.setChannels(1);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        File source = new File("C:\\Users\\hey\\Documents\\WeChat Files\\jaychou114118\\Files\\10002989.mp3");
        int time;

        try {
            FileInputStream fis = new FileInputStream(source);
            int b = fis.available();
            Bitstream bt = new Bitstream(fis);
            Header h = bt.readFrame();
            time = (int) h.total_ms(b)/1000;
            int size = time/60+1;
            float duration = 59F;
            float offset = 0F;
            String result = "";
            for(int i=0;i<size;i++){
                String path = "C:\\Users\\hey\\Desktop\\MP3\\a"+i+".wav";
                File target = new File(path);
                attrs.setDuration(duration);
                attrs.setOffset(offset);
                offset = duration+offset;
                encoder.encode(source,target,attrs);
                //识别
                long tt  = System.currentTimeMillis();
                result += VoiceDistinguish.getVoiceString(path);
                long te  = System.currentTimeMillis();
                System.out.println(te-tt);
                System.out.println(result);
            }
            long endTime = System.currentTimeMillis();
            long ti = endTime-beginTime;
            System.out.print("耗时："+ti);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
