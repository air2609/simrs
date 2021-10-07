package com.vone.medisafe.ui.component;

import java.util.Date;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;

public class DojoController {

	public void doLogout(){
		
		Session session = Sessions.getCurrent();
		session.invalidate();
		
		Executions.sendRedirect("login.zul");
	}
	
	public void invokeCP() throws InterruptedException{
		
		Window win = (Window)Executions.createComponents("/zkpages/admin/user/changePassword.zul", null, null);

		win.doModal();
	}
	
	public boolean executeCP(String stOldP, String stNewP) throws InterruptedException, VONEAppException{
		
		UserInfoBean uib = (UserInfoBean)Sessions.getCurrent().getAttribute(Constant.USER_INFO);
		UserManager userManager = ServiceLocator.getUserManager();
		
		if (uib == null) return false;
		
		MsUser userPojo = userManager.getMsUser(uib.getStUserId());		
		
		if (userPojo.getVPassword().equals(new MD5(stOldP).toString())){
			userPojo.setVPassword(new MD5(stNewP).toString());
			userPojo.setVWhochange(uib.getStUserId());
			userPojo.setDWhnchange(new Date());
			
			userManager.update(userPojo);
			
			Messagebox.show(MessagesService.getKey("admin.user.changepassword.success"));
			
			return true;
		}	
		
		Messagebox.show(MessagesService.getKey("admin.user.changepassword.fail"));
		
		return false;
	}
}
