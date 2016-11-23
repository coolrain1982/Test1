package com.web.order.service;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.web.basedata.BaseDataService;
import com.web.entity.Order;
import com.web.entity.User;
import com.web.order.dao.OrderDao;
import com.web.user.UserService;

@Service
public class SaveNewOrderImpl implements SaveNewOrderService {

	@Resource
	public UserService userService;
	@Resource
	public BaseDataService baseDataSrv;
	public OrderDao orderDao;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public long saveNewOrder(String userName, MultiValueMap<String, Object> reqParams, MultipartFile[] files)
			throws Exception {
		
		Map<String, List<Object>> params = reqParams;

		// 取用户信息并设置费率折扣
		User user = null;
		int feeDiscount = 100;

		user = userService.getUser(userName);
		if (user == null) {
			throw new Exception("未知用户：" + userName);
		}

		// 取当前汇率、佣金、paypal手续费（固定部分+比例）
		double exchangeRate = 0.00, commision = 0.00;
		int exchangeType = 0;
		double paypalFee = 0.3, paypalRate = 3.9;

		if (params.containsKey("exchange") && params.get("exchange").size() > 0) {
			try {
				exchangeType = Integer.parseInt(params.get("exchange").get(0).toString().trim());
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
		
		if (bsDataMap.containsKey("paypal_fee")) {

			try {
				paypalFee = Double.parseDouble(bsDataMap.get("paypal_fee").toString().trim());
			} catch (Exception e) {
				throw new Exception("系统缺少Paypal支付数据：paypal_fee，请尽快联系客服处理！");
			}
		} else {
			throw new Exception("系统缺少Paypal支付数据：paypal_fee，请尽快联系客服处理！");
		}
		
		if (bsDataMap.containsKey("paypal_fee_rate")) {
			try {
				paypalRate = Double.parseDouble(bsDataMap.get("paypal_fee_rate").toString().trim());
			} catch (Exception e) {
				throw new Exception("系统缺少Paypal支付数据：paypal_fee_rate，请尽快联系客服处理！");
			}
		} else {
			throw new Exception("系统缺少Paypal支付数据：paypal_fee_rate，请尽快联系客服处理！");
		}

		//
		String link = "";
		if (params.containsKey("link") && params.get("link").size() > 0 && 
				!params.get("link").get(0).toString().trim().equals("")) {
			link = params.get("link").get(0).toString().trim();
		} else {
			throw new Exception("产品链接输入不正确！");
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
		
		String productPhotoUrl = "";
		if (files != null && files.length > 0) {
			try {
				productPhotoUrl = saveUploadImg(files, user, System.getProperty("meiyabuy.root"));
			} catch (Exception e) {
				throw new Exception("保存产品截图失败：" + e.getMessage());
			}
		} else {
			throw new Exception("请上传至少一张产品截图");
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
		newOrder.setPaypal_fee(paypalFee);
		newOrder.setPaypal_rate(paypalRate);
		newOrder.setUser(user);
		
		orderDao.newOrder(newOrder);
		
		throw new Exception("保存订单失败！"); 

//		return newOrder.getOrder_id();
	}

	/**
	 * 保存上传图片
	 * 
	 * @param files
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String saveUploadImg(MultipartFile[] files, User user, String webroot) throws Exception {
		
		Calendar c = Calendar.getInstance();
		String fileSavePath = String.format("userupload%s%s%s%s%s%s%s",File.separatorChar, user.getName(), 
				File.separatorChar, c.get(Calendar.YEAR), File.separatorChar, c.get(Calendar.MONTH) + 1, File.separatorChar) ;
		
		StringBuffer sb = new StringBuffer();
		
		File baseDir = new File(String.format("%s%s%s", webroot, File.separatorChar, fileSavePath));
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		for(int i = 0; i < files.length && i < 3; i ++) {
			MultipartFile mfile = files[i];
			String origName = mfile.getOriginalFilename();
			long mill = Calendar.getInstance().getTimeInMillis(); 

			File imgFile = new File(String.format("%s%s%s_%s", baseDir, File.separatorChar, mill, origName));
			
			try {
				mfile.transferTo(imgFile);
				sb.append(String.format("@@%s%s_%s", fileSavePath, mill, origName));
			} catch (Exception e) {
				throw new Exception(String.format("file[%s], error[%s]", origName, e.getMessage()));
			}
		}
		
		return "";
	}

}