package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.service.iface.master.StaffManager;
import com.vone.medisafe.ui.base.BaseController;

public class AntrianPerDokterController extends BaseController{
	
	Label dokter;
	Listbox listPasien;
	MsStaff staff;
	StaffManager staffService = MasterServiceLocator.getStaffManager();
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		dokter = (Label) cmp.getFellow("dokter");
		listPasien = (Listbox)cmp.getFellow("listPasien");
		
		staff = staffService.getByStaffId(getUserInfoBean().getMsUser().getMsStaff().getNStaffId());
		
		dokter.setValue(staff.getVStaffName());
		
		manager.getAntrian(staff, listPasien);
	}

}
