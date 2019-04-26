package com.weilai.o2o.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.Area;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {
	@Autowired
	AreaService areaServie;
	
	@Autowired
	CacheService cacheService;
	
	@Test
	public void test() {
		List<Area> areaList = areaServie.getAreaList();
		System.out.println(areaList);
		//移除Redis中的key
		cacheService.removeFromCache(areaServie.AREALISTKEY);
		areaList = areaServie.getAreaList();
	}

}
