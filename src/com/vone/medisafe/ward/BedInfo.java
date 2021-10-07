package com.vone.medisafe.ward;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;
import com.vone.medisafe.ui.base.BaseController;

public class BedInfo extends BaseController{
	

	
	
	Listbox bedInfoList;
	Timer timer;
	Label hospitalName;
	int counter = 0;
	
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		super.init(cmp);
		
		bedInfoList = (Listbox)cmp.getFellow("bedInfoList");
		timer = (Timer)cmp.getFellow("timer");
		hospitalName = (Label)cmp.getFellow("hospitalName");
		
		hospitalName.setValue(MessagesService.getKey("hospital.name.short").toUpperCase());
		timer.setDelay(10 * 1000);
		MasterServiceLocator.getBedManager().getBedInfo(bedInfoList);
		
	}
	
	public void onTimer() throws VONEAppException{
		MasterServiceLocator.getBedManager().getBedInfo(bedInfoList);
	}



}
