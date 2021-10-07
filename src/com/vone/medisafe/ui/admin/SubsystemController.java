package com.vone.medisafe.ui.admin;

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
import com.vone.medisafe.mapping.MsSubsystem;
import com.vone.medisafe.service.iface.admin.SubsystemManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class SubsystemController extends BaseController{
	
	SubsystemManager sbyManager = ServiceLocator.getSubsystemManager();
	
	ZulConstraint cst = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		if (listBox.getSelectedItem() == null){

			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}		
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;
			
		super.doDelete(cmp);
				
		MsSubsystem screenPojo = sbyManager.getSubsystemById(((Integer)listBox.getSelectedItem().getValue()));
		
		sbyManager.delete(screenPojo);
		
		redrawList(cmp);
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));		
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub		

		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		if (listBox.getSelectedItem() == null){

			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}
		
		super.doModify(cmp);
		
		Textbox kodeSubsystem = (Textbox)cmp.getFellow("kodeSubsystem");
		Textbox namaSubsystem = (Textbox)cmp.getFellow("namaSubsystem");
	
		kodeSubsystem.select();
		
		Listitem listItem = listBox.getSelectedItem();
		
		//kodeSubsystem
		Listcell listCell = (Listcell) listItem.getChildren().get(0);
		kodeSubsystem.setText(listCell.getLabel());

		//desc
		listCell = (Listcell) listItem.getChildren().get(1);
		namaSubsystem.setText(listCell.getLabel());		
		
		kodeSubsystem.select();
		
		listBox.setDisabled(true);
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
		
		Textbox kodeSubsystem = (Textbox)cmp.getFellow("kodeSubsystem");
		Textbox namaSubsystem = (Textbox)cmp.getFellow("namaSubsystem");
		
		MsSubsystem sbyPojo = new MsSubsystem();
		sbyPojo.setVSubsystemCode(kodeSubsystem.getText());
		sbyPojo.setVDesc(namaSubsystem.getText());
		
		sbyManager.save(sbyPojo);	
		
		redrawList(cmp);
		
		Messagebox.show(MessagesService.getKey("common.add.success"));		
		
		kodeSubsystem.setText("");
		namaSubsystem.setText("");
		kodeSubsystem.focus();
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);			
		
		if (respond != Messagebox.YES)
			return;		
		
		super.doSaveModify(cmp);
		
		Textbox kodeSubsystem = (Textbox)cmp.getFellow("kodeSubsystem");
		Textbox namaSubsystem = (Textbox)cmp.getFellow("namaSubsystem");
		
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		listBox.setDisabled(false);
		
		Integer iSubsystemId = (Integer)listBox.getSelectedItem().getValue();
		
		MsSubsystem sbyPojo = sbyManager.getSubsystemById(iSubsystemId);
		
		sbyPojo.setVSubsystemCode(kodeSubsystem.getText());
		sbyPojo.setVDesc(namaSubsystem.getText());
		
		sbyManager.update(sbyPojo);
		
		doCancel(cmp);
		
		redrawList(cmp);
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));	
		
		kodeSubsystem.setText("");
		namaSubsystem.setText("");
		kodeSubsystem.focus();		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		
		Textbox kodeSubsystem = (Textbox)cmp.getFellow("kodeSubsystem");
		Textbox namaSubsystem = (Textbox)cmp.getFellow("namaSubsystem");
		
		kodeSubsystem.focus();
		
		cst.registerComponent(kodeSubsystem);
		cst.registerComponent(namaSubsystem);
		cst.registerComponent(kodeSubsystem, ZulConstraint.UPPER_CASE);
		cst.registerComponent(namaSubsystem, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		redrawList(cmp);
	}

	private void redrawList(Component cmp) throws VONEAppException {
		Listbox listBox = (Listbox)cmp.getFellow("listTable");
		
		listBox.getItems().clear();
		
		List listSby = sbyManager.getAllSubsystem(new MsSubsystem());
		
		Iterator it = listSby.iterator(); 
		
		while (it.hasNext()){
			
			Listitem listItem = new Listitem();
			MsSubsystem sbyPojo = (MsSubsystem)it.next();
			
			listItem.appendChild(new Listcell(sbyPojo.getVSubsystemCode()));
			listItem.appendChild(new Listcell(sbyPojo.getVDesc()));			
			
			listItem.setValue(sbyPojo.getNSubsystemId());
			
			listItem.setParent(listBox);
		}			
	}
}
