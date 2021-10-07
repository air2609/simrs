package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;

public class DoctorListener implements EventListener {
	
//	Listitem item;
	Listbox doctList;
	Listbox pasienList;
	
	DoctorManager manager = MasterServiceLocator.getDoctorManager();

	public DoctorListener(Listbox doctorList, Listbox pasList){
//		this.item = item;
		this.doctList = doctorList;
		this.pasienList = pasList;
	}
	
	
	public void onEvent(Event arg0) throws Exception {
		
		MsStaff staff = (MsStaff)doctList.getSelectedItem().getValue();
		if(staff != null)
			manager.getAntrian(staff, pasienList);

	}

}
