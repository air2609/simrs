package com.vone.medisafe.ui.common;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;


import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;


public class NoteCancelationController extends BaseController{

	
	private MsUser user;
	
	Textbox cancelationReason;
	Textbox username;
	Textbox password;
	Component win;
	
	Session session;
	
	ZulConstraint constraint = new ZulConstraint();
	
	NoteManager service = Service.getNotaManager();
	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		cancelationReason = (Textbox)win.getFellow("cancelationReason");
		username = (Textbox)win.getFellow("username");
		password = (Textbox)win.getFellow("password");
		
		constraint.registerComponent(username, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(cancelationReason, ZulConstraint.UPPER_CASE);
		this.win = win;
		
		user = getUserInfoBean().getMsUser();
		
		session = win.getDesktop().getSession();
	}
	
	public void cancelNote() throws InterruptedException, VONEAppException{
		//check if user valid
		MD5 md5 = new MD5(password.getValue());
		String username = this.username.getValue();
		String password = md5.toString();
		
		if(username.equals(user.getVUserName()) && password.equals(user.getVPassword())){
			
			//cancel[0] = nota
			//cancel[1] = warehouse id
			//cancel[2] = tipe pembatalan
			//cancel[3] = btn validation
			//canel[4] = btn modify
			//cancel[5] = btn cancelation note
			//cancel[6] = note status
//			
			
			service.cancelNote(cancelationReason.getText());
			
			win.detach();
			
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.password.not.match"));
			return;
		}
	}
}
