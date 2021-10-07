package com.vone.medisafe.ui.mr;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.AdmissionServiceLocator;
import com.vone.medisafe.service.iface.admission.MedicalRecordManager;
import com.vone.medisafe.ui.base.BaseController;

public class MRViewController extends BaseController{
	
	public Listbox mrFileStatueList;
	public Bandbox mrNumber;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	public Listbox mrList;
	public Listbox mrFileList;
	
	Listitem item;
	Listcell cell;
	
	
	ZulConstraint constraints = new ZulConstraint();
	
	MedicalRecordManager serv = AdmissionServiceLocator.getMedicalRecordManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		mrFileStatueList = (Listbox)win.getFellow("mrFileStatueList");
		mrNumber = (Bandbox)win.getFellow("mrNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		mrList = (Listbox)win.getFellow("mrList");
		mrFileList = (Listbox)win.getFellow("mrFileList");
		
		
		constraints.registerComponent(crNoMR, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNama, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat, ZulConstraint.UPPER_CASE);
		
		
	}
	
	
	public void viewMrStatus() throws VONEAppException{
		
		serv.viewMrStatus(mrFileStatueList,mrFileList);
		MiscTrxController.setFont(mrFileList);
		
		
	}
	
	
	
	
	
	public void getMrStatus(int type) throws VONEAppException, InterruptedException{
		
		serv.getMrStatus(this,type);
		MiscTrxController.setFont(mrFileList);
				
	}
	
	
	
}
