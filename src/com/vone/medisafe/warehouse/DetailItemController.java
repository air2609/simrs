package com.vone.medisafe.warehouse;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.ui.base.BaseController;

public class DetailItemController extends BaseController {
	Session session;
	MsUser user;
	Treechildren permintaanTreechildren;
	Window window;
	public void init(Component win) throws InterruptedException,
		VONEAppException {

		super.init(win);
		window = (Window)win;
		session = win.getDesktop().getSession();
		permintaanTreechildren = (Treechildren)session.getAttribute("permintaanTreechildren");
		
		user = getUserInfoBean().getMsUser();
	}
	public void simpanClick(){
//		permintaanTreechildren.getChildren().clear(); //testing
		window.setAttribute("aksi", "Save");
		window.detach();
	}

}
