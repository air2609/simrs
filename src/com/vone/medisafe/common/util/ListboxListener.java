package com.vone.medisafe.common.util;




import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;




public class ListboxListener implements EventListener{
	private Label listbox;
	private Bandbox bed;
	
	
	public ListboxListener(Label listbox, Bandbox bed){
		this.listbox = listbox;
		this.bed = bed;
	}
	public void onEvent(Event event) {
		
		try {
			bed.setValue(listbox.getValue());
			bed.closeDropdown();
		} catch (Exception e) {
			
		}
	}

	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

}
