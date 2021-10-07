package com.vone.medisafe.ui.master.item;

import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;


public class ItemInventoryPicker extends BaseController{	
	
	public Textbox code = null;
	public Textbox batch = null;
	public Intbox qty = null;
	public Listbox list = null;
	int qtyGlobal = 1;
	StringBuffer stMsg = new StringBuffer();
	
	ItemManager itemManager = MasterServiceLocator.getItemManager(); 
	
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		
		this.code = (Textbox)win.getFellow("code");
		this.batch = (Textbox)win.getFellow("batch");
		this.qty = (Intbox)win.getFellow("qty");
		this.list = (Listbox)win.getFellow("list");
		
		list.getItems().clear();
		
		code.focus();
	}			
	
	public void add() throws InterruptedException, VONEAppException {
		
		itemManager.searchItem(this);
		
//		if (!StringUtils.hasValue(this.code.getText()) && !StringUtils.hasValue(this.batch.getText())){
//			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
//			
//			this.code.select();
//			
//			return;
//		}
//
//		this.list.getItems().clear();		
//		
//		List listItem = itemManager.searchItem(this.code.getText(), this.batch.getText());
//		
//		Iterator it = listItem.iterator();
//		
//		qtyGlobal = 1;
//		
//		if (this.qty.getValue() != null && this.qty.getValue() > 1)
//			qtyGlobal = this.qty.intValue();
//		
//		while (it.hasNext()){
//			MsItem pojo = (MsItem)it.next();
//			
//			Listitem item = new Listitem();
//			item.setParent(this.list);
//			item.setValue(pojo);
//			
//			item.appendChild(new Listcell(pojo.getVItemCode()));
//			item.appendChild(new Listcell(pojo.getVItemName()));
//			item.appendChild(new Listcell(pojo.getMsItemMeasurement().getVMitemEndQuantify()));
//			item.appendChild(new Listcell(""+qtyGlobal));
//		}
	}
	
	public void doSave() throws InterruptedException, VONEAppException {
			
//		Session session = Sessions.getCurrent();
		
//		UnitInventoryController ctr = (UnitInventoryController)session.getAttribute(MedisafeConstants.ITEM_INVENTORY_SESSION);			
		
//		List currentItem = ctr.getList().getItems();
		
		Set set = list.getSelectedItems();
		
		Iterator it = set.iterator();
		
		//check for duplicate entries
		loop:
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
			MsItem itemPojo = (MsItem)item.getValue();
						
			//Save
			stMsg.setLength(0);
			stMsg.append(MessagesService.getKey("master.iteminventory.manipulate.warning"))
			.append(" "+MessagesService.getKey("master.iteminventory.save.confirm")+" "+itemPojo.getVItemCode())
			.append(" "+MessagesService.getKey("master.iteminventory.save.confirm.qty")+" "+qtyGlobal);

			int respond = 0;
			
			respond = Messagebox.show(stMsg.toString(), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			
			if (respond != Messagebox.YES)
				continue loop;				
			
			//common ADD
//			ctr.addItem(itemPojo, qtyGlobal);
		}
				
	}
}
