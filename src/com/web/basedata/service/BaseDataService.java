package com.web.basedata.service;

import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.web.common.PageForQuery;
import com.web.entity.Commision;
import com.web.entity.ExchangeRate;
import com.web.entity.PaypalFee;
import com.web.entity.User;


public interface BaseDataService {
	
	public Map<String, Object> getBaseData(int type);

	public long getExchangeCount() throws Exception;

	public List<ExchangeRate> getExchanges(PageForQuery pageForQuery) throws Exception;

	public long getCommisionCount() throws Exception;

	public List<Commision> getCommisions(PageForQuery pageForQuery) throws Exception;

	public long getPaypalCount() throws Exception;

	public List<PaypalFee> getPaypals(PageForQuery pageForQuery) throws Exception;
	
	public ExchangeRate addExchange(User user, MultiValueMap<String, Object> reqParams) throws Exception;
	
	public Commision addCommision(User user, MultiValueMap<String, Object> reqParams) throws Exception;
	
	public PaypalFee addPaypal(User user, MultiValueMap<String, Object> reqParams) throws Exception;
}
