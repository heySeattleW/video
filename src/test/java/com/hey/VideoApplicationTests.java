package com.hey;

import com.hey.service.impl.VideoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
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
}
