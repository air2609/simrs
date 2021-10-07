package com.vone.medisafe.service;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import com.vone.medisafe.laborat.LaboratManager;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.service.iface.acct.COAManager;
import com.vone.medisafe.service.iface.master.BankManager;
import com.vone.medisafe.service.iface.master.BedManager;
import com.vone.medisafe.service.iface.master.DivisionManager;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.service.iface.master.GimManager;
import com.vone.medisafe.service.iface.master.HallManager;
import com.vone.medisafe.service.iface.master.InsuranceManager;
import com.vone.medisafe.service.iface.master.LabTestDetailManager;
import com.vone.medisafe.service.iface.master.MedicStaffGroupManager;
import com.vone.medisafe.service.iface.master.MiscFeeManager;
import com.vone.medisafe.service.iface.master.PatientEscortManager;
import com.vone.medisafe.service.iface.master.PatientManager;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.service.iface.master.ProvinceManager;
import com.vone.medisafe.service.iface.master.RegenyManager;
import com.vone.medisafe.service.iface.master.RoomManager;
import com.vone.medisafe.service.iface.master.ShiftManager;
import com.vone.medisafe.service.iface.master.StaffInUnitManager;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.service.iface.master.SubDstrictManager;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.service.iface.master.VillageManager;
import com.vone.medisafe.service.iface.master.WardManager;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.service.iface.master.item.BloodManager;
import com.vone.medisafe.service.iface.master.item.ItemGroupManager;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.service.iface.master.item.ItemMeasurementManager;
import com.vone.medisafe.service.iface.master.item.ItemSellingPriceManager;
import com.vone.medisafe.service.iface.master.item.VendorManager;
import com.vone.medisafe.service.iface.master.treatment.TreatmentGroupManager;


public class MasterServiceLocator {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static SessionFactory getSessionFactory(){
		return (SessionFactory) ctx.getBean("SessionFactory",SessionFactory.class);
	}
	
	public static PlatformTransactionManager getTrxManager(){
		return (PlatformTransactionManager)ctx.getBean("txManager", PlatformTransactionManager.class);
	}

	
	public static VillageManager getVillageManager(){		

		return (VillageManager)ctx.getBean("VillageManager",VillageManager.class);
	}
	
	public static ProvinceManager getProvinceManager(){
		return (ProvinceManager)ctx.getBean("ProvinceManager",ProvinceManager.class);
	}
	
	public static PatientManager getPatientManager(){
		return (PatientManager)ctx.getBean("PatientManager",PatientManager.class);
	}
	
	public static PatientTypeManager getPatientTypeManager(){
		return (PatientTypeManager)ctx.getBean("PatientTypeManager",PatientTypeManager.class);
	}
	
	public static SubDstrictManager getSubDistrictManager(){
		return (SubDstrictManager)ctx.getBean("SubDistrictManager",SubDstrictManager.class);
	}
	
	public static RegenyManager getRegencyManager(){
		return (RegenyManager)ctx.getBean("RegencyManager",RegenyManager.class);
	}
	
	public static DivisionManager getDivisionManager(){
		return (DivisionManager)ctx.getBean("DivisionManager",DivisionManager.class);
	}
	
	public static UnitManager getUnitManager(){
		return (UnitManager)ctx.getBean("UnitManager",UnitManager.class);
	}
	
	public static WardManager getWardManager(){
		return (WardManager)ctx.getBean("WardManager",WardManager.class);
	}
	
	public static TreatmentClassManager getTreatmentClassManager(){
		return (TreatmentClassManager)ctx.getBean("TreatmentClassManager",TreatmentClassManager.class);
	}
	
	public static HallManager getHallManager(){
		return (HallManager)ctx.getBean("HallManager",HallManager.class);
	}
	
	public static RoomManager getRoomManager(){
		return (RoomManager)ctx.getBean("RoomManager",RoomManager.class);
	}
	
	public static StaffManager getStaffManager(){
		return (StaffManager)ctx.getBean("StaffManager",StaffManager.class);
	}
	
