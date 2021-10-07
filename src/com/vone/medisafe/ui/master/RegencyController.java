package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.RegenyManager;
import com.vone.medisafe.ui.base.BaseController;

/**
 * RegencyController.java
 * @author Arifullah Ibn Rusyd
 * @version 0.3 -- last revised at 03-08-2006
 * @since 29-07-2006
 * used to controll all data flow from .zul file which used regency/kabupaten data
 */

public class RegencyController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	
	RegenyManager regencyService = MasterServiceLocator.getRegencyManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		super.doCancel(win);
		
		id.setValue(null);
		name.setValue(null);
		id.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		if(regencyList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.regency.regencylist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getRegencyManager().deleteById((Integer)regencyList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getRegencyDataList(regencyList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		if(regencyList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.regency.regencylist.notselected"));
			return;
		}
		super.doModify(win);
		id.setText(((Listcell)regencyList.getSelectedItem().getChildren().get(0)).getLabel());
		name.setText(((Listcell)regencyList.getSelectedItem().getChildren().get(1)).getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if (!constraints.validateComponent(true)) return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getRegencyManager().getRegencyByCode(id.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		MsRegency regency = new MsRegency();
		regency.setDWhnCreate(new Date());
		regency.setVRegencyId(id.getText());
		regency.setVRegencyName(name.getText());
		MasterServiceLocator.getRegencyManager().save(regency);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getRegencyDataList(regencyList);
		MiscTrxController.setFont(regencyList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		super.doSaveModify(win);
		
		MsRegency regency = new MsRegency();
		regency.setNRegencyId((Integer)regencyList.getSelectedItem().getValue());
		regency.setDWhnCreate(new Date());
		regency.setVRegencyId(id.getText());
		regency.setVRegencyName(name.getText());
		MasterServiceLocator.getRegencyManager().save(regency);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getRegencyDataList(regencyList);
		MiscTrxController.setFont(regencyList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Textbox crKodeKabupaten = (Textbox)win.getFellow("crKodeKabupaten"); 
		Textbox crNamaKabupaten = (Textbox)win.getFellow("crNamaKabupaten");
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		Listbox searchRegencyList = (Listbox)win.getFellow("searchRegencyList");
		super.init(win);
		
		constraints.registerComponent(id,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(id,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(crKodeKabupaten,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNamaKabupaten,ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		
		getRegencyDataList(regencyList);
		searchRegencyList.getItems().clear();
	}

	public void doSearch(Component win) throws InterruptedException{
		Textbox crKodeKabupaten = (Textbox)win.getFellow("crKodeKabupaten"); 
		Textbox crNamaKabupaten = (Textbox)win.getFellow("crNamaKabupaten");
		Listbox searchRegencyList = (Listbox)win.getFellow("searchRegencyList");
		searchRegencyList.getItems().clear();
		
		MsRegency regency = null;
		Listitem listitem = null;
		
		List result = MasterServiceLocator.getRegencyManager().searchRegency(crKodeKabupaten.getText(),crNamaKabupaten.getText());
		Iterator it = result.iterator();
		while(it.hasNext()){
			regency = (MsRegency)it.next();
	        
            listitem = new Listitem();
            listitem.setValue(regency.getNRegencyId());
            listitem.setParent(searchRegencyList);

            Listcell nameCell = new Listcell(regency.getVRegencyId());
            nameCell.setParent(listitem);
            Listcell surnameCell = new Listcell(regency.getVRegencyName());
            surnameCell.setParent(listitem);
		}
		MiscTrxController.setFont(searchRegencyList);
	}
	
	public void getRegencyData(Component win) throws InterruptedException{
		Listbox searchRegencyList = (Listbox)win.getFellow("searchRegencyList");
		Listbox regencyList = (Listbox)win.getFellow("regencyList");
		Textbox crKodeKabupaten = (Textbox)win.getFellow("crKodeKabupaten"); 
		Textbox crNamaKabupaten = (Textbox)win.getFellow("crNamaKabupaten");
		Tab tab1 = (Tab)win.getFellow("tab1");
		Tab tab2 = (Tab)win.getFellow("tab2");
		
		if(searchRegencyList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.regency.regencylist.notselected"));
			return;
		}
		regencyList.getItems().clear();
		Listitem item = searchRegencyList.getSelectedItem();
		item.setParent(regencyList);
		searchRegencyList.getItems().clear();
		crKodeKabupaten.setValue(null);
		crNamaKabupaten.setValue(null);
		
		tab2.setSelected(false);
		tab1.setSelected(true);
		
	}
	
	public void getRegencyDataList(Listbox regencyList) throws VONEAppException{
		
		regencyService.getRegencyData(regencyList);
		MiscTrxController.setFont(regencyList);
		
	}

	

}
