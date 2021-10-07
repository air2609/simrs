package com.vone.medisafe.apotik;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.service.Service;

public class TakeoutAntrianListener implements EventListener {
	
	Listbox myList;
	Listitem myItem;
	
	ApotikManager manager = Service.getApotikManager();
	
	public TakeoutAntrianListener(Listbox list, Listitem item){
		this.myList = list;
		this.myItem = item;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		manager.takeOutAntrianApotik(myList, myItem);

	}

}
