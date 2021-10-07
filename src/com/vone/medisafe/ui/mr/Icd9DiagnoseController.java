package com.vone.medisafe.ui.mr;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.service.iface.master.diagnose.Icd9Manager;
import com.vone.medisafe.ui.base.BaseController;

public class Icd9DiagnoseController extends BaseController{
	
	

	Textbox ICD9Code;
	Textbox treatmentName;
	Listbox icd9cmList;
	
	Button btnSave;
	Button btnEnd;
	
	Window win;
	
	Window winroot;
//	Tab diagnoseTab;
//	Tab codTab;
	
	Listitem item;
	Listcell cell;
	
	
	Listbox listbox;
	
	
	ZulConstraint constraints = new ZulConstraint();
	
	private Icd9Manager serv = DiagnoseService.getIcd9Manager();

	
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		
		super.init(win);
		
		ICD9Code = (Textbox)win.getFellow("ICD9Code");
		treatmentName = (Textbox)win.getFellow("treatmentName");
		icd9cmList = (Listbox)win.getFellow("icd9cmList");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnEnd = (Button)win.getFellow("btnEnd");
		
		
		this.win = (Window)win;
		
		constraints.registerComponent(ICD9Code, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(treatmentName,ZulConstraint.UPPER_CASE);
		
		
		
		
		ICD9Code.focus();
		icd9cmList.getItems().clear();
		

		
		Page page = win.getDesktop().getPage("rmDiagnosePage");
		winroot = (Window)page.getFellow("rekamMedisDiagnosa");
		listbox = (Listbox)winroot.getFellow("diagnoseList");
		
	}
	
	
	
	public void getIcd9() throws InterruptedException,VONEAppException{
		
		Listitem listitem;
		MsIcd9cm icd9;
		
		Iterator it = icd9cmList.getSelectedItems().iterator();
		
		while(it.hasNext()){
			
			item = (Listitem)it.next();
			
			icd9 = (MsIcd9cm)item.getValue();
			
			listitem = new Listitem();
			listitem.setParent(listbox);
			listitem.setValue(icd9);
			
			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(listitem);
			
			cell = new Listcell("ICD9CM");
			cell.setParent(listitem);
			
			cell = new Listcell(icd9.getVIcd9cmName());
			cell.setParent(listitem);
			
			listbox.clearSelection();
			
			this.win.detach();
		}
		
		
	}
	
	
	public void searchIcd9Cm() throws InterruptedException,VONEAppException{
		
		serv.serachIcd9ByCodeAndName(icd9cmList, ICD9Code,treatmentName);
			
	}

}
