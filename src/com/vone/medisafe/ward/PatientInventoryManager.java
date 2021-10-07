package com.vone.medisafe.ward;

import java.util.List;
import java.util.Set;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;


/**
 * PatientInventoryManager.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 23 2006
 * @category business model
 */

public interface PatientInventoryManager {

	
	
	
	
	public List<TbPatientInventory> getPatientInventory(TbRegistration patient) throws VONEAppException;
	
	public boolean save(List<TbPatientInventory> inventory) throws VONEAppException; 
	
	public boolean delete(Set<TbPatientInventory> patInv) throws VONEAppException;
	
	public void getPatientInventory(PatientInventoryController controller) throws VONEAppException;

	public void getHistoryInventory(PatientInventoryController controller) throws VONEAppException;

	public List<TbPatientInventory> getPatientInventory(MsItem msItem, TbRegistration patient) throws VONEAppException;
}
