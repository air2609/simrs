package com.vone.medisafe.ward;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbPatientInventory;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.pojo.item.MsItem;

/**
 * PatientInventoryManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 23 2006
 * @category service (logical model)
 */



public class PatientInventoryManagerImpl implements PatientInventoryManager{
	
	private PatientInventoryDAO dao;
	private TbMedicalRecordDAO mrDao;

	public PatientInventoryDAO getDao() {
		return dao;
	}

	public void setDao(PatientInventoryDAO dao) {
		this.dao = dao;
	}

	
	

	public List<TbPatientInventory> getPatientInventory(TbRegistration patient) throws VONEAppException {
		List<TbPatientInventory> hasil = new ArrayList<TbPatientInventory>();
		TbPatientInventory patientInventory = null;
		List result = dao.getPatientInventory(patient);
		Iterator it = result.iterator();
		while(it.hasNext()){
			patientInventory = (TbPatientInventory)it.next();
			hasil.add(patientInventory);
		}
		return hasil;
	}

	public boolean save(List<TbPatientInventory> inventory) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.save(inventory);
	}

	public boolean delete(Set<TbPatientInventory> patInv) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.delete(patInv);
	}

	
	public void getHistoryInventory(PatientInventoryController controller) throws VONEAppException {
		
		int jumlahTerpakai = 0;
		int jumlahMasuk = 0;
		int sisa = 0;
		
		for(MsItem msItem : controller.msItems){
			
			controller.treeItem = new Treeitem();
			controller.treeItem.setValue(msItem);
			controller.treeItem.setParent(controller.child);
			
			controller.treeRow = new Treerow();
			controller.treeRow.setParent(controller.treeItem);
			
			controller.treeCell = new Treecell(msItem.getVItemName());
			controller.treeCell.setParent(controller.treeRow);
			
			controller.children = new Treechildren();
			controller.children.setParent(controller.treeItem);
			
			controller.inventories = dao.getPatientInventory(msItem, controller.reg);
			for(TbPatientInventory pat : controller.inventories){
				
				Treeitem treeItem = new Treeitem();
				treeItem.setValue(pat);
				treeItem.setParent(controller.children);
				
				controller.treeRow = new Treerow();
				controller.treeRow.setParent(treeItem);
				
				
				controller.treeCell = new Treecell(MedisafeUtil.convertDateToString(pat.getDWhnCreate()));
				controller.treeCell.setParent(controller.treeRow);
				
				jumlahMasuk = jumlahMasuk + pat.getNQty();
				controller.treeCell = new Treecell(pat.getNQty()+"");
				controller.treeCell.setParent(controller.treeRow);
				
				jumlahTerpakai = jumlahTerpakai + pat.getNQtyOut();
				controller.treeCell = new Treecell(pat.getNQtyOut()+"");
				controller.treeCell.setParent(controller.treeRow);
				
				sisa = jumlahMasuk - jumlahTerpakai;
				controller.treeCell = new Treecell(sisa+"");
				controller.treeCell.setParent(controller.treeRow);
				
			}
			
			jumlahTerpakai = 0;
			jumlahMasuk = 0;
			
		}

		
	}

	
	public void getPatientInventory(PatientInventoryController controller) throws VONEAppException {
		
		Listitem item;
		Listcell cell;
		
		if(Sessions.getCurrent().getAttribute("mr") == null){
			return;
		}
		
		TbMedicalRecord mr = (TbMedicalRecord) Sessions.getCurrent().getAttribute("mr");
		mr = mrDao.findById(mr.getNMrId());
		controller.reg = mrDao.getLastActiveRegistration(mr.getNMrId());
		
		controller.patient = mr.getMsPatient();
				
		int jumlahMasuk = 0;
		int jumlahKeluar = 0;
		Intbox ibox = null;
		
		controller.msItems = dao.getPatientItems(controller.reg);
		
		for(MsItem msItem : controller.msItems){
			item = new Listitem();
			item.setValue(msItem);
			item.setParent(controller.patientInventoryList);
			
			cell = new Listcell(msItem.getVItemCode());
			cell.setParent(item);
			
			cell = new Listcell(msItem.getVItemName());
			cell.setParent(item);
												
			controller.inventories = dao.getPatientInventory(msItem, controller.reg);
			
			for(TbPatientInventory pat: controller.inventories){
				jumlahMasuk = jumlahMasuk + pat.getNQty();
				jumlahKeluar = jumlahKeluar + pat.getNQtyOut();
			}
			
			cell =  new Listcell(jumlahMasuk+"");
			cell.setParent(item);
			
			cell = new Listcell(jumlahKeluar+"");
			cell.setParent(item);
			
			cell = new Listcell(msItem.getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
			cell = new Listcell();
			cell.setParent(item);
			ibox = new Intbox();
			ibox.setWidth("90%");
			ibox.setParent(cell);
			
			jumlahKeluar = 0;
			jumlahMasuk = 0;
			
		}

		
	}

	public TbMedicalRecordDAO getMrDao() {
		return mrDao;
	}

	public void setMrDao(TbMedicalRecordDAO mrDao) {
		this.mrDao = mrDao;
	}

	
	
	public List<TbPatientInventory> getPatientInventory(MsItem msItem, TbRegistration patient) throws VONEAppException {
		
		return dao.getPatientInventory(msItem, patient);
	}
	
	

}
