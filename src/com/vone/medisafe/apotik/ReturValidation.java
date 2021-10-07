package com.vone.medisafe.apotik;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbReturPharmacyTrx;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class ReturValidation extends BaseController{
	
private MsUser user;
	
	Datebox date;
	Listbox shiftList;
	Textbox username;
	Textbox password;
	Component win;
	
	Session session;
	
	ZulConstraint constraints = new ZulConstraint();
	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		date = (Datebox)win.getFellow("date");
		shiftList = (Listbox)win.getFellow("shiftList");
		username = (Textbox)win.getFellow("username");
		password = (Textbox)win.getFellow("password");
		this.win = win;
		
		constraints.registerComponent(username, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(username, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(password, ZulConstraint.NO_EMPTY);
		constraints.validateComponent(false);
		
		user = getUserInfoBean().getMsUser();
		
		session = win.getDesktop().getSession();
		
		date.setValue(new Date());
		
//		fill shift List
		Iterator<MsShift> it = MasterServiceLocator.getShiftManager().findAll().iterator();
		
		String curShift = getCurrentShift();
		
		while (it.hasNext()){
			MsShift sPojo = it.next();
			
			Listitem item = new Listitem();
			item.setParent(shiftList);
			item.setValue(sPojo);
			
			item.appendChild(new Listcell(sPojo.getVShiftCode()));
			
			if (sPojo.getVShiftCode() != null && sPojo.getVShiftCode().equals(curShift)){
				shiftList.setSelectedItem(item);
			}
		}
		
		if (shiftList.getSelectedItem() == null)
			shiftList.setSelectedIndex(0);
		
		this.username.select();
		
	}
	
	private String getCurrentShift() throws VONEAppException {

		Iterator<MsShift> it = MasterServiceLocator.getShiftManager().findAll().iterator();
		
		while (it.hasNext()){
			
			MsShift shiftPojo = it.next();
			
			String dCurStr = MedisafeUtil.convertDateToTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			Date dFrom = null;
			Date dTo = null;
			Date dCur = null;
			
			try {
				dFrom = shiftPojo.getDShiftFrom();
				dTo = shiftPojo.getDShiftTo();
				dCur = sdf.parse(dCurStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new VONEAppException(e.getMessage());
			}
			
			//on the right shift..
			if (dCur.after(dFrom) && dCur.before(dTo)){
				return shiftPojo.getVShiftCode();
			}
		}
		
		return null;
	}
	
	public void validate() throws InterruptedException, VONEAppException{
		//check if user valid
		MD5 md5 = new MD5(password.getValue());
		String username = this.username.getValue();
		String password = md5.toString();
		
		if(username.equals(user.getVUserName()) && password.equals(user.getVPassword())){
			
			Window window = (Window)win.getDesktop().getPage("returItemPage").getFellow("PharmacyReturn");
			
			Bandbox nomorNota = (Bandbox)window.getFellow("returnNoteNumber");
			Button btnModify = (Button)window.getFellow("btnModify");
			Button btnValidation = (Button)window.getFellow("btnValidation");
			//Button cancelButton = (Button)window.getFellow("btnCancel");
			Label labelStatus = (Label)window.getFellow("labelStatus");
			Label statusNota = (Label)window.getFellow("statusNota");
			
			TbReturPharmacyTrx retur = (TbReturPharmacyTrx)nomorNota.getAttribute("retur");
			retur.setNStatus((short)MedisafeConstants.VALIDATED_NOTE);
			
			MsUnit unit = (MsUnit)Sessions.getCurrent().getAttribute("lokasiretur");
			
			if(Service.getApotikManager().validateReturNote(retur,unit)){
				
				Messagebox.show(MessagesService.getKey("common.validate.success"));
				statusNota.setValue(MedisafeUtil.getNoteStatus(retur.getNStatus()));
				labelStatus.setVisible(true);
				statusNota.setVisible(true);
				btnValidation.setDisabled(true);
				btnModify.setDisabled(true);
			//	cancelButton.setDisabled(true);
				
			}else{
				Messagebox.show(MessagesService.getKey("common.validate.fail"));
			}

		}
		else{
			Messagebox.show(MessagesService.getKey("common.password.not.match"));
			return;
		}
	}

}
