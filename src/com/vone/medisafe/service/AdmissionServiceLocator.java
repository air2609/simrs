package com.vone.medisafe.service;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import com.vone.medisafe.service.iface.admission.BedOccupancyManager;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.service.iface.admission.MutasiKamarManager;
import com.vone.medisafe.service.iface.admission.RajalManager;
import com.vone.medisafe.service.iface.admission.RanapManager;

public class AdmissionServiceLocator {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static SessionFactory getSessionFactory(){
		return (SessionFactory) ctx.getBean("SessionFactory",SessionFactory.class);
	}
	
	public static PlatformTransactionManager getTrxManager(){
		return (PlatformTransactionManager)ctx.getBean("txManager", PlatformTransactionManager.class);
	}

	
	public static MedicalRecordManager getMedicalRecordManager(){
		return (MedicalRecordManager)ctx.getBean("MedicalRecordManager",MedicalRecordManager.class);
	}
	
	public static RanapManager getRegistrationManager(){
		return (RanapManager)ctx.getBean("RegistrationManager",RanapManager.class);
	}
	
	
	public static BedOccupancyManager getBedOccupanyManager(){
		return (BedOccupancyManager)ctx.getBean("BedOccupanyManager",BedOccupancyManager.class);
	}
	
	
	public static RajalManager getRajalManager(){
		
		return (RajalManager) ctx.getBean("RajalManager",RajalManager.class);
		
	}
	
	
	public static MutasiKamarManager getMutasiKamarManager(){
		
		return (MutasiKamarManager) ctx.getBean("MutasiKamarManager", MutasiKamarManager.class);
	}
}
