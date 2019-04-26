package com.weilai.o2o.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.weilai.o2o.dao.HeadLineDao;
import com.weilai.o2o.entity.HeadLine;
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineServiceTest{
	@Autowired
	HeadLineService headLineServiceo;
	
	@Test
	public void testQueryHeadLine() {
		List<HeadLine> headLineList = headLineServiceo.getHeadLineList(new HeadLine());
		System.out.println(headLineList.size());
	}
	
}
