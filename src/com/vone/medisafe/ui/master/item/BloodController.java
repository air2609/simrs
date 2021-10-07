package com.vone.medisafe.ui.master.item;

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
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.pojo.item.MsBlood;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;


public class BloodController extends BaseController{
	private MsBlood blood;
	private MsItem msItem;
	
	Textbox itemCode;
	Textbox itemName;
	Textbox bloodCode;
	Textbox bloodType;
	Textbox rhesus;
	Listbox bloodGroup;
	Listbox bloodList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		bloodGroup.setSelectedIndex(0);
		bloodCode.setValue(null);
		bloodType.setValue(null);
		rhesus.setValue(null);
		bloodList.clearSelection();
		bloodCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		item = bloodList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		blood = (MsBlood)item.getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = bloodList.getSelectedIndex();
		
		if(MasterServiceLocator.getBloodManager().delete(blood)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			bloodList.removeItemAt(indexSelected);
					
		}else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
			
		}
		
		doCancel(cmp);
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		item = bloodList.getSelectedItem();
		if(item == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		blood = (MsBlood)item.getValue();
		
		bloodCode.setValue(blood.getVBloodCode());
		bloodType.setValue(blood.getVBloodType());
		rhesus.setValue(blood.getVBloodRhesus());
		
		
		for(int i=1; i < bloodGroup.getItems().size(); i++){
			if(blood.getVBloodGroup().equals(bloodGroup.getItemAtIndex(i).getValue().toString())){
				bloodGroup.setSelectedIndex(i);
				break;
			}
		}
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		blood = new MsBlood();
		blood.setVBloodCode(bloodCode.getValue());
		blood.setVBloodGroup(bloodGroup.getSelectedItem().getValue().toString());
		blood.setVBloodRhesus(rhesus.getValue());
		blood.setVBloodType(bloodType.getValue());
		msItem = (MsItem)itemCode.getAttribute("item");
		blood.setMsItem(msItem);
		
		if(MasterServiceLocator.getBloodManager().save(blood)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setParent(bloodList);
			item.setValue(blood);
			
			cell = new Listcell(blood.getVBloodCode());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodGroup());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodType());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodRhesus());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		
		doCancel(cmp);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		item = bloodList.getSelectedItem();
		
		blood = (MsBlood)item.getValue();
		blood.setVBloodCode(bloodCode.getValue());
		blood.setVBloodGroup(bloodGroup.getSelectedItem().getValue().toString());
		blood.setVBloodRhesus(rhesus.getValue());
		blood.setVBloodType(bloodType.getValue());
		
		if(MasterServiceLocator.getBloodManager().save(blood)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(blood);
			
			cell = new Listcell(blood.getVBloodCode());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodGroup());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodType());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodRhesus());
			cell.setParent(item);
						
		}
		else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		doCancel(cmp);
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	
	public void init(Component win) throws InterruptedException, VONEAppException {
		bloodGroup = (Listbox)win.getFellow("bloodGroup");
		bloodCode = (Textbox)win.getFellow("bloodCode"); 
		bloodType = (Textbox)win.getFellow("bloodType");
		itemCode = (Textbox)win.getFellow("itemCode");
		itemName = (Textbox)win.getFellow("itemName");
		rhesus = (Textbox)win.getFellow("rhesus");
		bloodList = (Listbox)win.getFellow("bloodList");
		
		super.init(win);
		
		constraints.registerComponent(bloodCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(bloodCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(bloodType, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(bloodType, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(bloodGroup, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(rhesus, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
//		msItem = MasterServiceLocator.getItemManager().getBloodItem("DARAH");
//		itemCode.setValue(msItem.getVItemCode());
//		itemName.setValue(msItem.getVItemName());
		
		itemCode.setAttribute("item", msItem);
		
		itemCode.setDisabled(true);
		itemName.setDisabled(true);
		
		getBloodData(bloodList);
		
		bloodCode.focus();
		
	}
	
	public void getBloodData(Listbox bloodList){
		bloodList.getItems().clear();
		
		List list = MasterServiceLocator.getBloodManager().getBloods();
		Iterator it = list.iterator();
		while(it.hasNext()){
			blood = (MsBlood)it.next();
			
			item = new Listitem();
			item.setParent(bloodList);
			item.setValue(blood);
			
			cell = new Listcell(blood.getVBloodCode());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodGroup());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodType());
			cell.setParent(item);
			
			cell = new Listcell(blood.getVBloodRhesus());
			cell.setParent(item);
				
		}
		MiscTrxController.setFont(bloodList);
	}
}
