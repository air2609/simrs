package com.vone.medisafe.common.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.ui.base.BaseController;

public class DateRangeController extends BaseController {
	Window window;
	Session session;
	Datebox dateFrom;
	Datebox dateTo;
	
	public void init(Component win) throws InterruptedException,
			VONEAppException {

		super.init(win);
		window = (Window)win;
		dateFrom = (Datebox) win.getFellow("dateFrom");
		dateTo = (Datebox) win.getFellow("dateTo");
		session = win.getDesktop().getSession();
	
	}
	public void validate() throws InterruptedException, VONEAppException{
		window.setAttribute("aksi", 1);
		window.detach();
	}

}
