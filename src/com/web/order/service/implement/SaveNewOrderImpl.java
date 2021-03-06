package com.web.order.service.implement;

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

import com.scheduletask.OrderSync;
import com.web.basedata.dao.CommisionDao;
import com.web.basedata.service.BaseDataService;
import com.web.entity.Commision;
import com.web.entity.Order;
import com.web.entity.User;
import com.web.order.dao.OrderDao;
import com.web.order.service.SaveNewOrderService;
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
	
	@Resource
	public CommisionDao commDao;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Order saveNewOrder(String userName, MultiValueMap<String, Object> reqParams, MultipartFile[] files)
			throws Exception {
		
		Map<String, List<Object>> params = reqParams;

		// 取用户信息并设置费率折扣
		User user = null;

		user = userService.getUser(userName);
		if (user == null) {
			throw new Exception("未知用户：" + userName);
		}
		
		int csid = -1;
		List<User> csUsers = userService.getCS();
		if (csUsers.size() > 0) {
			csid = csUsers.get((int) (Math.random()*csUsers.size() + 1) -1).getId();
		}

		// 取当前汇率、佣金、paypal手续费（固定部分+比例）
		double exchangeRate = 0.00;
		int exchangeType = 0, srvtype = 1, srvmode = 1;
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
		
		if (params.containsKey("srvmode") && params.get("srvmode").size() > 0) {
			try {
				srvmode = Integer.parseInt(params.get("srvmode").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的商品查找模式！");
			}
		} else {
			throw new Exception("请选择正确的商品查找模式！");
		}
		
		//取佣金数据
		Commision useComm = null;
		List<Commision> comList = commDao.getCommision(exchangeType);
		if (comList == null || comList.size() == 0) {
			throw new Exception("系统缺少佣金数据，请尽快联系客服处理！");
		}
			
		for (Commision com : comList) {
			if (com.getSrv_mode() == srvmode) {
				useComm = com;
				break;
			}
		}
		
		if (useComm == null) {
			throw new Exception(String.format("系统缺少佣金数据[%s]，请尽快联系客服处理！", srvtype));
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
		String link = "",keyword="", shopname="";
		int productPageIdx = 0;
		
		if (srvmode == 1) {  //链接模式
			if (params.containsKey("link") && params.get("link").size() > 0 && 
					!params.get("link").get(0).toString().trim().equals("")) {
				link = params.get("link").get(0).toString().trim();
				if (link.length() > 2000) {
					throw new Exception("商品链接不能超过2000个字符！");
				}
			} else {
				throw new Exception("商品链接输入不正确！");
			}
		} else if (srvmode == 2) {  //搜索模式
			if (params.containsKey("keyword") && params.get("keyword").size() > 0 && 
					!params.get("keyword").get(0).toString().trim().equals("")) {
				keyword = params.get("keyword").get(0).toString().trim();
				
				if (keyword.length() > 30) {
					throw new Exception("商品关键词不能超过30个字符！");
				}
			} else {
				throw new Exception("商品关键词不正确！");
			}
			
			if (params.containsKey("shopname") && params.get("shopname").size() > 0 && 
					!params.get("shopname").get(0).toString().trim().equals("")) {
				shopname = params.get("shopname").get(0).toString().trim();
				
				if (shopname.length() > 50) {
					throw new Exception("店铺名称不能超过50个字符！");
				}
			} else {
				throw new Exception("店铺名称不正确！");
			}
			
			if (params.containsKey("pageidx")) {
				try {
					productPageIdx = Integer.parseInt(params.get("pageidx").get(0).toString().trim());
				} catch (Exception e) {
					throw new Exception("商品所在页输入不正确：" + e.getMessage());
				}
				
				if (productPageIdx > 99) {
					throw new Exception("商品所在页不能大于99");
				}
				
			} else {
				throw new Exception("商品所在页输入不正确！");
			}
		}
		
		String product_asin = "";
		if (params.containsKey("asin") && params.get("asin").size() > 0 && 
				!params.get("asin").get(0).toString().trim().equals("")) {
			product_asin = params.get("asin").get(0).toString().trim();
			if (product_asin.length() > 30) {
				throw new Exception("商品ASIN不能超过30个字符！");
			}
		} else {
			throw new Exception("商品ASIN输入不正确！");
		}

		String productDescript = "";
		if (params.containsKey("descript") && params.get("descript").size() > 0 &&
				!params.get("descript").get(0).toString().trim().equals("")) {
			productDescript = params.get("descript").get(0).toString().trim();
			if (productDescript.length() > 140) {
				throw new Exception("商品描述不能超过140个字符！");
			}
		} else {
			throw new Exception("商品描述输入不正确！");
		}

		int quantity = 0;
		if (params.containsKey("quantity")) {
			try {
				quantity = Integer.parseInt(params.get("quantity").get(0).toString().trim());
				if (quantity > 999) {
					throw new Exception("商品数量不能大于999个！");
				}
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
				if (unitFreight > 999) {
					throw new Exception("运费单价不能超过999！");
				}
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
				if (unitPrice > 9999) {
					throw new Exception("商品单价不能超过9999！");
				}
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
		newOrder.setExchange_rate(exchangeRate);
		newOrder.setLink(link);
		newOrder.setProduct_photo_url(productPhotoUrl);
		newOrder.setProduct_descript(productDescript);
		newOrder.setProduct_quantity(quantity);
		newOrder.setComm(useComm);
		newOrder.setProduct_unit_freight(unitFreight);
		newOrder.setProduct_unit_price(unitPrice);
		newOrder.setPaypal_fee(paypalFee);
		newOrder.setPaypal_rate(paypalRate);
		newOrder.setUser(user);
		newOrder.setDiscount(user.getDiscount()==null || user.getDiscount()<= 0?100:user.getDiscount());
		newOrder.setStatus(1);
		newOrder.setType(srvtype);
		newOrder.setCsid(csid);
		newOrder.setProduct_asin(product_asin);
		newOrder.setFind_product_mode(srvmode);
		newOrder.setKey_word(keyword);
		newOrder.setShop_name(shopname);
		newOrder.setSearch_page_idx(productPageIdx);
		
		orderDao.newOrder(newOrder);
		
		Order rtnOrder = new Order();
		rtnOrder.setOrder_id(newOrder.getOrder_id());
		rtnOrder.setCreate_date(newOrder.getCreate_date());
		
		//在日志中记录一下成功的新订单
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("[orderid=%s]", newOrder.getOrder_id()));
		sb.append(String.format("[discount=%s]", newOrder.getDiscount()));
		sb.append(String.format("[exchange=%s]", exchangeRate));
		sb.append(String.format("[link=%s]", link));
		sb.append(String.format("[productPhotoUrl=%s]", productPhotoUrl));
		sb.append(String.format("[productDescript=%s]", productDescript));
		sb.append(String.format("[quantity=%s]", quantity));
		sb.append(String.format("[commision=%s]", useComm.getId()));
		sb.append(String.format("[unitFreight=%s]", unitFreight));
		sb.append(String.format("[unitPrice=%s]", unitPrice));
		sb.append(String.format("[paypalFee=%s]", paypalFee));
		sb.append(String.format("[paypalRate=%s]", paypalRate));
		sb.append(String.format("[user=%s]", newOrder.getUser().getName()));
		sb.append(String.format("[srvtype=%s]", srvtype));
		sb.append(String.format("[csid=%s]", csid));
		sb.append(String.format("[product_asin=%s]", product_asin));
		sb.append(String.format("[srvmode=%s]", srvmode));
		sb.append(String.format("[keyword=%s]", keyword));
		sb.append(String.format("[shopname=%s]", shopname));
		sb.append(String.format("[productPageIdx=%s]", productPageIdx));
		
		OrderSync.log.info(String.format("[new order success]%s", sb.toString()));

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
