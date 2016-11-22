package com.web.order.service;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.web.basedata.BaseDataService;
import com.web.entity.Order;
import com.web.entity.User;
import com.web.user.UserService;

@Service
public class SaveNewOrderImpl implements SaveNewOrderService {

	@Resource
	public UserService userService;
	@Resource
	public BaseDataService baseDataSrv;

	@Override
	public long saveNewOrder(String userName, MultiValueMap<String, Object> params, MultipartFile[] files)
			throws Exception {

		// 取用户信息并设置费率折扣
		User user = null;
		int feeDiscount = 100;

		user = userService.getUser(userName);
		if (user == null) {
			throw new Exception("未知用户：" + userName);
		}

		// 取当前汇率和佣金
		double exchangeRate = 0.00d;
		double commision = 0.00d;
		int exchangeType = 0;

		if (params.containsKey("exchange") && !params.get("exchange").toString().trim().equals("")) {
			try {
				exchangeType = Integer.parseInt(params.get("exchange").toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的汇率！");
			}
		} else {
			throw new Exception("请选择正确的汇率！");
		}

		Map<String, Object> bsDataMap = baseDataSrv.getBaseData(exchangeType);
		if (bsDataMap.containsKey("exchange")) {

			try {
				exchangeRate = Double.parseDouble(bsDataMap.get("exchange").toString().trim());
			} catch (Exception e) {
				throw new Exception("系统缺少汇率数据，请尽快联系客户处理！");
			}
		} else {
			throw new Exception("系统缺少汇率数据，请尽快联系客户处理！");
		}

		if (bsDataMap.containsKey("commision")) {

			try {
				commision = Double.parseDouble(bsDataMap.get("commision").toString().trim());
			} catch (Exception e) {
				throw new Exception("系统缺少佣金数据，请尽快联系客服处理！");
			}
		} else {
			throw new Exception("系统缺少佣金数据，请尽快联系客服处理！");
		}

		//
		String link = "";
		if (params.containsKey("link") && !params.get("link").toString().trim().equals("")) {
			link = params.get("link").toString().trim();
		} else {
			throw new Exception("产品链接输入不正确！");
		}

		String productPhotoUrl = "";
		if (files != null && files.length > 0) {
			try {
				productPhotoUrl = saveUploadImg(files, user);
			} catch (Exception e) {
				throw new Exception("保存产品截图失败：" + e.getMessage());
			}
		}

		String productDescript = "";
		if (params.containsKey("descript") && !params.get("descript").toString().trim().equals("")) {
			productDescript = params.get("descript").toString().trim();
		} else {
			throw new Exception("产品描述输入不正确！");
		}

		int quantity = 0;
		if (params.containsKey("quantity")) {
			try {
				quantity = Integer.parseInt(params.get("quantity").toString().trim());
			} catch (Exception e) {
				throw new Exception("产品数量输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("产品数量输入不正确！");
		}

		double unitFreight = 0.00;
		if (params.containsKey("unit_freight")) {
			try {
				unitFreight = Double.parseDouble(params.get("unit_freight").toString().trim());
			} catch (Exception e) {
				throw new Exception("运费单价输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("运费单价输入不正确！");
		}

		double unitPrice = 0.00;
		if (params.containsKey("unit_price")) {
			try {
				unitPrice = Double.parseDouble(params.get("unit_price").toString().trim());
			} catch (Exception e) {
				throw new Exception("产品单价输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("产品单价输入不正确！");
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

		return 0;
	}

	/**
	 * 保存上传图片
	 * 
	 * @param files
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String saveUploadImg(MultipartFile[] files, User user) throws Exception {
		return "";
	}

}
