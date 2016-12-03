package com.web.common;

public class CustomMySQLDialect extends org.hibernate.dialect.MySQL57InnoDBDialect {
	
	 public String getTableTypeString() {  
	        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";  
	    }  
}