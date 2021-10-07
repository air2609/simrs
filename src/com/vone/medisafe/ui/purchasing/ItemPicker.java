package com.vone.medisafe.ui.purchasing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;


public class ItemPicker extends BaseController{	
	
	ZulConstraint cst = new ZulConstraint();
	
	Textbox code = null;
	Textbox name = null;
	Listbox list = null;
	
	ItemManager itemManager = MasterServiceLocator.getItemManager(); 
	
	Window winParent = null;
	
	Window winCurrent = null;

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
		this.name = (Textbox)win.getFellow("name");
		this.list = (Listbox)win.getFellow("list");
		
		this.winCurrent = (Window)win;
		
		cst.registerComponent(code, ZulConstraint.UPPER_CASE);
		cst.registerComponent(name, ZulConstraint.UPPER_CASE);
		cst.validateComponent(false);
		
		winParent = (Window)win.getParent();
		
		list.getItems().clear();
		
		code.focus();
	}			
	
	public void search() throws VONEAppException, InterruptedException {
		
		if (!StringUtils.hasValue(this.code.getText()) && !StringUtils.hasValue(this.name.getText())){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			this.code.focus();
			return;
		}
			
		
		//KEEP SELECTED ITEM
		Listitem itemKept;		
		Set treatments = list.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		list.getItems().clear();
				
		for(int i=0; i < listitem.length; i++){
			itemKept = listitem[i];
			itemKept.setParent(list);
		}
		
		//END OF KEEP SELECTED ITEM
		
//		List listItem = itemManager.searchItem(this.code.getText(), this.name.getText());
		List listItem = itemManager.searchItemUnderBuffer(this.code.getText(), this.name.getText());
		
		int jml = listItem.size();
		
		it = listItem.iterator();
		
		while (it.hasNext()){
//			MsItem pojo = (MsItem)it.next();
			Object[] pojo = (Object[])it.next();
			
			Listitem item = new Listitem();
			item.setParent(this.list);
			item.setValue(pojo);
			
//			item.appendChild(new Listcell(pojo.getVItemCode()));
//			item.appendChild(new Listcell(pojo.getVItemName()));
			item.appendChild(new Listcell(pojo[3].toString()));
			item.appendChild(new Listcell(pojo[4].toString()));
			item.appendChild(new Listcell(pojo[0].toString()));
			item.appendChild(new Listcell(pojo[2].toString()));
			
			if (jml == 1)
				item.setSelected(true);
		}
	}
	
	public void add() throws InterruptedException, VONEAppException {
		
		Session session = Sessions.getCurrent();
		
		PurchaseController ctr = (PurchaseController)session.getAttribute(MedisafeConstants.PURCHASING_SESSION);			
		
		List currentItem = ctr.getList().getItems();
		
		Set set = list.getSelectedItems();
		
		Iterator it = set.iterator();
		
		//check for duplicate entries
		loop:
		while (it.hasNext()){
			Listitem item = (Listitem)it.next();
//			MsItem itemPojo = (MsItem)item.getValue();
			Object[] itemPojo = (Object[])item.getValue();
			
			Iterator it2 = currentItem.iterator();
			
			while(it2.hasNext()){
				Listitem currItem = (Listitem)it2.next();
				MsItem currItemPojo = (MsItem)currItem.getValue();
//				Object[] currItemPojo = (Object[])currItem.getValue();
				
				if (currItemPojo.getNItemId().equals(itemPojo[1]))
					continue loop;
			}
			
			//common ADD
//			ctr.addItem(itemPojo);
			ctr.addItemRequest(itemPojo);
		}
				
		this.winCurrent.detach();
	}
}
