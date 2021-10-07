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
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.ui.base.BaseController;

public class Icd9Controller extends BaseController{

	private MsIcd9cm icd9;
	
	Textbox ICD9CMCode;
	Textbox treatmentName;
	Listbox listICD9;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		ICD9CMCode.setValue(null);
		treatmentName.setValue(null);
		listICD9.clearSelection();
		ICD9CMCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = listICD9.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.icd9.notselected"));
			return;
		}
		icd9 = (MsIcd9cm)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = listICD9.getSelectedIndex();
		
		if(DiagnoseService.getIcd9Manager().delete(icd9)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			listICD9.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = listICD9.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.icd9.notselected"));
			return;
		}
		icd9 = (MsIcd9cm)item.getValue();
		
		ICD9CMCode.setValue(icd9.getVIcd9cmCode());
		treatmentName.setValue(icd9.getVIcd9cmName());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		icd9 = new MsIcd9cm();
		icd9.setVIcd9cmCode(ICD9CMCode.getText());
		icd9.setVIcd9cmName(treatmentName.getText());
		
		if(DiagnoseService.getIcd9Manager().save(icd9)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(listICD9);
			item.setValue(icd9);
			
			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(item);
			
			cell = new Listcell(icd9.getVIcd9cmName());
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
		item = listICD9.getSelectedItem();
		icd9 = (MsIcd9cm)item.getValue();
		icd9.setVIcd9cmCode(ICD9CMCode.getText());
		icd9.setVIcd9cmName(treatmentName.getText());
		
		if(DiagnoseService.getIcd9Manager().save(icd9)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(icd9);
			
			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(item);
			
			cell = new Listcell(icd9.getVIcd9cmName());
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
		ICD9CMCode = (Textbox)win.getFellow("ICD9CMCode"); 
		treatmentName = (Textbox)win.getFellow("treatmentName");
		listICD9 = (Listbox)win.getFellow("listICD9");
		
		super.init(win);
		
		constraints.registerComponent(ICD9CMCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(ICD9CMCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(treatmentName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getIcdData(listICD9);
		ICD9CMCode.focus();
		
	}
	
	public void initSearch(Component win) throws InterruptedException, VONEAppException {
		ICD9CMCode = (Textbox)win.getFellow("ICD9CMCode"); 
		treatmentName = (Textbox)win.getFellow("treatmentName");
		listICD9 = (Listbox)win.getFellow("listICD9");
		
		super.init(win);
		
		constraints.registerComponent(ICD9CMCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(treatmentName, ZulConstraint.UPPER_CASE);
		listICD9.getItems().clear();
		ICD9CMCode.focus();
		
	}
	
	public void getIcdData(Listbox icdList) throws VONEAppException{
		icdList.getItems().clear();
		
		List list = DiagnoseService.getIcd9Manager().getIcd9s();
		Iterator it = list.iterator();
		while(it.hasNext()){
			icd9 = (MsIcd9cm)it.next();
			
			item = new Listitem();
			item.setParent(icdList);
			item.setValue(icd9);
			
			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(item);
			
			cell = new Listcell(icd9.getVIcd9cmName());
			cell.setParent(item);
		}
		MiscTrxController.setFont(icdList);
	}
	
	public void searchIcd9() throws VONEAppException{
		listICD9.getItems().clear();
		
		icd9 = new MsIcd9cm();
		icd9.setVIcd9cmCode("%"+ICD9CMCode.getText()+"%");
		icd9.setVIcd9cmName("%"+treatmentName.getText()+"%");
		List list = DiagnoseService.getIcd9Manager().serachIcd9s(icd9);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			icd9 = (MsIcd9cm)it.next();
			
			item = new Listitem();
			item.setParent(listICD9);
			item.setValue(icd9);
			
			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(item);
			
			cell = new Listcell(icd9.getVIcd9cmName());
			cell.setParent(item);
		}
		
	}

}
