package com.vone.medisafe.ui.master.treatment;


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
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class TreatmentGroupController extends BaseController{
	
//	private static Logger log = Logger.getLogger(TreatmentGroupController.class);
	private MsTreatmentGroup tgroup;
	
	Textbox treatGroupCode;
	Textbox treatGroupName;
	Listbox treatmentGroupList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();
	

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		treatGroupCode.setValue(null);
		treatGroupName.setValue(null);
		
		super.doCancel(cmp);
		
		treatGroupCode.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		
		if(treatmentGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.treatmentgroup.notselected"));
			treatGroupCode.focus();
			return;
		}
		tgroup = (MsTreatmentGroup)treatmentGroupList.getSelectedItem().getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = treatmentGroupList.getSelectedIndex();
		if(MasterServiceLocator.getTreatmentGroupManager().delete(tgroup)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			treatmentGroupList.removeItemAt(indexSelected);
			doCancel(cmp);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {

		super.doModify(win);
		
		if(treatmentGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.treatmentgroup.notselected"));
			treatGroupCode.focus();
			return;
		}
		tgroup = (MsTreatmentGroup)treatmentGroupList.getSelectedItem().getValue();
		treatGroupCode.setValue(tgroup.getVTgroupCode());
		treatGroupName.setValue(tgroup.getVTgroupName());
		MiscTrxController.setFont(treatmentGroupList);
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		
		super.doSaveAdd(win);
		
		tgroup = new MsTreatmentGroup();
		tgroup.setVTgroupCode(treatGroupCode.getValue());
		tgroup.setVTgroupName(treatGroupName.getValue());
		
		
		if(MasterServiceLocator.getTreatmentGroupManager().save(tgroup)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(tgroup);
			item.setParent(treatmentGroupList);
			
			cell = new Listcell(tgroup.getVTgroupCode());
			cell.setParent(item);
			
			cell = new Listcell(tgroup.getVTgroupName());
			cell.setParent(item);
			
			doCancel(win);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.add.fail"));
		}
		MiscTrxController.setFont(treatmentGroupList);
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		
		super.doSaveModify(cmp);
		item = treatmentGroupList.getSelectedItem();
		tgroup = (MsTreatmentGroup)item.getValue();
		tgroup.setVTgroupCode(treatGroupCode.getValue());
		tgroup.setVTgroupName(treatGroupName.getValue());
		
				
		if(MasterServiceLocator.getTreatmentGroupManager().save(tgroup)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			item.getChildren().clear();
			item.setValue(tgroup);
			
			cell = new Listcell(tgroup.getVTgroupCode());
			cell.setParent(item);
			
			cell = new Listcell(tgroup.getVTgroupName());
			cell.setParent(item);
			
			doCancel(cmp);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.modify.fail"));
		}
		MiscTrxController.setFont(treatmentGroupList);
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		treatGroupCode = (Textbox) win.getFellow("treatGroupCode");
		treatGroupName = (Textbox) win.getFellow("treatGroupName");
		treatmentGroupList =(Listbox)win.getFellow("treatmentGroupList");
		
		super.init(win);
		
		constraints.registerComponent(treatGroupCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatGroupCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(treatGroupName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatGroupName, ZulConstraint.UPPER_CASE);
		
				
		constraints.validateComponent(false);
		getTreatmentListData(treatmentGroupList);
	}

	public void setUserInfoBean(UserInfoBean uib) {
		// TODO Auto-generated method stub
//		super.setUserInfoBean(uib);
	}
	
	public void getTreatmentListData(Listbox tgroupList) throws VONEAppException{
		tgroupList.getItems().clear();
		
		List list = MasterServiceLocator.getTreatmentGroupManager().getTGroups();
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			tgroup = (MsTreatmentGroup)it.next();
			
			item = new Listitem();
			item.setValue(tgroup);
			item.setParent(tgroupList);
			
			cell = new Listcell(tgroup.getVTgroupCode());
			cell.setParent(item);
			
			cell = new Listcell(tgroup.getVTgroupName());
			cell.setParent(item);
				
		} 
		MiscTrxController.setFont(tgroupList);
	}
	
	public static void getTGroupForSelect(Listbox listbox) throws VONEAppException{
		listbox.getItems().clear();
		
		MsTreatmentGroup tgroup;
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
		
		List list = MasterServiceLocator.getTreatmentGroupManager().getTGroups();
				
		Iterator it = list.iterator();
		while(it.hasNext()){
			tgroup = (MsTreatmentGroup)it.next();
			item = new Listitem();
			item.setValue(tgroup);
			item.setLabel(tgroup.getVTgroupName());
			item.setParent(listbox);
		}
	}
	
}