package com.web.basedata;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.web.common.PageForQuery;
import com.web.entity.Commision;
import com.web.entity.ExchangeRate;
import com.web.entity.PaypalFee;
import com.web.entity.User;

@Service
public class BaseDataServiceImpl implements BaseDataService {

	@Resource
	public CommisionDao comDao;
	@Resource
	public ExchangeDao exDao;
	@Resource
	public PaypalDao ppDao;

	@Override
	@Transactional
	public Map<String, Object> getBaseData(int type) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("paypal_fee", null);
		rtnMap.put("paypal_rate", null);

		List<Commision> commision = comDao.getCommision(type);
		Double exchange = exDao.getExchange(type);
		PaypalFee ppFee = ppDao.getPaypal(type);
		if (ppFee != null) {
			if (ppFee.getFee() > 0) {
				rtnMap.put("paypal_fee", ppFee.getFee());
			}
			if (ppFee.getFee_rate() > 0) {
				rtnMap.put("paypal_fee_rate", ppFee.getFee_rate());
			}
		}

		rtnMap.put("commision", commision);
		rtnMap.put("exchange", exchange);

		return rtnMap;
	}

	@Override
	@Transactional
	public long getExchangeCount() throws Exception {

		return exDao.getCount();
	}

	@Override
	@Transactional
	public List<ExchangeRate> getExchanges(PageForQuery pageForQuery) throws Exception {
		int startIdx = (pageForQuery.getPage() - 1) * pageForQuery.getSize();
		return exDao.getExchange(startIdx, pageForQuery.getSize());
	}

	@Override
	@Transactional
	public long getCommisionCount() throws Exception {
		return comDao.getCount();
	}

	@Override
	@Transactional
	public List<Commision> getCommisions(PageForQuery pageForQuery) throws Exception {
		int startIdx = (pageForQuery.getPage() - 1) * pageForQuery.getSize();
		return comDao.getCommision(startIdx, pageForQuery.getSize());
	}

	@Override
	@Transactional
	public long getPaypalCount() throws Exception {
		return ppDao.getCount();
	}

	@Override
	@Transactional
	public List<PaypalFee> getPaypals(PageForQuery pageForQuery) throws Exception {
		int startIdx = (pageForQuery.getPage() - 1) * pageForQuery.getSize();
		return ppDao.getPaypal(startIdx, pageForQuery.getSize());
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public ExchangeRate addExchange(User user, MultiValueMap<String, Object> reqParams) throws Exception {
		Map<String, List<Object>> params = reqParams;

		//
		int type;
		Double rate;

		if (params.containsKey("type") && params.get("type").size() > 0) {
			try {
				type = Integer.parseInt(params.get("type").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的汇率类型！");
			}
		} else {
			throw new Exception("请选择正确的汇率类型！");
		}

		if (params.containsKey("rate") && params.get("rate").size() > 0
				&& !params.get("rate").get(0).toString().trim().equals("")) {
			try {
				rate = Double.parseDouble(params.get("rate").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请输入正确的汇率！");
			}
		} else {
			throw new Exception("汇率输入不正确！");
		}

		ExchangeRate exchange = new ExchangeRate();
		exchange.setType(type);
		exchange.setRate(rate);
		exchange.setDate(Calendar.getInstance());
		exchange.setUser(user);
		
		exDao.addExchange(exchange);

		return exchange;

	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Commision addCommision(User user, MultiValueMap<String, Object> reqParams) throws Exception {
		
		Map<String, List<Object>> params = reqParams;
		int srv_type;
		Double fee;

		if (params.containsKey("srv_type") && params.get("srv_type").size() > 0) {
			try {
				srv_type = Integer.parseInt(params.get("srv_type").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的佣金类型！");
			}
		} else {
			throw new Exception("请选择正确的佣金类型！");
		}

		if (params.containsKey("fee") && params.get("fee").size() > 0
				&& !params.get("fee").get(0).toString().trim().equals("")) {
			try {
				fee = Double.parseDouble(params.get("fee").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请输入正确的佣金金额！");
			}
		} else {
			throw new Exception("佣金金额输入不正确！");
		}

		Commision comm = new Commision();
		comm.setUser(user);
		comm.setDate(Calendar.getInstance());
		comm.setFee(fee);
		comm.setSrv_type(srv_type);
		comm.setType(1);
		
		comDao.addCommision(comm);
		return comm;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public PaypalFee addPaypal(User user, MultiValueMap<String, Object> reqParams) throws Exception {
		
		Map<String, List<Object>> params = reqParams;
		Double fee, fee_rate;

		if (params.containsKey("fee") && params.get("fee").size() > 0
				&& !params.get("fee").get(0).toString().trim().equals("")) {
			try {
				fee = Double.parseDouble(params.get("fee").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请输入正确的费用金额！");
			}
		} else {
			throw new Exception("费用金额输入不正确！");
		}
		
		if (params.containsKey("fee_rate") && params.get("fee_rate").size() > 0
				&& !params.get("fee_rate").get(0).toString().trim().equals("")) {
			try {
				fee_rate = Double.parseDouble(params.get("fee_rate").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请输入正确的费率！");
			}
			
			if (fee_rate > 100 || fee_rate < 0) {
				throw new Exception("请输入正确的费率！");
			}
		} else {
			throw new Exception("费率输入不正确！");
		}
		
		PaypalFee paypal = new PaypalFee();
		paypal.setDate(Calendar.getInstance());
		paypal.setUser(user);
		paypal.setFee(fee);
		paypal.setFee_rate(fee_rate);
		paypal.setType(1);
		
		ppDao.addPaypal(paypal);
		return paypal;
	}

}
