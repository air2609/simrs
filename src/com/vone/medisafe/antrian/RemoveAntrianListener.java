package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.DoctorManager;

public class RemoveAntrianListener implements EventListener {
	
	Listbox pasList;
	Listitem item;
	DoctorManager manager = MasterServiceLocator.getDoctorManager();
	
	public RemoveAntrianListener(Listbox myList, Listitem item){
		this.pasList = myList;
		this.item = item;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		TbRegistration reg = (TbRegistration)item.getValue();
//		reg.setAntrianStatus(1);
		manager.takeOutFromAntrian(reg, pasList, item);

	}

}
