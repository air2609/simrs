package com.vone.medisafe.common.util;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IdsServiceLocator {
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static SessionFactory getSessionFactory(){
		return (SessionFactory) ctx.getBean("SessionFactory",SessionFactory.class);
	}
	public static IdsManager getIdsManager(){
		return (IdsManager)ctx.getBean("IdsManager",IdsManager.class);
	}
}
