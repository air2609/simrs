package com.vone.test;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;

public class MenuListener implements EventListener {
	
	String menu;
	Include inc;
	public MenuListener(String pathMenu, Include inc){
		this.menu=pathMenu;
		this.inc=inc;
	}

	public void onEvent(Event arg0) throws Exception {
		inc.setSrc(menu);

	}

}
