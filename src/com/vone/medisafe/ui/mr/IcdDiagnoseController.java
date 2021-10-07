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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.service.iface.master.diagnose.IcdManager;
import com.vone.medisafe.ui.base.BaseController;

public class IcdDiagnoseController extends BaseController{
	
	Textbox ICDCode;
	Textbox diseaseName;
	Listbox icdList;
	
	Button btnSave;
	Button btnEnd;
	
	Window win;
	
	Window winroot;
	Tab diagnoseTab;
	Tab codTab;
	
	Listitem item;
	Listcell cell;
	
	
	Listbox listbox;
	
	
	ZulConstraint constraints = new ZulConstraint();
	
	private IcdManager serv = DiagnoseService.getIcdManager();

	
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		
		super.init(win);
		
		ICDCode = (Textbox)win.getFellow("ICDCode");
		diseaseName = (Textbox)win.getFellow("diseaseName");
		icdList = (Listbox)win.getFellow("icdList");
		
		btnSave = (Button)win.getFellow("btnSave");
		btnEnd = (Button)win.getFellow("btnEnd");
		
		
		this.win = (Window)win;
		
		constraints.registerComponent(ICDCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(diseaseName,ZulConstraint.UPPER_CASE);
		
		
		
		
		ICDCode.focus();
		icdList.getItems().clear();
		

		
		Page page = win.getDesktop().getPage("diagnosePage");
		winroot = (Window)page.getFellow("diagnoseRoot");
		diagnoseTab = (Tab)winroot.getFellow("diagnoseTab");
		codTab = (Tab)winroot.getFellow("codTab");
		
		if(diagnoseTab.isSelected()){
			
			page = win.getDesktop().getPage("rmDiagnosePage");
			winroot = (Window)page.getFellow("rekamMedisDiagnosa");
			listbox = (Listbox)winroot.getFellow("diagnoseList");
			
		}
		else{
			page = win.getDesktop().getPage("codDiagnosePage");
			winroot = (Window)page.getFellow("sebabKematian");
			listbox = (Listbox)winroot.getFellow("diseaseList");
		}
		
		
	}
	
	
	
	public void getIcd() throws InterruptedException,VONEAppException{
		
		Listitem listitem;
		MsIcd icd;
		
		Iterator it = icdList.getSelectedItems().iterator();
		
		while(it.hasNext()){
			
			item = (Listitem)it.next();
			icd = (MsIcd)item.getValue();
			
			listitem = new Listitem();
			listitem.setParent(listbox);
			listitem.setValue(icd);
			
			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(listitem);
			
			cell = new Listcell("ICD");
			cell.setParent(listitem);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(listitem);
			
			listbox.clearSelection();
			
			this.win.detach();
		}
		
		
	}
	
	
	public void searchIcd() throws InterruptedException,VONEAppException{
		
		serv.serachIcdByCodeAndName(icdList, ICDCode,diseaseName);
			
	}


}
