package com.vone.medisafe.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vone.medisafe.service.iface.master.treatment.TreatmentManager;
import com.vone.medisafe.service.iface.master.treatment.TreatmentRoomManager;

public class TreatmentService {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static TreatmentManager getTreatmentManager(){
		return (TreatmentManager)ctx.getBean("TreatmentManager",TreatmentManager.class);
	}
	
	public static TreatmentRoomManager getTreatmentRoomManager(){
		return (TreatmentRoomManager) ctx.getBean("TreatmentRoomManager",TreatmentRoomManager.class);
	}

}
