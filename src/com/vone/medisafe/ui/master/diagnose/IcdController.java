package com.vone.medisafe.ui.master.diagnose;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.ui.base.BaseController;

public class IcdController extends BaseController{
	
	private MsIcd icd;
	
	Textbox ICDCode;
	Textbox diseaseName;
	Listbox listICD;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		ICDCode.setValue(null);
		diseaseName.setValue(null);
		listICD.clearSelection();
		ICDCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = listICD.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.icd.notselected"));
			return;
		}
		icd = (MsIcd)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = listICD.getSelectedIndex();
		
		if(DiagnoseService.getIcdManager().delete(icd)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			listICD.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = listICD.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.icd.notselected"));
			return;
		}
		icd = (MsIcd)item.getValue();
		
		ICDCode.setValue(icd.getVIcdCode());
		diseaseName.setValue(icd.getVIcdName());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		icd = new MsIcd();
		icd.setVIcdCode(ICDCode.getText());
		icd.setVIcdName(diseaseName.getText());
		
		if(DiagnoseService.getIcdManager().save(icd)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(listICD);
			item.setValue(icd);
			
			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(item);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		item = listICD.getSelectedItem();
		icd = (MsIcd)item.getValue();
		icd.setVIcdCode(ICDCode.getText());
		icd.setVIcdName(diseaseName.getText());
		
		if(DiagnoseService.getIcdManager().save(icd)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(icd);
			
			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(item);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		doCancel(cmp);
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		ICDCode = (Textbox)win.getFellow("ICDCode"); 
		diseaseName = (Textbox)win.getFellow("diseaseName");
		listICD = (Listbox)win.getFellow("listICD");
		
		super.init(win);
		
		constraints.registerComponent(ICDCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(ICDCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(diseaseName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(diseaseName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getIcdData(listICD);
		ICDCode.focus();
		
	}
	
	public void initSearch(Component win) throws InterruptedException, VONEAppException {
		ICDCode = (Textbox)win.getFellow("ICDCode"); 
		diseaseName = (Textbox)win.getFellow("diseaseName");
		listICD = (Listbox)win.getFellow("listICD");
		
		super.init(win);
		
		constraints.registerComponent(ICDCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(diseaseName, ZulConstraint.UPPER_CASE);
		listICD.getItems().clear();
		ICDCode.focus();
		
	}
	
	public void getIcdData(Listbox icdList){
		icdList.getItems().clear();
		
		List list = DiagnoseService.getIcdManager().getIcds();
		Iterator it = list.iterator();
		while(it.hasNext()){
			icd = (MsIcd)it.next();
			
			item = new Listitem();
			item.setParent(icdList);
			item.setValue(icd);
			
			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(item);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(item);
		}
		MiscTrxController.setFont(icdList);
	}
	
	public void searchIcd() throws VONEAppException{
		listICD.getItems().clear();
		
		icd = new MsIcd();
		icd.setVIcdCode("%"+ICDCode.getText()+"%");
		icd.setVIcdName("%"+diseaseName.getText()+"%");
		List list = DiagnoseService.getIcdManager().serachIcds(icd);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			icd = (MsIcd)it.next();
			
			item = new Listitem();
			item.setParent(listICD);
			item.setValue(icd);
			
			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(item);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(item);
		}
		
	}

}
