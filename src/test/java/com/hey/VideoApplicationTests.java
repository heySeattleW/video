package com.hey;

import com.hey.service.impl.VideoServiceImpl;
import com.hey.utils.TecentCloudUtils;
import com.hey.utils.TestLfasr;
import com.hey.utils.VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoApplicationTests {

	@Autowired
	private VideoServiceImpl video;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testFun()throws Exception{
		Map map = new HashMap();
		map.put("head","xx");
		map.put("nickName","nickName");
		map.put("sex",1);
		map.put("city","xx");
		//Map id = video.addUser(map);
		System.out.println(video.getUserInfo(10742359L));
		System.out.println(video.getWords());
	}

	@Test
	public void testStream()throws Exception{
		//String stream = VideoUtil.getStreamAddress("18504769");
		//
		//VideoUtil.getStreamAddress("1850476");
		Map map = TecentCloudUtils.getPushUrl("888888");
		VideoUtil.allowStream("21749_888888",1,Long.valueOf(map.get("txTime").toString()));
		System.out.println(map.toString());
	}

	@Test
	public void testRandomList()throws Exception{
		List<Map> list = video.getWords();
		list.forEach(n->{
			System.out.println(n.get("words").toString());
		});

	}

	@Test
	public void testi()throws Exception{
		int i=1;
//		--i;
		System.out.println(i--);

	}

	@Test
	public void test1(){
		TestLfasr.init("C:\\Users\\hey\\Desktop\\MP3\\10002989.mp3");
	}

	@Test
	public void testT()throws Exception{
		video.testT();
	}
}
