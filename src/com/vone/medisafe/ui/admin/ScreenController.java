package com.vone.medisafe.ui.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.mapping.MsScreenInUnit;
import com.vone.medisafe.mapping.MsScreenInUnitId;
import com.vone.medisafe.mapping.MsSubsystem;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.service.iface.admin.SubsystemManager;
import com.vone.medisafe.service.iface.master.UnitManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class ScreenController extends BaseController{

	ScreenManager screenManager = ServiceLocator.getScreenManager();
	SubsystemManager subsystemManager = ServiceLocator.getSubsystemManager();
	UnitManager unitManager = MasterServiceLocator.getUnitManager();
	ZulConstraint constraint = new ZulConstraint();
	
	Textbox kodeScreen;
	Textbox namaScreen;
	Listbox subsystem;
	Listbox unitList;
	Listbox list;
	Textbox cariTextbox;
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		
		kodeScreen = (Textbox)win.getFellow("kodeScreen");
		namaScreen = (Textbox)win.getFellow("namaScreen");
		subsystem = (Listbox)win.getFellow("subsystem");	
		unitList = (Listbox)win.getFellow("unitList");
		list = (Listbox)win.getFellow("listScreen");
		
		//search component
		cariTextbox = (Textbox)win.getFellow("cariTextbox");
		
		//register component		
		constraint.registerComponent(kodeScreen);
		constraint.registerComponent(namaScreen);				
		constraint.registerComponent(subsystem);
		constraint.registerComponent(kodeScreen, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(namaScreen, ZulConstraint.UPPER_CASE);
		constraint.registerComponent(cariTextbox, ZulConstraint.UPPER_CASE);
		
		constraint.validateComponent(false);
		
		//set Dropdown Subsystem
		Listbox listSubsystem = (Listbox)win.getFellow("subsystem");
		
		listSubsystem.getItems().clear();
		
		List listSubsystemPojo = subsystemManager.getAllSubsystem(new MsSubsystem());
		
		for (int i=0; i<listSubsystemPojo.size(); i++){
			MsSubsystem subsystemPojo = (MsSubsystem)listSubsystemPojo.get(i);
			
			Listitem subsystemItem = new Listitem();
			subsystemItem.setLabel(subsystemPojo.getVSubsystemCode()+" - "+subsystemPojo.getVDesc());
			subsystemItem.setValue(subsystemPojo);		
			
			subsystemItem.setParent(listSubsystem);
			
			if (i == 0)
				listSubsystem.setSelectedItem(subsystemItem);			
		}			
		
		//set List Screen
		redrawListScreen(win);
		MiscTrxController.setFont(unitList);
		MiscTrxController.setFont(list);
	}
	
	public void redrawListScreenByCriteria(ScreenController ctr) throws VONEAppException {
		
		screenManager.redrawListScreenByCriteria(ctr);
		
		this.cariTextbox.select();
	}
	
	public void doSave(Component win) throws InterruptedException, VONEAppException {
	
		if (!constraint.validateComponent(true)) return;		
		
		super.doSave(win);
		
	}
	
	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {			
		
		super.doSaveAdd(win);
		
		MsScreen screenPojo = new MsScreen();
		screenPojo.setVScreenCode(kodeScreen.getText());
		screenPojo.setVDesc(namaScreen.getText());
		
		screenPojo.setMsSubsystem((MsSubsystem)this.subsystem.getSelectedItem().getValue());

		//set Unit Pojo in Screen Pojo
		Iterator it = unitList.getSelectedItems().iterator();
		Set set = new HashSet();
		while (it.hasNext()){
			MsScreenInUnit siuPojo = new MsScreenInUnit();
			set.add(siuPojo);
			
			MsScreenInUnitId siuiPojo = new MsScreenInUnitId();
			siuPojo.setId(siuiPojo);
			
			siuiPojo.setMsScreen(screenPojo);
			Listitem item = (Listitem)it.next();
			siuiPojo.setMsUnit((MsUnit)item.getValue());
		}
		
		screenPojo.setMsScreenInUnits(set);
		
		screenManager.save(screenPojo);
		
		//redraw list
		Listitem item = new Listitem();
		item.setValue(screenPojo);
		item.setParent(list);
		
		item.appendChild(new Listcell(this.kodeScreen.getText()));
		item.appendChild(new Listcell(this.namaScreen.getText()));
		MsSubsystem sPojo = (MsSubsystem)this.subsystem.getSelectedItem().getValue();	
		item.appendChild(new Listcell(sPojo.getVDesc()));
		
		//end of redraw list
		
		Messagebox.show(MessagesService.getKey("common.add.success"));
		
		kodeScreen.focus();
		MiscTrxController.setFont(list);
		doCancel(win);
	}
	
	public void doDelete(Component win) throws InterruptedException, VONEAppException {
					
		if (list.getSelectedItem() == null){

			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}		
		
		int respond = 0;
		
		respond = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if (respond != Messagebox.YES)
			return;
			
		super.doDelete(win);
					
		screenManager.deleteScreen((MsScreen)list.getSelectedItem().getValue());
		
		//redraw list
		Listitem item = list.getSelectedItem();

		list.removeChild(item);
		//end of redraw list
		
		Messagebox.show(MessagesService.getKey("common.delete.success"));
		MiscTrxController.setFont(list);
	}
	
	public void doModify(Component win) throws InterruptedException, VONEAppException {		
		
		if (list.getSelectedItem() == null){

			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			
			return;
		}
		
		super.doModify(win);
		
		screenManager.doModify(this);
	}
	
	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {				

		int respond = 0;
				
		respond = Messagebox.show(MessagesService.getKey("common.modify.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);			
		
		if (respond != Messagebox.YES)
			return;
		
		super.doSaveModify(win);		

		list.setDisabled(false);
		Listitem item = this.list.getSelectedItem();
		MsScreen screenPojo = (MsScreen)item.getValue();
		
		//fill Pojos
		screenPojo.setVScreenCode(this.kodeScreen.getText());
		screenPojo.setVDesc(this.namaScreen.getText());
		screenPojo.setMsSubsystem((MsSubsystem)this.subsystem.getSelectedItem().getValue());
		
		//set Unit Pojo in Screen Pojo
		Iterator it = unitList.getSelectedItems().iterator();
		Set set = new HashSet();
		while (it.hasNext()){
			MsScreenInUnit siuPojo = new MsScreenInUnit();
			set.add(siuPojo);
			
			MsScreenInUnitId siuiPojo = new MsScreenInUnitId();
			siuPojo.setId(siuiPojo);
			
			siuiPojo.setMsScreen(screenPojo);
			Listitem itemUnit = (Listitem)it.next();
			siuiPojo.setMsUnit((MsUnit)itemUnit.getValue());
		}			
		
		screenPojo.setMsScreenInUnits(set);
				
		screenManager.update((MsScreen)this.list.getSelectedItem().getValue());		
		
		this.kodeScreen.getText();
		
		doCancel(win);
		
		//redraw list
		item.getChildren().clear();
		item.appendChild(new Listcell(screenPojo.getVScreenCode()));
		item.appendChild(new Listcell(screenPojo.getVDesc()));		
		item.appendChild(new Listcell(screenPojo.getMsSubsystem().getVDesc()));
		
		//end of redraw list
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));	
		MiscTrxController.setFont(list);
		doCancel(win);
	}	
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException{

		super.doCancel(win);
		
		kodeScreen.setText("");
		namaScreen.setText("");
		kodeScreen.focus();
		
		if (subsystem.getItemCount() > 0)
			subsystem.setSelectedIndex(0);		
		
		
		unitList.clearSelection();
	}
	
	public void redrawListScreen(Component win)  throws VONEAppException{		
		
		screenManager.redrawListScreen(this);
	}

	public Textbox getCariTextbox() {
		return cariTextbox;
	}

	public void setCariTextbox(Textbox cariTextbox) {
		this.cariTextbox = cariTextbox;
	}

	public Textbox getKodeScreen() {
		return kodeScreen;
	}

	public void setKodeScreen(Textbox kodeScreen) {
		this.kodeScreen = kodeScreen;
	}

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}

	public Textbox getNamaScreen() {
		return namaScreen;
	}

	public void setNamaScreen(Textbox namaScreen) {
		this.namaScreen = namaScreen;
	}

	public Listbox getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(Listbox subsystem) {
		this.subsystem = subsystem;
	}

	public Listbox getUnitList() {
		return unitList;
	}

	public void setUnitList(Listbox unitList) {
		this.unitList = unitList;
	}
		
}
