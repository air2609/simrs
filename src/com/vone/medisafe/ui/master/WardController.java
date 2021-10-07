package com.vone.medisafe.ui.master;

import java.util.Date;
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
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsWard;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;


public class WardController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		super.doCancel(win);
		kode.setValue(null);
		nama.setValue(null);
		kode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox wardList = (Listbox)win.getFellow("wardList");
		if(wardList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.ward.wardlist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getWardManager().deleteById((Integer)wardList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getWardDataList(wardList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		super.doModify(win);
		if(wardList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.ward.wardlist.notselected"));
			return;
		}
		Listitem item = wardList.getSelectedItem();
		Listcell cellCode = (Listcell)item.getChildren().get(0);
		Listcell cellName = (Listcell)item.getChildren().get(1);
		kode.setValue(cellCode.getLabel());
		nama.setValue(cellName.getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getWardManager().getWardByCode(kode.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		MsWard ward = new MsWard();
		ward.setDWhnCreate(new Date());
		ward.setVWardCode(kode.getText());
		ward.setVWardName(nama.getText());
		MasterServiceLocator.getWardManager().save(ward);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getWardDataList(wardList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		
		super.doSaveModify(win);
		MsWard ward = new MsWard();
		ward.setNWardId((Integer)wardList.getSelectedItem().getValue());
		ward.setVWardCode(kode.getText());
		ward.setVWardName(nama.getText());
		MasterServiceLocator.getWardManager().save(ward);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getWardDataList(wardList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		super.init(win);
		
		constraints.registerComponent(kode,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(nama,ZulConstraint.NO_EMPTY);
		
		constraints.registerComponent(kode,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(nama,ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		getWardDataList(wardList);
	}
	
	
	public static void getWardDataList(Listbox wardList){
			
			wardList.getItems().clear();
			MsWard ward = null;
			
			Listitem item;
			List list = MasterServiceLocator.getWardManager().getAllWard();
			Iterator it = list.iterator();
			while(it.hasNext()){
				ward = (MsWard)it.next();
				item = new Listitem();
				item.setValue(ward.getNWardId());
				item.setParent(wardList);
				Listcell id = new Listcell(ward.getVWardCode());
				id.setParent(item);
				Listcell name = new Listcell(ward.getVWardName());
				name.setParent(item);
			}
			MiscTrxController.setFont(wardList);
			
	}	

	public static void getWardList(Listbox wardList){
		MsWard ward;
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(wardList);
				
		List list = MasterServiceLocator.getWardManager().getAllWard();
		Iterator it = list.iterator();
		while(it.hasNext()){
			ward = (MsWard)it.next();
			item = new Listitem();
			item.setValue(ward.getNWardId());
			item.setLabel(ward.getVWardName());
			item.setParent(wardList);
			
		}
		if(wardList.getItems().size() > 0)wardList.setSelectedIndex(0);
		MiscTrxController.setFont(wardList);	
	}
	
}
