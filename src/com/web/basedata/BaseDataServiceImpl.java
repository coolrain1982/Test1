package com.web.basedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.Commision;
import com.web.entity.PaypalFee;

@Service
public class BaseDataServiceImpl  implements BaseDataService {

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
			if (ppFee.getFee_rate()>0) {
				rtnMap.put("paypal_fee_rate", ppFee.getFee_rate());
			}
		}
		
		rtnMap.put("commision", commision);
		rtnMap.put("exchange", exchange);
		
		return rtnMap;
	}

}
