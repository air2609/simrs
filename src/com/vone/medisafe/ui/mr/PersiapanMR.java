package com.vone.medisafe.ui.mr;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.iface.admission.RajalManager;
import com.vone.medisafe.ui.base.BaseController;

public class PersiapanMR extends BaseController{
	
	Listbox mrList;
	Listbox mrReadyList;
	
	RajalManager service = AdmissionServiceLocator.getRajalManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		mrList = (Listbox) cmp.getFellow("mrList");
		mrReadyList = (Listbox)cmp.getFellow("mrReadyList");
		
		getMrList();
		getMrReadyList();
		
	}

	private void getMrReadyList() throws VONEAppException {
		service.getPatientOldRegistration(mrList, MedisafeConstants.MR_NOT_READY);
		
	}

	private void getMrList() throws VONEAppException{
		service.getPatientOldRegistration(mrReadyList, MedisafeConstants.MR_READY);
		
	}
	
	public void moveMR() throws InterruptedException, VONEAppException{
		
		if(mrList.getSelectedCount() == 0){
			Messagebox.show("PILIH DATA BERKAS REKAM MEDIS YANG BELUM SIAP TERLEBIH DAHULU...!");
			return;
		}
		
		TbRegistration reg = (TbRegistration)mrList.getSelectedItem().getValue();
		
		reg.setMrStatus(MedisafeConstants.MR_READY);
		
		service.updateRegistration(reg);
		
		getMrList();
		getMrReadyList();
	}

}
