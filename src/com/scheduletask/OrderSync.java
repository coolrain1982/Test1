package com.scheduletask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.web.entity.Order;
import com.web.order.service.AssignOrderService;
import com.web.order.service.FinishOrderService;

@Controller("OrderSync")
public class OrderSync {
	
	public static Logger log = LogManager.getRootLogger();
	private static Object ORDER_SYNC_LOCK = new Object();
	private static Calendar LAST_ACTION_CALENDAR = null;
	
	@Resource
	public AssignOrderService assOrderSrv;
	
	@Resource
	public FinishOrderService finishOrderSrv;
	
	@Scheduled(cron="0 10 0,3,6,9,12,15,18,21 * * ?")
	//@Scheduled(cron="0 10,36 0,3,6,9,12,15,18,21,22 * * ?")
	public void orderTask() {
		
		synchronized (ORDER_SYNC_LOCK) {
			
			Calendar now = Calendar.getInstance();
			
			if (LAST_ACTION_CALENDAR != null) {
				long deltTimeInMillis = now.getTimeInMillis() - LAST_ACTION_CALENDAR.getTimeInMillis() ;
				if ((double)(deltTimeInMillis/1000)/3600 < 2) {
					log.info(String.format("%s---OrderTask calcel!Two task interval is too short!", 
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
					return;
				}
			}
			
			LAST_ACTION_CALENDAR = now;
			
			log.info(String.format("%s---OrderTask start!", 
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
			
			//从tbl_order中取出等待分配的订单记录
			assignOrder();
			
			//将符合条件的订单状态设置为“完成”
			finishOrder();
			
			log.info(String.format("%s---OrderTask finish!", 
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
		}	
	}
	
	private void assignOrder() {
		List<Order> waitAssignOrders = null;
		try {
			waitAssignOrders = assOrderSrv.getWaitAssignOrders();
		} catch (Exception e) {
			log.error("Get assignment orders info failed:" + e.getMessage());
			return;
		}
		
		log.info(String.format("%s orders to be assignmented!", waitAssignOrders.size()));
		
		for(Order order:waitAssignOrders) {
			try {
				assOrderSrv.assignOrder(order);
			} catch(Exception e) {
				log.error(String.format("Assignment order[id=%s] failed:%s" , order.getOrder_id()));
			}
		}
	}
	
	private void finishOrder() {
		List<Order> canFinishOrder = null;
		try {
			canFinishOrder = finishOrderSrv.getFinishOrder();
		} catch (Exception e) {
			log.error("Get finish order list failed:" + e.getMessage());
			return;
		}
		
		log.info(String.format("%s orders can finished!", canFinishOrder.size()));
		
		for(Order order :canFinishOrder) {
			try {
				finishOrderSrv.setOrderFinish(order);
			} catch(Exception e) {
				log.error(String.format("Finish order[id=%s] failed:%s" , order.getOrder_id()));
			}
		}	
	}
}
