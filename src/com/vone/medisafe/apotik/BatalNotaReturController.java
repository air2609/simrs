package com.vone.medisafe.apotik;



import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbReturPharmacyDetailTrx;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class BatalNotaReturController extends BaseController{

	private MsUser user;

	Textbox cancelationReason;
	Textbox username;
	Textbox password;
	Component win;
	
	Session session;
	
	ZulConstraint constraint = new ZulConstraint();
	
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
		constraint.registerComponent(username, ZulConstraint.NO_EMPTY);
		constraint.registerComponent(password, ZulConstraint.NO_EMPTY);
		
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
			
			Window window = (Window)win.getDesktop().getPage("returItemPage").getFellow("PharmacyReturn");
			
			Bandbox nomorNota = (Bandbox)window.getFellow("returnNoteNumber");
			Button btnModify = (Button)window.getFellow("btnModify");
			Button btnValidation = (Button)window.getFellow("btnValidation");
			Button btnCancelNote = (Button)window.getFellow("btnCancelNote");
		//	Button cancelButton = (Button)window.getFellow("btnCancel");
			Label labelStatus = (Label)window.getFellow("labelStatus");
			Label statusNota = (Label)window.getFellow("statusNota");
			Listbox location = (Listbox)window.getFellow("locationList");
			
			TbReturPharmacyTrx retur = (TbReturPharmacyTrx)nomorNota.getAttribute("retur");
			
			MsUnit unit = (MsUnit)location.getSelectedItem().getValue();
			
			if(Service.getApotikManager().cancelReturnTrx(retur, null, unit)){
				Messagebox.show(MessagesService.getKey("common.nota.cancelatioan.success"));
				
				statusNota.setValue(MedisafeUtil.getNoteStatus(retur.getNStatus()));
				labelStatus.setVisible(true);
				statusNota.setVisible(true);
				btnValidation.setDisabled(true);
				btnModify.setDisabled(true);
				btnCancelNote.setDisabled(true);
			//	cancelButton.setDisabled(true);
				
			}
			else{
				Messagebox.show(MessagesService.getKey("common.nota.cancelatioan.fail"));
			}
				
		} 
		else{
			Messagebox.show(MessagesService.getKey("common.password.not.match"));
			this.username.focus();
			return;
		} 
	} 

}
