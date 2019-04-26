package com.weilai.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weilai.o2o.entity.Area;
import com.weilai.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	//打印AreaController的日志
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/listarea",method = RequestMethod.GET)
	@ResponseBody   //返回的格式是json
	private Map<String, Object> listArea(){
		logger.info("====start====");
		Long startTime = System.currentTimeMillis();
		
		Map<String, Object>modelMap = new HashMap<String, Object>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			areaList = areaService.getAreaList();
			modelMap.put("rows", areaList);
			modelMap.put("total", areaList.size());
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		logger.error("test error");
		Long endTime = System.currentTimeMillis();
		logger.debug("costime:[{}ms]",endTime-startTime);
		logger.info("====end====");
		return modelMap;
	}

}
