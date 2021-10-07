package com.vone.medisafe.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vone.medisafe.service.iface.common.NavigationManager;

public class NavigationServiceLocator {
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static NavigationManager getNavigationManager(){
		return (NavigationManager)ctx.getBean("NavigationManager",NavigationManager.class);
	}
	
}
