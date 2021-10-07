package com.vone.medisafe.apotik;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.service.Service;

public class MoveToAntrianApotikListener implements EventListener {
	
	Listbox validList;
	Listbox jadiList;
	Listitem item;
	
	ApotikManager manager = Service.getApotikManager();
	
	public MoveToAntrianApotikListener(Listbox validList, Listbox jadiList, Listitem item){
		this.validList = validList;
		this.jadiList = jadiList;
		this.item = item;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		manager.moveToAntiranApotik(validList, jadiList, item);

	}

}
