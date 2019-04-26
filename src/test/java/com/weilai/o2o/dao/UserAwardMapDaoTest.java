package com.weilai.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.Award;
import com.weilai.o2o.entity.PersonInfo;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.UserAwardMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAwardMapDaoTest {
	@Autowired
	UserAwardMapDao userAwardMapDao;
	
	@Test
	public void insert() {
		UserAwardMap userAwardMap = new UserAwardMap();
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(0);
		userAwardMap.setPoint(1);
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		
		PersonInfo operator = new PersonInfo();
		operator.setUserId(2L);
		
		Shop shop = new Shop();
		shop.setShopId(35L);
		
		Award award = new Award();
		award.setAwardId(1L);
		
		userAwardMap.setUser(user);
		userAwardMap.setAward(award);
		userAwardMap.setOperator(operator);
		userAwardMap.setShop(shop);
		userAwardMapDao.insertUserAwardMap(userAwardMap);
		
	}
	
	@Test
	public void twstQueryList() {
		UserAwardMap userAwardCondition = new UserAwardMap();
//		Shop shop = new Shop();
//		shop.setShopId(35L);
//		userAwardCondition.setShop(shop);
		
		//PersonInfo user = new PersonInfo();
		//user.setUserId(1L);
		//user.setName("测");
		//userAwardCondition.setUser(user);
		
		Award award = new Award();
		//award.setAwardName("第");
		award.setEnableStatus(0);
		userAwardCondition.setAward(award);
		
		List<UserAwardMap> userAwardMaps = userAwardMapDao.queryAwardList(userAwardCondition, 0, 3);
		System.out.println(userAwardMaps.size());
	}
	
	@Test
	public void testCount() {
		UserAwardMap userAwardCondition = new UserAwardMap();
		Award award = new Award();
		//award.setAwardName("第");
		award.setEnableStatus(0);
		userAwardCondition.setAward(award);
		
		int effected = userAwardMapDao.queryCount(userAwardCondition);
		assertEquals(1, effected);
	}
	
	@Test
	public void testByid() {
		UserAwardMap userAwardMap = new UserAwardMap();
		userAwardMap.setUserAwardId(2L);
		userAwardMapDao.queryUserAwardMapByid(2L);
	}
	
	@Test
	public void update() {
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		userAwardMap.setUserAwardId(5L);
		userAwardMap.setUsedStatus(1);
		userAwardMap.setUser(user);
		int awardMap = userAwardMapDao.updateUserAwardMap(userAwardMap);
		assertEquals(1, awardMap);
	}
	
	
	
}
