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
import com.vone.medisafe.mapping.pojo.diagnose.MsCauseOfDeath;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.DiagnoseService;
import com.vone.medisafe.ui.base.BaseController;

public class CodController extends BaseController{
	private MsCauseOfDeath cod;
	
	Textbox causeOfDeadCode;
	Textbox description;
	Listbox codList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		causeOfDeadCode.setValue(null);
		description.setValue(null);
		codList.clearSelection();
		causeOfDeadCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = codList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.cod.notselected"));
			return;
		}
		cod = (MsCauseOfDeath)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = codList.getSelectedIndex();
		
		if(DiagnoseService.getCodManager().delete(cod)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			codList.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = codList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("master.diagnosa.cod.notselected"));
			return;
		}
		cod = (MsCauseOfDeath)item.getValue();
		
		causeOfDeadCode.setValue(cod.getVCodCode());
		description.setValue(cod.getVCodDesc());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		cod = new MsCauseOfDeath();
		cod.setVCodCode(causeOfDeadCode.getValue());
		cod.setVCodDesc(description.getValue());
		
		if(DiagnoseService.getCodManager().save(cod)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(codList);
			item.setValue(cod);
			
			cell = new Listcell(cod.getVCodCode());
			cell.setParent(item);
			
			cell = new Listcell(cod.getVCodDesc());
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
		item = codList.getSelectedItem();
		cod = (MsCauseOfDeath)item.getValue();
		cod.setVCodCode(causeOfDeadCode.getText());
		cod.setVCodDesc(description.getText());
		
		if(DiagnoseService.getCodManager().save(cod)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(cod);
			
			cell = new Listcell(cod.getVCodCode());
			cell.setParent(item);
			
			cell = new Listcell(cod.getVCodDesc());
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
		causeOfDeadCode = (Textbox)win.getFellow("causeOfDeadCode"); 
		description = (Textbox)win.getFellow("description");
		codList = (Listbox)win.getFellow("codList");
		
		super.init(win);
		
		constraints.registerComponent(causeOfDeadCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(causeOfDeadCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(description, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(description, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getCodData(codList);
		causeOfDeadCode.focus();
		
	}
	
	public void getCodData(Listbox icdList){
		icdList.getItems().clear();
		
		List list = DiagnoseService.getCodManager().getCods();
		Iterator it = list.iterator();
		while(it.hasNext()){
			cod = (MsCauseOfDeath)it.next();
			
			item = new Listitem();
			item.setParent(icdList);
			item.setValue(cod);
			
			cell = new Listcell(cod.getVCodCode());
			cell.setParent(item);
			
			cell = new Listcell(cod.getVCodDesc());
			cell.setParent(item);
		}
		MiscTrxController.setFont(icdList);
	}
	
}
