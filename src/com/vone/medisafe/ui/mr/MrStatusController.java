package com.vone.medisafe.ui.mr;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.ui.base.BaseController;

public class MrStatusController extends BaseController {
	
	MedicalRecordManager service = AdmissionServiceLocator.getMedicalRecordManager();
	

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox medicalRecordFile = (Listbox)win.getFellow("medicalRecordFile");
		super.init(win);
		
		service.getMROut(medicalRecordFile);
		MiscTrxController.setFont(medicalRecordFile);
		
	}
	
	
	
	public void updateStatus(Component win, String status) throws InterruptedException, VONEAppException{
		Listbox medicalRecordFile = (Listbox)win.getFellow("medicalRecordFile");
		
		service.updateStatus(win,status);
		MiscTrxController.setFont(medicalRecordFile);
	
				
	}

}
