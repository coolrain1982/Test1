package com.web.basedata;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("basedata")
public class BaseDataCtrl {
	
	@Resource
	public BaseDataService service;
	
	@RequestMapping("/neworder")
	@ResponseBody
	public Map<String, Object> getBaseDataForNewOrder(@RequestParam MultiValueMap<String, Object> params) {
		
		return service.getBaseData(1);
	}
}
