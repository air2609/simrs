package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;

public class WarehouseController extends BaseController{
	
	Logger logger = Logger.getLogger(WarehouseController.class);
	
	ZulConstraint cst = new ZulConstraint();	
	
	public Listbox list = null;
	
	public Textbox code = null;
	public Textbox name = null;
	public Textbox location = null;
	public Listbox superior = null;
	public Bandbox coa = null;
	
	WarehouseManager warehouseManager = MasterServiceLocator.getWarehouseManager();
			

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		this.code.setText("");
		this.name.setText("");
		this.location.setText("");
		this.superior.setSelectedIndex(0);
		this.coa.setText("");
		this.coa.removeAttribute("coa");
		
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
		
		warehouseManager.delete((MsWarehouse)this.list.getSelectedItem().getValue());
		
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
		
		warehouseManager.prepareModify(this);
		
		
		
		
		
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
		
		MsWarehouse pojo = new MsWarehouse();
		
		pojo.setVWhouseCode(this.code.getText());
		pojo.setVWhouseName(this.name.getText());
		pojo.setVWhouseLoc(this.location.getText());
		if (!MedisafeConstants.LISTKOSONG.equals(this.superior.getSelectedItem().getValue()))
			pojo.setMsWarehouse((MsWarehouse)this.superior.getSelectedItem().getValue());
		else
			pojo.setMsWarehouse(null);
		
		pojo.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		
		pojo.setVWhoCreate(super.getUserInfoBean().getStUserId());
		pojo.setDWhnCreate(new Date());
		
		warehouseManager.save(pojo);
		
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
		
		MsWarehouse pojo = (MsWarehouse)this.list.getSelectedItem().getValue();
		
		pojo.setVWhouseCode(this.code.getText());
		pojo.setVWhouseName(this.name.getText());
		pojo.setVWhouseLoc(this.location.getText());
		if (!MedisafeConstants.LISTKOSONG.equals(this.superior.getSelectedItem().getValue()))
			pojo.setMsWarehouse((MsWarehouse)this.superior.getSelectedItem().getValue());
		else
			pojo.setMsWarehouse(null);
		
		pojo.setMsCoa((MsCoa)coa.getAttribute("coa"));
		
		pojo.setVWhoChange(super.getUserInfoBean().getStUserId());
		pojo.setDWhnChange(new Date());		
		
		warehouseManager.update(pojo);
		
		redrawList();
		
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		
		doCancel(cmp);
		
	}

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);			
		
		list = (Listbox)cmp.getFellow("list");
		code = (Textbox)cmp.getFellow("code");
		name = (Textbox)cmp.getFellow("name");
		location = (Textbox)cmp.getFellow("location");
		superior = (Listbox)cmp.getFellow("superior");
		coa = (Bandbox)cmp.getFellow("coa");		
		getWarehouseList(this.superior);
//		CoaController.getCoaForSelect(coa, MedisafeConstants.COA_ALL);
		
		cst.registerComponent(code);
		cst.registerComponent(name);
		cst.registerComponent(code,ZulConstraint.UPPER_CASE);
		cst.registerComponent(name,ZulConstraint.UPPER_CASE);
		cst.registerComponent(location,ZulConstraint.UPPER_CASE);
		
		cst.validateComponent(false);
		
		redrawList();
	}

	private void redrawList() throws VONEAppException{
		
		warehouseManager.redrawList(list);
		getWarehouseList(this.superior);
		MiscTrxController.setFont(list);
//		list.getItems().clear();
//				
//		List listWarehouse = warehouseManager.findAll();
//		
//		Iterator it = listWarehouse.iterator();
//		
//		getWarehouseList(this.superior);		
//		
//		while (it.hasNext()){
//			Listitem item = new Listitem();
//			
//			MsWarehouse warehousePojo = (MsWarehouse)it.next();
//			
//			item.appendChild(new Listcell(warehousePojo.getVWhouseCode()));					
//			item.appendChild(new Listcell(warehousePojo.getVWhouseName()));
//			item.appendChild(new Listcell(warehousePojo.getVWhouseLoc()));
//			if (warehousePojo.getMsWarehouse() != null)
//				item.appendChild(new Listcell(warehousePojo.getMsWarehouse().getVWhouseName()));
//			if (warehousePojo.getMsCoa() != null)
//				item.appendChild(new Listcell(warehousePojo.getMsCoa().getVAcctNo()+"-"+warehousePojo.getMsCoa().getVAcctName()));
//			else
//				item.appendChild(new Listcell());
//			
//			item.setValue(warehousePojo);
//			
//			item.setParent(list);
//		}
	}		
	
	public static void getWarehouseList(Listbox listbox) throws VONEAppException {
		
		if (listbox == null)
			listbox = new Listbox();
		else
			listbox.getItems().clear();		
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(listbox);	
		
		WarehouseManager service = MasterServiceLocator.getWarehouseManager();
		
		List listWarehouse = service.findAll();
		
		Iterator it = listWarehouse.iterator();		
		
		while (it.hasNext()){
			item = new Listitem();
			
			MsWarehouse warehousePojo = (MsWarehouse)it.next();
			
			item.setLabel(warehousePojo.getVWhouseCode()+" - "+warehousePojo.getVWhouseName());
			item.setValue(warehousePojo);			
			
			item.setParent(listbox);
		}		
		
		listbox.setSelectedIndex(0);				
	}
}
