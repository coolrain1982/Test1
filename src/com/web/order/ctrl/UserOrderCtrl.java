package com.web.order.ctrl;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.order.service.SaveNewOrderService;

@Controller
@RequestMapping("userorder")
public class UserOrderCtrl {
	
	@Resource
	public SaveNewOrderService saveSrv;
    
	@RequestMapping("/neworder.do")
	@ResponseBody
	public OrderCtrlResp SaveNewOrder(@RequestParam MultiValueMap<String, Object> params, 
			@RequestParam("files")MultipartFile[] files) {
		
		//先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName ="";
		if (UserDetails.class.isInstance(o)) {
			userName = ((UserDetails) o).getUsername();
		} else {
			return new OrderCtrlResp(0, "请先登录系统后再进行订单操作！", 0);
		}
		
		try {
			return new OrderCtrlResp(1, String.valueOf(saveSrv.saveNewOrder(userName, params, files)), 0);
		} catch (Exception e) {
			return new OrderCtrlResp(0, e.getMessage(), 0);
		}
	}
}