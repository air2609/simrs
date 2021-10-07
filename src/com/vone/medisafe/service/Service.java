package com.vone.medisafe.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate.support.OpenSessionInViewFilter;
import org.springframework.transaction.PlatformTransactionManager;

import com.vone.medisafe.antrian.bpjs.AntrianManager;
import com.vone.medisafe.apotik.ApotikManager;
import com.vone.medisafe.cashier.CashierManager;
import com.vone.medisafe.emergency.EmergencyManager;
import com.vone.medisafe.laborat.LaboratManager;
import com.vone.medisafe.radiology.RadiologyManager;
import com.vone.medisafe.service.iface.CommonPatientHistoryManager;
import com.vone.medisafe.service.iface.DiagnoseManager;
import com.vone.medisafe.service.iface.ItemInventoryManager;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.service.iface.admission.AntrianKamarManager;
import com.vone.medisafe.service.iface.master.CreditCardTypeManager;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.service.iface.polyclinic.PolyclinicManager;
import com.vone.medisafe.vk.VkManager;
import com.vone.medisafe.ward.BedTransactionManager;
import com.vone.medisafe.ward.PatientInventoryManager;
import com.vone.medisafe.ward.WardTransactionManager;

public class Service {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static ItemInventoryManager getItemInventoryManager(){
		return (ItemInventoryManager)ctx.getBean("ItemInventoryManager",ItemInventoryManager.class);
	}
	
	public static PolyclinicManager getPolyclinicManager(){
		return (PolyclinicManager)ctx.getBean("PolyclinicManager",PolyclinicManager.class);
	}
	
	public static CommonPatientHistoryManager getPatientHistory(){
		
		return (CommonPatientHistoryManager)ctx.getBean("PatientHistoryManager", CommonPatientHistoryManager.class);
	}
	
	public static NoteManager getNotaManager(){
		return (NoteManager)ctx.getBean("NoteManager", NoteManager.class);
	}
	
	public static EmergencyManager getEmergencyManager(){
		return (EmergencyManager)ctx.getBean("EmergencyManager", EmergencyManager.class);
	}
	
	public static WardTransactionManager getWardTransactionManager(){
		return (WardTransactionManager)ctx.getBean("WardTransactionManager", WardTransactionManager.class);
	}
	
	public static BedTransactionManager getBedTransactionManager(){
		return (BedTransactionManager)ctx.getBean("BedTransactionManager", BedTransactionManager.class);
	}
	
	public static ApotikManager getApotikManager(){
		return (ApotikManager)ctx.getBean("ApotikManager", ApotikManager.class);
	}
	
	public static PlatformTransactionManager getTrxManager(){
		return (PlatformTransactionManager)ctx.getBean("txManager", PlatformTransactionManager.class);
	}
	
	public static JournalManager getJournalManager(){
		return (JournalManager)ctx.getBean("JournalManager", JournalManager.class);
	}
	
	public static PatientInventoryManager getPatientInventoryManager(){
		return (PatientInventoryManager)ctx.getBean("PatientInventoryManager", PatientInventoryManager.class);
	}
	
	public static CreditCardTypeManager getCreditCardTypeManager(){
		return (CreditCardTypeManager)ctx.getBean("CreditCardTypeManager", CreditCardTypeManager.class);
	}
	
	public static LaboratManager getLaboratManager(){
		return (LaboratManager)ctx.getBean("LaboratManager",LaboratManager.class);
	}
		
	public static CashierManager getCashierManager(){
		return (CashierManager)ctx.getBean("CashierManager",CashierManager.class);
	}
	
	public static WarehouseManager getWarehouseManager(){
		return (WarehouseManager)ctx.getBean("WarehouseManager",WarehouseManager.class);
	}
	
	
	public static RadiologyManager getRadiologyManager(){
		return (RadiologyManager)ctx.getBean("RadiologyManager",RadiologyManager.class);
	}
	
	
	public static AntrianKamarManager getAntrianKamarManager(){
		return (AntrianKamarManager)ctx.getBean("AntrianKamarManager",AntrianKamarManager.class);
	}
	
	
	
	public static OpenSessionInViewFilter getFilterManager(){
		
		return (OpenSessionInViewFilter)ctx.getBean("FilterManager");
	}

	
	
	public static DiagnoseManager getDiagnoseManager() {
		
		return (DiagnoseManager)ctx.getBean("DiagnoseManager", DiagnoseManager.class);
	}
	
	
	public static VkManager getVkManager(){
		
		return (VkManager) ctx.getBean("VkManager", VkManager.class);
		
	}
	
	public static AntrianManager getAntrianManager() {
		return (AntrianManager) ctx.getBean("AntrianManager", AntrianManager.class);
	}
		
	
}
