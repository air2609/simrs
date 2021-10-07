package com.vone.medisafe.ui.admission;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;

public class BedListener implements EventListener{
	
	private Listbox listbox;
	private Bandbox bbox;
	
	public BedListener(Listbox listbox, Bandbox bbox){
		this.listbox = listbox;
		this.bbox = bbox;
	}
	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof SelectEvent){
			bbox.setValue(listbox.getSelectedItem().getValue().toString());
			bbox.closeDropdown();
			listbox.clearSelection();
		}
	}

}
