package com.vone.medisafe.ui;

import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.LogoutListener;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.SessionManager;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;

public class LoginController{
	
	ZulConstraint constraint = new ZulConstraint();
	
	Logger logger = Logger.getLogger(LoginController.class);
	
	Textbox name = null;
	Textbox password = null;
	
	public void init(Window win) throws InterruptedException{
		
		name = (Textbox)win.getFellow("userName");
		password = (Textbox)win.getFellow("password");
		
		constraint.registerComponent(name);
		constraint.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(password);
		
		name.focus();
	}
	
	public void doSubmit(Window win) throws InterruptedException, VONEAppException {
		
		if (!constraint.validateComponent(true));
								
		String userId = ((Textbox)win.getFellow("userName")).getValue();
		String password = ((Textbox)win.getFellow("password")).getValue();		
		
		UserManager userManager = ServiceLocator.getUserManager();
		   
		if (!StringUtils.hasValue(userId) || !StringUtils.hasValue(password)){
			
			Messagebox.show(MessagesService.getKey("login.invalid"));
		}else{
			
			userManager.validateLogin(userId,password);
		
		}
	}
	
}
