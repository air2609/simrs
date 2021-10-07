package com.vone.medisafe.service;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import com.vone.medisafe.service.iface.purchasing.DOManager;
import com.vone.medisafe.service.iface.purchasing.POManager;
import com.vone.medisafe.service.iface.purchasing.PORManager;
import com.vone.medisafe.service.iface.purchasing.ReturManager;

public class PurchaseServiceLocator {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static PlatformTransactionManager getTrxManager(){
		return (PlatformTransactionManager)ctx.getBean("txManager", PlatformTransactionManager.class);
	}
	
	public static SessionFactory getSessionFactory(){
		return (SessionFactory) ctx.getBean("SessionFactory",SessionFactory.class);
	}
	
	public static PORManager getPORManager(){
		return (PORManager)ctx.getBean("PORManager",PORManager.class);
	}
		
	public static POManager getPOManager(){
		return (POManager)ctx.getBean("POManager", POManager.class);
	}
	
	public static DOManager getDOManager(){
		return (DOManager)ctx.getBean("DOManager", DOManager.class);
	}
	
	public static ReturManager getReturManager(){
		return (ReturManager)ctx.getBean("ReturManager", ReturManager.class);
	}

}
