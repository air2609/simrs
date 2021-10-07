package com.vone.test;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class LogoutListner implements EventListener {

	public void onEvent(Event arg0) throws Exception {
		Session session = Sessions.getCurrent();
		session.invalidate();
		Executions.sendRedirect("login.zul");
	}

}
