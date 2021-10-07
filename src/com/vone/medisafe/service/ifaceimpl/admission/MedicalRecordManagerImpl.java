package com.vone.medisafe.service.ifaceimpl.admission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.TbDiagnose;
import com.vone.medisafe.mapping.TbIcd9Diagnose;
import com.vone.medisafe.mapping.TbIcdDiagnose;
import com.vone.medisafe.mapping.TbIllness;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbMedicalRecordDAO;
import com.vone.medisafe.mapping.TbOperation;
import com.vone.medisafe.mapping.TbPregnancy;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbStillbirth;
import com.vone.medisafe.mapping.TbViolent;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.ui.mr.MRViewController;

public class MedicalRecordManagerImpl implements MedicalRecordManager{
	
	TbMedicalRecordDAO dao;

	/**
	 * @return Returns the dao.
	 */
	public TbMedicalRecordDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(TbMedicalRecordDAO dao) {
		this.dao = dao;
	}

	public void save(TbMedicalRecord mr) {
		// TODO Auto-generated method stub
		this.dao.save(mr);
	}

	
	public TbMedicalRecord getMedicalRecord(String mrCode) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getPatientMedicalRecord(mrCode);
	}

	public TbMedicalRecord getByRegistration(TbRegistration reg) {
		// TODO Auto-generated method stub
		return null;
	}

	public TbMedicalRecord getMrById(Integer id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	public TbMedicalRecord getMrByPatientId(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getMedicalRecordByPaitentId(id);
	}

	public List getMROut() throws VONEAppException{
		
		return dao.getMROut();
	}

	public TbMedicalRecord getMrRegistered(String mrCode) throws VONEAppException{
	
		return dao.getMrRegistered(mrCode);
	}

	public List<TbMedicalRecord> getMrByStatus(String mrStatus) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getMrByStatus(mrStatus);
	}

	public Integer countVisit(TbMedicalRecord mr) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.countVisit(mr);
	}

	
	public boolean saveDiagnose(TbDiagnose diagnose, List<TbIcdDiagnose> icdList, List<TbIcd9Diagnose> icd9List, 
			List<TbIllness> illness, List<TbOperation> operation, List<TbViolent> rudapaksa, 
			List<TbPregnancy> kehamilan, List<TbStillbirth> keguguran, boolean isUpdate) throws VONEAppException {
		
		return dao.saveDiagnose(diagnose,icdList,icd9List,illness,operation,rudapaksa,kehamilan,keguguran,isUpdate);
	}

	
	
	public void viewMrStatus(Listbox mrFileStatueList, Listbox mrFileList) throws VONEAppException {
		
		List<TbMedicalRecord> list = null;
	
		if(mrFileStatueList.getSelectedItem().getValue().equals(MedisafeConstants.SEDANG_DIPINJAM))
		{
			list = dao.getMrByStatus(MedisafeConstants.SEDANG_DIPINJAM);
		}
		else if(mrFileStatueList.getSelectedItem().getValue().equals(MedisafeConstants.AKAN_DIPINJAM))
		{
			list = dao.getMrByStatus(MedisafeConstants.AKAN_DIPINJAM);
		}
		
		
		showMrList(list,mrFileList);
	
	
	}
	
	
	public void showMrList(List<TbMedicalRecord> mrList, Listbox mrFileList){
		
		Listitem item;
		Listcell cell;
		
		mrFileList.getItems().clear();
		
		String status = "";
		
		for(TbMedicalRecord mr : mrList)
		{
			item = new Listitem();
			item.setParent(mrFileList);
			
			
			cell = new Listcell(mr.getVMrCode());
			cell.setParent(item);
			
			cell = new Listcell(mr.getMsPatient().getVPatientName());
			cell.setParent(item);
			
			if(mr.getVMrStatus() == null || mr.getVMrStatus().equals(MedisafeConstants.TERSEDIA))
				status = "TERSEDIA";
			else if(mr.getVMrStatus().equals(MedisafeConstants.SEDANG_DIPINJAM))
				status = "SEDANG DIPINJAM";
			else if(mr.getVMrStatus().equals(MedisafeConstants.AKAN_DIPINJAM))
				status = "AKAN DIPINJAM";
			
			cell = new Listcell(status);
			cell.setParent(item);
			
			if(mr.getMsUnit() == null || mr.getVMrStatus().equals(MedisafeConstants.AKAN_DIPINJAM) || mr.getVMrStatus().equals(MedisafeConstants.TERSEDIA))
				status = "REKAM MEDIS";
			else status = mr.getMsUnit().getVUnitName();
			cell = new Listcell(status);
			cell.setParent(item);
		}
		
	}
	
	

	public void getMrStatus(MRViewController controller, int type) throws VONEAppException, InterruptedException {
		
		TbMedicalRecord mr = null;
		Listitem item;
		
		if(type == MedisafeConstants.INPUT_BY_MANUAL){
			String code = MedisafeUtil.convertToMrCode(controller.mrNumber.getText());
			controller.mrNumber.setValue(code);
			
			mr = dao.getPatientMedicalRecord(code);
			if(mr == null){
				try {
					Messagebox.show(MessagesService.getKey("mr.not.found"));
					controller.mrNumber.focus();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		else{
			item = controller.mrList.getSelectedItem();
			TbMedicalRecord mrPat = (TbMedicalRecord)item.getValue();
			mr = dao.getPatientMedicalRecord(mrPat.getVMrCode());
		}
		
		List<TbMedicalRecord> mrList = new ArrayList<TbMedicalRecord>();
		mrList.add(mr);
		
		showMrList(mrList, controller.mrFileList);
	}

	
	public void getMROut(Listbox medicalRecordFile) throws VONEAppException {
		
		List<TbMedicalRecord> mrOutList = dao.getMROut();
		
		showMROut(medicalRecordFile, mrOutList);
		
	}
	
	
	public void showMROut(Listbox view, List mrOutList) {
		TbMedicalRecord mr = null;
		Listitem item = null;
		Listcell cell = null;
		
		view.getItems().clear();
		
		Iterator it = mrOutList.iterator();
		while(it.hasNext()){
			mr = (TbMedicalRecord)it.next();
			item = new Listitem();
			item.setValue(mr);
			item.setParent(view);
			
			cell = new Listcell(mr.getVMrCode());
			cell.setParent(item);
			
			cell = new Listcell(mr.getMsPatient().getVPatientName());
			cell.setValue(mr.getMsPatient());
			cell.setParent(item);
			
			String status = null;
			if(mr.getVMrStatus().equals(MedisafeConstants.TERSEDIA)) status = "TERSEDIA";
			else if(mr.getVMrStatus().equals(MedisafeConstants.SEDANG_DIPINJAM))status = "SEDANG DIPINJAM";
			else status = "AKAN DIPINJAM";
			
			cell = new Listcell(status);
			cell.setParent(item);
			
			cell = new Listcell(mr.getMsUnit().getVUnitName());
			cell.setValue(mr.getMsUnit());
			cell.setParent(item);
		}
	}

	
	
	public void updateStatus(Component win, String status) throws VONEAppException, InterruptedException {
		
		
		Listbox medicalRecordFile = (Listbox)win.getFellow("medicalRecordFile");
		
		Listitem item = null;
		TbMedicalRecord mr = null;
		
		Set mrSet = medicalRecordFile.getSelectedItems();
		
		if(mrSet.size() == 0){
			Messagebox.show(MessagesService.getKey("mr.noselected"));
			return;
		}
				
		Iterator it = mrSet.iterator();
		while(it.hasNext()){
			
			item = (Listitem)it.next();
			mr = (TbMedicalRecord)item.getValue();
			
			if(status.equals(MedisafeConstants.MR_OUT)){
				
//				update status menjadi sedang keluar
				if(mr.getVMrStatus().equals(MedisafeConstants.SEDANG_DIPINJAM))
					Messagebox.show(mr.getVMrCode()+" "+MessagesService.getKey("mr.cannot.go.out"));
				else mr.setVMrStatus(MedisafeConstants.SEDANG_DIPINJAM);
				
			}
			else if(status.equals(MedisafeConstants.MR_BACK)){
				
				if(mr.getVMrStatus().equals(MedisafeConstants.AKAN_DIPINJAM))
					Messagebox.show(mr.getVMrCode()+" "+MessagesService.getKey("mr.cannot.be.back"));
				else mr.setVMrStatus(MedisafeConstants.TERSEDIA);
				
			}
			else{
				
				if(mr.getVMrStatus().equals(MedisafeConstants.AKAN_DIPINJAM))
					mr.setVMrStatus(MedisafeConstants.TERSEDIA);
				else{
					Messagebox.show(MessagesService.getKey("mr.is.cannot.be.cancel"));
				}
				
			}
			
			this.save(mr);
			Messagebox.show(mr.getVMrCode()+" "+MessagesService.getKey("mr.update.status.success"));		
		}
		
		
		List result = this.getMROut();
		showMROut(medicalRecordFile, result);

	}

}
