package com.vone.medisafe.ui.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;


public class ValidationController extends BaseController{
	
	private MsUser user;
	
	Datebox date;
	Listbox shift;
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
		shift = (Listbox)win.getFellow("shiftList");
		username = (Textbox)win.getFellow("username");
		password = (Textbox)win.getFellow("password");
		this.win = win;
		
		constraints.registerComponent(username, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(username, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(password, ZulConstraint.NO_EMPTY);
		constraints.validateComponent(false);
		
		user = getUserInfoBean().getMsUser();
		
		session = win.getDesktop().getSession();
		
		this.date.setValue(new Date());
		
		//fill shift List
		Iterator<MsShift> it = MasterServiceLocator.getShiftManager().findAll().iterator();
		
		String curShift = getCurrentShift();
		
		while (it.hasNext()){
			MsShift sPojo = it.next();
			
			Listitem item = new Listitem();
			item.setParent(shift);
			item.setValue(sPojo);
			
			item.appendChild(new Listcell(sPojo.getVShiftCode()));
			
			if (sPojo.getVShiftCode() != null && sPojo.getVShiftCode().equals(curShift)){
				shift.setSelectedItem(item);
			}
		}
		
		if (shift.getSelectedItem() == null)
			shift.setSelectedIndex(0);
		
		this.username.select();
	}
	
	public void validate() throws InterruptedException, VONEAppException{
		
		if (!constraints.validateComponent(true))
			return;
		
		//check if user valid
		MD5 md5 = new MD5(password.getValue());
		String username = this.username.getValue();
		String password = md5.toString();
		
		if(username.equals(user.getVUserName()) && password.equals(user.getVPassword())){
			//validasi
						
			Object[] validasi = (Object[])session.getAttribute("validasi");
			Integer type = (Integer)validasi[2];
			if(type.intValue() == MedisafeConstants.NOTA){
				TbExamination nota = (TbExamination)validasi[0];
				Button btnValidation = (Button)validasi[3];
				Button btnModify = (Button)validasi[4];
				Button btnCancelNote = (Button)validasi[5];
				Label label = (Label)validasi[6];
				nota.setNExamStatus(new Integer(MedisafeConstants.VALIDATED_NOTE));
				nota.setDValidationDate(date.getValue());
				nota.setVWhoChange(username);
				Listitem li = shift.getSelectedItem();
//				Messagebox.show(((MsShift) li.getValue()).getVShiftCode());
				nota.setMsShift((MsShift) li.getValue());
				
				try {
					
					
					if(Service.getNotaManager().save(nota)){
						Messagebox.show(MessagesService.getKey("common.validate.success"));
						label.setValue(MedisafeUtil.getNoteStatus(nota.getNExamStatus().intValue()));
						btnCancelNote.setDisabled(true);
						btnValidation.setDisabled(true);
						btnModify.setDisabled(true);
						win.detach();
					}
					else{
						Messagebox.show(MessagesService.getKey("common.validate.fail"));
					}
				} catch (RuntimeException e) {
					// todo Auto-generated catch block
					e.printStackTrace();
				}
//				pc.validate(nota);
				
			}
		}
		else{
			Messagebox.show(MessagesService.getKey("common.password.not.match"));
			return;
		}
	}
	
	public static String getCurrentShift() throws VONEAppException{
	
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

}