	public static StaffInUnitManager getStaffInUnitManager(){
		return (StaffInUnitManager)ctx.getBean("StaffInUnitManager",StaffInUnitManager.class);
	}
	
	public static MedicStaffGroupManager getMedicStaffGroupManager(){
		return (MedicStaffGroupManager)ctx.getBean("MedicStaffGroupManager",MedicStaffGroupManager.class);
	}
	
	public static DoctorManager getDoctorManager(){
		return (DoctorManager)ctx.getBean("DoctorManager",DoctorManager.class);
	}
	public static BedManager getBedManager(){
		return (BedManager)ctx.getBean("BedManager",BedManager.class);
	}
	
	public static ItemGroupManager getItemGroupManager(){
		return (ItemGroupManager)ctx.getBean("ItemGroupManager",ItemGroupManager.class);
	}
	
	public static VendorManager getVendorManager(){
		return (VendorManager)ctx.getBean("VendorManager",VendorManager.class);
	}
	
	public static ItemMeasurementManager getItemMeasurementManager(){
		return (ItemMeasurementManager)ctx.getBean("ItemMeasurementManager",ItemMeasurementManager.class);
	}
	
	public static ItemManager getItemManager(){
		return (ItemManager)ctx.getBean("ItemManager",ItemManager.class);
	}
	
	/**
	 * added at 29-09-2006
	 * @return COAManager
	 */
	public static COAManager getCoaManager(){
		return (COAManager)ctx.getBean("CoaManager",COAManager.class);
	}

	
	/**
	 * added at 02-10-2006
	 * @return ItemSellingPriceManager
	 */
	public static ItemSellingPriceManager getItemSellingPriceManager(){
		return (ItemSellingPriceManager)ctx.getBean("ItemSellingPriceManager",ItemSellingPriceManager.class);
	}

	
	/**
	 * Bank Master
	 * by JiM
	 */
	public static BankManager getBankManager(){
		return (BankManager)ctx.getBean("BankManager",BankManager.class);
	}
	
	/**
	 * General Information Master
	 * @return
	 */
	public static GimManager getGimManager(){
		return (GimManager)ctx.getBean("GimManager",GimManager.class);
	}

	/**
	 * Insurance Master
	 * @return
	 */
	public static InsuranceManager getInsuranceManager(){
		return (InsuranceManager)ctx.getBean("InsuranceManager",InsuranceManager.class);
	}
	
	/**
	 * added at 06-10-2006
	 * Item Group Master
	 * @return
	 */
	
	public static TreatmentGroupManager getTreatmentGroupManager(){
		return (TreatmentGroupManager)ctx.getBean("TreatmentGroupManager",TreatmentGroupManager.class);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static WarehouseManager getWarehouseManager(){
		return (WarehouseManager)ctx.getBean("WarehouseManager",WarehouseManager.class);
	}	

	/**
	 * 
	 * @return
	 */
	public static PatientEscortManager getPatientEscortManager(){
		return (PatientEscortManager)ctx.getBean("PatientEscortManager",PatientEscortManager.class);
	}	
	
	/**
	 * 
	 * @return
	 */
	public static ShiftManager getShiftManager(){
		return (ShiftManager)ctx.getBean("ShiftManager",ShiftManager.class);
	}
	
	public static MiscFeeManager getMiscFeeManager(){
		return (MiscFeeManager)ctx.getBean("MiscFeeManager", MiscFeeManager.class);
	}
	
	public static BloodManager getBloodManager(){
		return (BloodManager)ctx.getBean("BloodManager", BloodManager.class);
	}
	
	public static LabTestDetailManager getLabTestDetailManager(){
		return (LabTestDetailManager)ctx.getBean("LabTestDetailManager",LabTestDetailManager.class);
	}


	public static LaboratManager getLaboratManager() {
		return (LaboratManager)ctx.getBean("LaboratManager",LaboratManager.class);
	}


	public static NoteManager getNoteManager() {
		// todo Auto-generated method stub
		return (NoteManager)ctx.getBean("NoteManager", NoteManager.class);
	}
}
