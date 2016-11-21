package com.web.order.ctrl;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Order;
import com.web.entity.User;
import com.web.user.UserService;

@Controller
@RequestMapping("userorder")
public class UserOrderCtrl {
	
	@Resource
	public UserService userService;
    
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
		
		//取用户信息并设置费率折扣
		User user = null;
		int feeDiscount = 100;
		
		//取当前汇率
		double exchangeRate = 0.00d;
		
		//取当前佣金
		double commision = 4.00d;
		
		//
		String link = "";
		if (params.containsKey("link") && !params.get("link").toString().trim().equals("")) {
			link = params.get("link").toString().trim();
		} else {
			return new OrderCtrlResp(0, "产品链接输入不正确！", 0);
		}
		
		String productPhotoUrl = "";
		if (files!=null && files.length > 0) {
			try {
				productPhotoUrl = saveUploadImg(files, user);
			} catch (Exception e) {
				return new OrderCtrlResp(0, "保存产品截图失败：" + e.getMessage(), 0);
			}
		}
		
		String productDescript = "";
		if (params.containsKey("descript") && !params.get("descript").toString().trim().equals("")) {
			productDescript = params.get("descript").toString().trim();
		} else {
			return new OrderCtrlResp(0, "产品描述输入不正确！", 0);
		}
		
		int quantity = 0;
		if (params.containsKey("quantity")) {
			try {
				quantity = Integer.parseInt(params.get("quantity").toString().trim());
			} catch (Exception e) {
				return new OrderCtrlResp(0, "产品数量输入不正确：" + e.getMessage(), 0);
			}
		} else {
			return new OrderCtrlResp(0, "产品数量输入不正确！", 0);
		}
		
		double unitFreight = 0.00;
		if (params.containsKey("unit_freight")) {
			try {
				unitFreight = Double.parseDouble(params.get("unit_freight").toString().trim());
			} catch (Exception e) {
				return new OrderCtrlResp(0, "运费单价输入不正确：" + e.getMessage(), 0);
			}
		} else {
			return new OrderCtrlResp(0, "运费单价输入不正确！", 0);
		}
		
		double unitPrice = 0.00;
		if (params.containsKey("unit_price")) {
			try {
				unitPrice = Double.parseDouble(params.get("unit_price").toString().trim());
			} catch (Exception e) {
				return new OrderCtrlResp(0, "产品单价输入不正确：" + e.getMessage(), 0);
			}
		} else {
			return new OrderCtrlResp(0, "产品单价输入不正确！", 0);
		}
		
		Order newOrder = new Order();
		newOrder.setCreate_date(Calendar.getInstance());
		newOrder.setDiscount(feeDiscount);
		newOrder.setExchange_rate(exchangeRate);
		newOrder.setLink(link);
		newOrder.setProduct_photo_url(productPhotoUrl);
		newOrder.setProduct_descript(productDescript);
		newOrder.setProduct_quantity(quantity);
		newOrder.setProduct_unit_commission(commision);
		newOrder.setProduct_unit_freight(unitFreight);
		newOrder.setProduct_unit_price(unitPrice);
		
		return new OrderCtrlResp(0, "", 0);
	}
	
	/**
	 * 保存上传图片
	 * @param files
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String saveUploadImg(MultipartFile[] files, User user) throws Exception {
		return "";
	}
}