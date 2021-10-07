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
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsShift;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.ShiftManager;
import com.vone.medisafe.ui.base.BaseController;

public class ShiftController extends BaseController{
	
	Logger logger = Logger.getLogger(ShiftController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	Listbox list = null;
	
	Textbox code = null;
	Textbox from = null;
	Textbox to = null;
	
	ShiftManager shiftManager = MasterServiceLocator.getShiftManager();
			

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		this.code.setText("");
		this.from.setText("");
		this.to.setText("");
		
		this.code.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		
		if (list.getSelectedCount() < 1){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;				
		
		shiftManager.delete((MsShift)this.list.getSelectedItem().getValue());
		
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
		
		MsShift pojo = (MsShift)this.list.getSelectedItem().getValue();
		
		this.code.setText(pojo.getVShiftCode());
		this.from.setText(MedisafeUtil.convertDateToTime(pojo.getDShiftFrom()));
		this.to.setText(MedisafeUtil.convertDateToTime(pojo.getDShiftTo()));
			
		this.code.select();
		
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
		
		MsShift pojo = new MsShift();
		
		pojo.setVShiftCode(this.code.getText());
		pojo.setDShiftFrom(MedisafeUtil.convertTimeToDate(this.from.getText()));
		pojo.setDShiftTo(MedisafeUtil.convertTimeToDate(this.to.getText()));
				
		pojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		pojo.setDWhnCreate(new Date());
		
		shiftManager.save(pojo);
		
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
		
		MsShift pojo = (MsShift)this.list.getSelectedItem().getValue();		
		
		pojo.setVShiftCode(this.code.getText());
		pojo.setDShiftFrom(MedisafeUtil.convertTimeToDate(this.from.getText()));
		pojo.setDShiftTo(MedisafeUtil.convertTimeToDate(this.to.getText()));
		
		pojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		pojo.setDWhnChange(new Date());		
		
		shiftManager.update(pojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		doCancel(cmp);
		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);			
		
		list = (Listbox)cmp.getFellow("list");
		code = (Textbox)cmp.getFellow("code");
		from = (Textbox)cmp.getFellow("from");
		to = (Textbox)cmp.getFellow("to");				
		
		cst.registerComponent(code);
		cst.registerComponent(from);
		cst.registerComponent(to);
		cst.registerComponent(code,ZulConstraint.UPPER_CASE);
		cst.registerComponent(from,ZulConstraint.TIME_CONSTRAINT);
		cst.registerComponent(to,ZulConstraint.TIME_CONSTRAINT);
		
		cst.validateComponent(false);
		
		redrawList();
	}

	private void redrawList() throws VONEAppException{
		
		list.getItems().clear();
				
		List listShift = shiftManager.findAll();
		
		Iterator it = listShift.iterator();		
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsShift pojo = (MsShift)it.next();
			
			item.appendChild(new Listcell(pojo.getVShiftCode()));					
			item.appendChild(new Listcell(MedisafeUtil.convertDateToTime(pojo.getDShiftFrom())));
			item.appendChild(new Listcell(MedisafeUtil.convertDateToTime(pojo.getDShiftTo())));
			
			item.setValue(pojo);
			
			item.setParent(list);
		}
		MiscTrxController.setFont(list);
	}		
	
}
