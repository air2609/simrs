package com.vone.medisafe.ui.master.item;

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
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.pojo.item.MsItemGroup;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class ItemGroupController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	private static final Logger log = Logger.getLogger(ItemGroupController.class);

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox itemGroupCode = (Textbox) win.getFellow("itemGroupCode");
		Textbox itemGroupName = (Textbox)win.getFellow("itemGroupName");
		super.doCancel(win);
		itemGroupCode.setValue(null);
		itemGroupName.setValue(null);
		itemGroupCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox itemGroupList = (Listbox)win.getFellow("itemGroupList");
		super.doDelete(win);
		if(itemGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.groupitem.groupitemlist.notselected"));
			return;
		}
		MsItemGroup itemGroup = (MsItemGroup)itemGroupList.getSelectedItem().getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = itemGroupList.getSelectedIndex();
		if(MasterServiceLocator.getItemGroupManager().delete(itemGroup)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			itemGroupList.removeItemAt(indexSelected);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox itemGroupCode = (Textbox) win.getFellow("itemGroupCode");
		Textbox itemGroupName = (Textbox)win.getFellow("itemGroupName");
		Listbox itemGroupList = (Listbox)win.getFellow("itemGroupList");
		super.doModify(win);
		if(itemGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.groupitem.groupitemlist.notselected"));
			return;
		}
		MsItemGroup itemGroup = (MsItemGroup)itemGroupList.getSelectedItem().getValue();
		itemGroupCode.setValue(itemGroup.getVItemGroupCode());
		itemGroupName.setValue(itemGroup.getVItemGroupName());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		log.debug("save in");
		Textbox itemGroupCode = (Textbox) win.getFellow("itemGroupCode");
		Textbox itemGroupName = (Textbox)win.getFellow("itemGroupName");
		Listbox itemGroupList = (Listbox)win.getFellow("itemGroupList");
		super.doSaveAdd(win);
		MsItemGroup itemGroup = new MsItemGroup();
		itemGroup.setVItemGroupCode(itemGroupCode.getText());
		itemGroup.setVItemGroupName(itemGroupName.getText());
		if(MasterServiceLocator.getItemGroupManager().save(itemGroup)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			Listitem item = new Listitem();
			item.setValue(itemGroup);
			
			Listcell cellCode = new Listcell(itemGroup.getVItemGroupCode());
			cellCode.setParent(item);
			Listcell cellName = new Listcell(itemGroup.getVItemGroupName());
			cellName.setParent(item);
			
			itemGroupList.appendChild(item);
			doCancel(win);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		log.debug("save out");
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox itemGroupCode = (Textbox) win.getFellow("itemGroupCode");
		Textbox itemGroupName = (Textbox)win.getFellow("itemGroupName");
		Listbox itemGroupList = (Listbox)win.getFellow("itemGroupList");
		super.doSaveModify(win);
		Listitem item = itemGroupList.getSelectedItem();
		MsItemGroup itemGroup = (MsItemGroup)item.getValue();
		itemGroup.setVItemGroupCode(itemGroupCode.getValue());
		itemGroup.setVItemGroupName(itemGroupName.getValue());
		if(MasterServiceLocator.getItemGroupManager().save(itemGroup)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			item.getChildren().clear();
			item.setValue(itemGroup);
			Listcell cellCode = new Listcell(itemGroup.getVItemGroupCode());
			cellCode.setParent(item);
			Listcell cellName = new Listcell(itemGroup.getVItemGroupName());
			cellName.setParent(item);
			doCancel(win);
		}else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		
		
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox itemGroupCode = (Textbox) win.getFellow("itemGroupCode");
		Textbox itemGroupName = (Textbox)win.getFellow("itemGroupName");
		Listbox itemGroupList = (Listbox)win.getFellow("itemGroupList");
		super.init(win);
		
		constraints.registerComponent(itemGroupCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(itemGroupCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(itemGroupName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(itemGroupName, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		
		getDataItemGroup(itemGroupList);
	}
	
	public void getDataItemGroup(Listbox itemGroupList) throws VONEAppException{
		itemGroupList.getItems().clear();
		Listitem item = null;
		Listcell cell = null;
		MsItemGroup itemGroup = null;
		List itemGroups = MasterServiceLocator.getItemGroupManager().getAllItemGroup();
		
		Iterator it = itemGroups.iterator();
		while(it.hasNext()){
				itemGroup = (MsItemGroup)it.next();
				item = new Listitem();
				item.setParent(itemGroupList);
				item.setValue(itemGroup);
			
				cell = new Listcell(itemGroup.getVItemGroupCode());
				cell.setParent(item);
			
				cell = new Listcell(itemGroup.getVItemGroupName());
				cell.setParent(item);
		}
		MiscTrxController.setFont(itemGroupList);
	}
	
	/**
	 * added at 29-09-2006
	 * @param itemGroupList
	 * @throws VONEAppException 
	 */
	
	public static void getItemGroupForSelect(Listbox itemGroupList) throws VONEAppException{
		itemGroupList.getItems().clear();
		Listitem item = null;
		MsItemGroup itemGroup = null;
		List itemGroups = MasterServiceLocator.getItemGroupManager().getAllItemGroup();
		
		Iterator it = itemGroups.iterator();
		while(it.hasNext()){
				itemGroup = (MsItemGroup)it.next();
				item = new Listitem();
				item.setParent(itemGroupList);
				item.setValue(itemGroup);
				item.setLabel(itemGroup.getVItemGroupName());
		}
		if(itemGroupList.getItems().size()>0)itemGroupList.setSelectedIndex(0);
	}

}
