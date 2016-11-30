package com.web.order.service;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.web.basedata.BaseDataService;
import com.web.entity.Commision;
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
	@Resource
	public OrderDao orderDao;
	@Value("${upload.base}")
	private String uploadBase;


	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public Order saveNewOrder(String userName, MultiValueMap<String, Object> reqParams, MultipartFile[] files)
			throws Exception {
		
		Map<String, List<Object>> params = reqParams;

		// 取用户信息并设置费率折扣
		User user = null;
		int feeDiscount = 100;

		user = userService.getUser(userName);
		if (user == null) {
			throw new Exception("未知用户：" + userName);
		}
		
		int csid = -1;
		List<User> csUsers = userService.getCS();
		if (csUsers.size() > 0) {
			csid = csUsers.get((int) (Math.random()*csUsers.size() + 1)).getId();
		}

		// 取当前汇率、佣金、paypal手续费（固定部分+比例）
		double exchangeRate = 0.00, commision = 0.00;
		int exchangeType = 0, srvtype = 1;
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

		
		if (params.containsKey("srvtype") && params.get("srvtype").size() > 0) {
			try {
				srvtype = Integer.parseInt(params.get("srvtype").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的服务类型！");
			}
		} else {
			throw new Exception("请选择正确的服务类型！");
		}
		
		if (bsDataMap.containsKey("commision")) {
			List<Commision> comList = null;
			boolean comExist = false;
			try {
				Object o = bsDataMap.get("commision");
				comList = (List<Commision>) o;
			} catch (Exception e) {
				throw new Exception("系统缺少佣金数据，请尽快联系客服处理！");
			}
			
			for (Commision com : comList) {
				if (com.getSrv_type() == srvtype) {
					commision = com.getFee();
					comExist = true;
				}
			}
			
			if (!comExist) {
				throw new Exception(String.format("系统缺少佣金数据[%s]，请尽快联系客服处理！", srvtype));
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
			throw new Exception("商品链接输入不正确！");
		}

		String productDescript = "";
		if (params.containsKey("descript") && params.get("descript").size() > 0 &&
				!params.get("descript").get(0).toString().trim().equals("")) {
			productDescript = params.get("descript").get(0).toString().trim();
		} else {
			throw new Exception("商品描述输入不正确！");
		}

		int quantity = 0;
		if (params.containsKey("quantity")) {
			try {
				quantity = Integer.parseInt(params.get("quantity").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("商品数量输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("商品数量输入不正确！");
		}

		double unitFreight = 0.00;
		if (params.containsKey("unit_freight")) {
			try {
				unitFreight = Double.parseDouble(params.get("unit_freight").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("运费单价输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("运费单价输入不正确！");
		}

		double unitPrice = 0.00;
		if (params.containsKey("unit_price")) {
			try {
				unitPrice = Double.parseDouble(params.get("unit_price").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("商品单价输入不正确：" + e.getMessage());
			}
		} else {
			throw new Exception("商品单价输入不正确！");
		}
		
		String productPhotoUrl = "";
		if (files != null && files.length > 0) {
			try {
				productPhotoUrl = saveUploadImg(files, user);
			} catch (Exception e) {
				throw new Exception("保存商品截图失败：" + e.getMessage());
			}
		} else {
			throw new Exception("请上传至少一张商品截图");
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
		newOrder.setDiscount(user.getDiscount()==null || user.getDiscount()<= 0?100:user.getDiscount());
		newOrder.setStatus(1);
		newOrder.setType(srvtype);
		newOrder.setCsid(csid);
		
		orderDao.newOrder(newOrder);
		
		Order rtnOrder = new Order();
		rtnOrder.setOrder_id(newOrder.getOrder_id());
		rtnOrder.setCreate_date(newOrder.getCreate_date());

		return rtnOrder;
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
		
		Calendar c = Calendar.getInstance();
		String fileSavePath = String.format("%s%s%s%s%s%s", user.getName(), 
				File.separatorChar, c.get(Calendar.YEAR), File.separatorChar, c.get(Calendar.MONTH) + 1, File.separatorChar) ;
		
		StringBuffer sb = new StringBuffer();
		
		File baseDir = new File(String.format("%s%s%s", uploadBase, File.separatorChar, fileSavePath));
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		for(int i = 0; i < files.length && i < 3; i ++) {
			MultipartFile mfile = files[i];
			
			if (mfile.getSize() > 300 * 1024) {
				throw new Exception(String.format("上传图片大小不能超过300k！"));
			}
			
			String origName = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
			long mill = Calendar.getInstance().getTimeInMillis(); 

			File imgFile = new File(String.format("%s%s%s_%s%s", baseDir, File.separatorChar, mill, i, origName));
			
			try {
				mfile.transferTo(imgFile);
				sb.append(String.format("@@%s%s_%s%s", fileSavePath, mill,i, origName));
			} catch (Exception e) {
				throw new Exception(String.format("file[%s], error[%s]", origName, e.getMessage()));
			}
		}
		
		return sb.toString();
	}
}
