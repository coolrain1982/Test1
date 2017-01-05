package com.scheduletask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller("OrderSync")
public class OrderSync {
	
	public static Logger log = LogManager.getRootLogger();
	private static Object ORDER_SYNC_LOCK = new Object();
	
	@Scheduled(cron="0 10 0,3,6,10,20,22 * * ?")
	public void assignOrder() {
		
		synchronized (ORDER_SYNC_LOCK) {
			log.info(String.format("%s---OrderAssignment start!", 
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
			
			//从tbl_order中取出等待分配的订单记录
			
		}	
	}
}
