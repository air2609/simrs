package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Style;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;



import com.vone.medisafe.apotik.ApotikManager;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class AntrianApotikController extends BaseController{
	
	Window obatWin;
	Listbox listPasien;
	Timer timer;
	Label stApotik;
	
	
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	ApotikManager apotikManager = Service.getApotikManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		obatWin = (Window)cmp.getFellow("obatWin");
		listPasien = (Listbox)obatWin.getFellow("listPasien");
		stApotik = (Label)cmp.getFellow("stApotik");
		timer = (Timer)cmp.getFellow("timer");
		manager.getDelayAntrian(timer, stApotik, 1);
		apotikManager.getApotikAntrian(listPasien);
		super.init(cmp);
		
		
	}
	
	public void onTimer() throws VONEAppException{
		apotikManager.getApotikAntrian(listPasien);
	}

}
