package com.vone.medisafe.ui.mr;



import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.constant.ScreenConstant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;


public class MRController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	UserManager userService = ServiceLocator.getUserManager();
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox locationList = (Listbox) win.getFellow("locationList");
		Listbox medicalRecordFileList = (Listbox)win.getFellow("medicalRecordFileList");
		Listbox mRNumberList = (Listbox)win.getFellow("MRNumberList");
		Textbox crNoMR = (Textbox)win.getFellow("crNoMR");
		Textbox crNama = (Textbox)win.getFellow("crNama");
		Textbox crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		
		super.init(win);
		
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		UserInfoBean user = (UserInfoBean)win.getDesktop().getSession().getAttribute(Constant.USER_INFO);
		if(user == null){
			Messagebox.show("user kosong");
			return;
		}
		
		locationList.getItems().clear();
		userService.getUnitUser(user.getMsUser(), locationList);
		
		
//		Listbox locationList2 = user.getUnitListboxByScreenCode(ScreenConstant.PINJAM_BERKAS_RM);
//		List listitem = locationList2.getItems();
//		
//		if(listitem != null){
//			Iterator it = listitem.iterator();
//			while(it.hasNext()){
//				Listitem item = (Listitem)it.next();
//				item.setParent(locationList);
//			}
//			locationList.setSelectedIndex(0);
//		}
		
		
		
		medicalRecordFileList.getItems().clear();
		mRNumberList.getItems().clear();
		
	}
	
	public void getMrStatusByPatientId(Component win) throws InterruptedException, VONEAppException{
		Listbox mRNumberList = (Listbox)win.getFellow("MRNumberList");
		Listbox medicalRecordFileList = (Listbox)win.getFellow("medicalRecordFileList");
		Bandbox mRNumber = (Bandbox)win.getFellow("MRNumber");
		
		TbMedicalRecord mr = null;
		
		if(mRNumber.getText().length() > 1){
			String code = MedisafeUtil.convertToMrCode(mRNumber.getValue());
			mr = AdmissionServiceLocator.getMedicalRecordManager().getMedicalRecord(code);
			mRNumber.setValue(null);
		}
		else{
			mr = (TbMedicalRecord)mRNumberList.getSelectedItem().getValue();
				//AdmissionServiceLocator.getMedicalRecordManager().getMrById(
//					(TbMedi)mRNumberList.getSelectedItem().getValue());
//			mRNumberList.
		
		}
		 
				
		Listitem item = new Listitem();
		item.setValue(mr);
		
		Listcell mrCode = new Listcell(mr.getVMrCode());
		mrCode.setParent(item);
		
		Listcell patientName = new Listcell(mr.getMsPatient().getVPatientName());
		patientName.setParent(item);
		
		String status = null;
		if(mr.getVMrStatus() == null)status="TERSEDIA";
		else if(mr.getVMrStatus().equals(MedisafeConstants.TERSEDIA)) status = "TERSEDIA";
		else if(mr.getVMrStatus().equals(MedisafeConstants.SEDANG_DIPINJAM))status = "SEDANG DIPINJAM";
		else status = "AKAN DIPINJAM";
		
		Listcell mrStatus = new Listcell(status);
		mrStatus.setParent(item);
		
		medicalRecordFileList.appendChild(item);
		MiscTrxController.setFont(medicalRecordFileList);
	}

	public void requestMR(Component win) throws InterruptedException, VONEAppException{
		Listbox medicalRecordFileList = (Listbox)win.getFellow("medicalRecordFileList");
		Listbox locationList = (Listbox) win.getFellow("locationList");
		
//		MsUnit unit = MasterServiceLocator.getUnitManager().getById((Integer)locationList.getSelectedItem().getValue());
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		
		Set items = medicalRecordFileList.getSelectedItems();
		if(items.size() == 0)return;
		int counter = 0;
		Iterator it = items.iterator();
		while(it.hasNext()){
			Listitem item = (Listitem)it.next();
			TbMedicalRecord mr = (TbMedicalRecord)item.getValue();
			if(mr.getVMrStatus() == null || mr.getVMrStatus().equals(MedisafeConstants.TERSEDIA))
			{
				mr.setVMrStatus(MedisafeConstants.AKAN_DIPINJAM);
				mr.setMsUnit(unit);
				AdmissionServiceLocator.getMedicalRecordManager().save(mr);
				
			}
			else{
				Messagebox.show("Berkas Rekam Medis dengan Kode "+mr.getVMrCode()+" Tidak Bisa Dipinjam..!");
				counter++;
			}
			
		}
		if(items.size() == counter) return;
		Messagebox.show("Data Sudah Terkirim..!");
	}
	
	public void clearListitem(Listbox listbox){
		listbox.getItems().clear();
	}
}
