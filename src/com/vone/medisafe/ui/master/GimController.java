package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsGim;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.BankManager;
import com.vone.medisafe.service.iface.master.GimManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class GimController extends BaseController{
	
	Logger logger = Logger.getLogger(GimController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	Textbox key = null;
	Textbox value = null;	
	Listbox list = null;
	
	GimManager gimManager = MasterServiceLocator.getGimManager();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		this.key.setText("");
		this.value.setText("");		
		this.key.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doDelete(cmp);
				
		gimManager.delete((MsGim)list.getSelectedItem().getValue());
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		super.doModify(cmp);
		
		MsGim gimPojo = (MsGim)list.getSelectedItem().getValue();
		
		this.key.setText(gimPojo.getVKey());
		this.value.setText(gimPojo.getVValue());
		
		this.key.select();
		
		list.setDisabled(true);
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!cst.validateComponent(true))
			return;
		
		super.doSave(cmp);
				
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		
		MsGim gimPojo = new MsGim();
		gimPojo.setVKey(this.key.getText());
		gimPojo.setVValue(this.value.getText());
		
		gimPojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		gimPojo.setDWhnCreate(new Date());
		
		gimManager.save(gimPojo);
		
		redrawList();		
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		super.doSaveModify(cmp);
		
		list.setDisabled(false);	
		
		MsGim gimPojo = (MsGim)list.getSelectedItem().getValue();
		gimPojo.setVKey(this.key.getText());
		gimPojo.setVValue(this.value.getText());
		
		gimPojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		gimPojo.setDWhnChange(new Date());
		
		gimManager.update(gimPojo);

		Messagebox.show(MessagesService.getKey("common.modify.success"));		
		
		redrawList();
		
		doCancel(cmp);			
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);		
		
		key = (Textbox)cmp.getFellow("key");
		value = (Textbox)cmp.getFellow("value");
		list = (Listbox)cmp.getFellow("list");
		
		cst.registerComponent(key,ZulConstraint.NO_EMPTY);
		cst.registerComponent(value,ZulConstraint.NO_EMPTY);
		cst.registerComponent(key,ZulConstraint.UPPER_CASE);
		cst.registerComponent(value, ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		redrawList();
	}
	
	private void redrawList() throws VONEAppException{
		
		list.getItems().clear();
				
		List listBank = gimManager.findByExample(new MsGim());
		
		Iterator it = listBank.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsGim gimPojo = (MsGim)it.next();
			
			item.appendChild(new Listcell(gimPojo.getVKey()));			
			item.appendChild(new Listcell(gimPojo.getVValue()));			
			
			item.setValue(gimPojo);
			
			item.setParent(list);
		}
	}	

}
