package com.vone.test;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

public class CPListener implements EventListener {

	public void onEvent(Event arg0) throws Exception {
		Window win = (Window)Executions.createComponents("/zkpages/admin/user/changePassword.zul", null, null);

		win.doModal();
	}

}
