package com.hey.utils;


import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.VideoAttributes;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;


import java.io.File;
import java.io.FileInputStream;

/**
 * Created by hey on 2018/1/16.
 */
public class AudioConverter {


    //格式转换(音频)
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
//            encoder.encode(sourceFile,targetFile,attrs);
            flag = targetFile.exists();
            if(flag){
                sourceFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    //格式转换(视频)
    public static boolean videoConverter(String source,String target,String format)throws Exception{
        VideoAttributes video = new VideoAttributes();
        EncodingAttributes attrs = new EncodingAttributes();

        //audio.setBitRate(16000);
        //audio.setCodec("wavpack");

        attrs.setFormat(format);
        attrs.setVideoAttributes(video);
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
            String videoPath = "C:\\Users\\er\\Documents\\WeChat Files\\jaychou114118\\Files\\1.webm";
            String audioPath = "C:\\Users\\er\\Documents\\WeChat Files\\jaychou114118\\Files\\6.mp3";
            String suffix = "mp3";
            String format = "mp4";
            getAudioFromVideo(videoPath,audioPath,suffix);
//            System.out.println(videoConverter(videoPath,audioPath,format));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //从视频中分离出音频
    public static boolean getAudioFromVideo(String videoPath,String audioPath,String suffix){

        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        audio.setSamplingRate(16000);
        audio.setChannels(1);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat(suffix);
        attrs.setAudioAttributes(audio);
        File source = new File(videoPath);
        File target = new File(audioPath);
        try {
//            encoder.encode(source,target,attrs);
            encoder.encode(source,target,attrs);
            //分离过后删除原视频
            if(source.exists()){
                source.delete();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //获取音频时间
    public static int getAudioTime(String audioPath)throws Exception{
        FileInputStream fis = new FileInputStream(audioPath);
        int b = fis.available();
        Bitstream bt = new Bitstream(fis);
        Header h = bt.readFrame();
        int time = (int) h.total_ms(b)/1000;
        return time;
    }

    //分割音频成小段,然后进行语音识别，返回识别出的文字
    public static String splitAudio(String sourcePath,String targetPath){
        long beginTime = System.currentTimeMillis();
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        audio.setSamplingRate(16000);
        audio.setChannels(1);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        File source = new File(sourcePath);
        int time;
        String result = "";
        try {
            FileInputStream fis = new FileInputStream(source);
            int b = fis.available();
            Bitstream bt = new Bitstream(fis);
            Header h = bt.readFrame();
            time = (int) h.total_ms(b)/1000;
            int size = time/60+1;
            float duration = 59F;
            float offset = 0F;
            for(int i=0;i<size;i++){
                String path = targetPath+"_"+i+".wav";
                File target = new File(path);
                attrs.setDuration(duration);
                attrs.setOffset(offset);
                offset = duration+offset;
//                encoder.encode(source,target,attrs);
                encoder.encode(source,target,attrs);
                //识别
                long tt  = System.currentTimeMillis();
                result += VoiceDistinguish.getVoiceString(path,VoiceDistinguish.PUTONGHUA);
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
        return result;
    }
}
