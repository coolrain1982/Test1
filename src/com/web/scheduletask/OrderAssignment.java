package com.web.scheduletask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("OrderAssignment")
public class OrderAssignment {
	
	public static Logger log = LogManager.getRootLogger();
	
	@Scheduled(cron="0 0 0,3,6,10,20,22 * * ?")
	public void assignOrder() {
		
		log.info(String.format("%s---OrderAssignment start!", 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
	}
}
