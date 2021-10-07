package com.vone.medisafe.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vone.medisafe.service.iface.master.diagnose.CodManager;
import com.vone.medisafe.service.iface.master.diagnose.Icd9Manager;
import com.vone.medisafe.service.iface.master.diagnose.IcdManager;



public class DiagnoseService {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static IcdManager getIcdManager(){
		return (IcdManager)ctx.getBean("IcdManager",IcdManager.class);
	}
	
	public static Icd9Manager getIcd9Manager(){
		return (Icd9Manager) ctx.getBean("Icd9Manager",Icd9Manager.class);
	}
	
	public static CodManager getCodManager(){
		return (CodManager) ctx.getBean("CodManager",CodManager.class);
	}
}
