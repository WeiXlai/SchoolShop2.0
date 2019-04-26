package com.weilai.o2o.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.Award;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {
	@Autowired
	AwardDao awardDao;
	
	@Test
	public void testInsert() {
		Award award = new Award();
		award.setAwardName("第三个奖品");
		award.setAwardDesc("第三个");
		award.setCreateTime(new Date());
		award.setPoint(2);
		award.setPriority(5);
		award.setShopId(30L);
		award.setEnableStatus(1);
		int insertAwrad = awardDao.insertAwrad(award);
		assertEquals(1, insertAwrad);
	}
	
	@Test
	public void testSelevtList() {
		Award awardCondition = new Award();
		//awardCondition.setAwardName("第");
		//awardCondition.setEnableStatus(0);
		awardCondition.setShopId(30L);
		List<Award> insertAwrad = awardDao.queryAwardList(awardCondition, 0, 3);
		assertEquals(1, insertAwrad.size());
	}
	
	
	@Test
	public void testCount() {
		Award awardCondition = new Award();
		//awardCondition.setShopId(30L);
		//awardCondition.setAwardName("第");
		awardCondition.setEnableStatus(0);
		int insertAwrad = awardDao.queryCount(awardCondition);
		assertEquals(1, insertAwrad);
	}
	
	@Test
	public void testqueryAwardByid() {
		Award insertAwrad = awardDao.queryAwardByid(1L);
		assertEquals("第一个奖品", insertAwrad.getAwardName());
	}
	
	
	@Test
	public void testupdateAward() {
		Award award = new Award();
		award.setAwardName("第er个奖品");
		award.setAwardDesc("第er个");
//		award.setCreateTime(new Date());
//		award.setPoint(4);
//		award.setPriority(1);
//		award.setShopId(30L);
//		award.setEnableStatus(0);
		award.setAwardId(2L);
		int insertAwrad = awardDao.updateAward(award);
		assertEquals(1, insertAwrad);
	}
	
	@Test
	public void testDelect() {
		awardDao.deleteAward(2L, 34L);
	}
}
