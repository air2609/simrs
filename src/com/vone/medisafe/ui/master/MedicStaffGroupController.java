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
import com.vone.medisafe.mapping.MsMedicStaffGroup;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class MedicStaffGroupController extends BaseController{
	
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
		Listbox medicStaffGroupList = (Listbox)win.getFellow("medicStaffGroupList");
		if(medicStaffGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.msg.msglist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getMedicStaffGroupManager().deleteById((Integer)medicStaffGroupList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getMedicStaffGroupDataList(medicStaffGroupList);
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
		Listbox medicStaffGroupList = (Listbox)win.getFellow("medicStaffGroupList");
		super.doModify(win);
		if(medicStaffGroupList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.msg.msglist.notselected"));
			return;
		}
		kode.setText(((Listcell)medicStaffGroupList.getSelectedItem().getChildren().get(0)).getLabel());
		nama.setText(((Listcell)medicStaffGroupList.getSelectedItem().getChildren().get(1)).getLabel());
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
		Listbox medicStaffGroupList = (Listbox)win.getFellow("medicStaffGroupList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getMedicStaffGroupManager().getByCode(kode.getText())!= null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		MsMedicStaffGroup msg = new MsMedicStaffGroup();
		msg.setDWhnCreate(new Date());
		msg.setVMsgroupCode(kode.getText());
		msg.setVMsgroupName(nama.getText());
		MasterServiceLocator.getMedicStaffGroupManager().save(msg);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getMedicStaffGroupDataList(medicStaffGroupList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox medicStaffGroupList = (Listbox)win.getFellow("medicStaffGroupList");
		super.doSaveModify(win);
		
		MsMedicStaffGroup msg = new MsMedicStaffGroup();
		msg.setNMsgroupId((Integer)medicStaffGroupList.getSelectedItem().getValue());
		msg.setVMsgroupCode(kode.getText());
		msg.setVMsgroupName(nama.getText());
		MasterServiceLocator.getMedicStaffGroupManager().save(msg);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getMedicStaffGroupDataList(medicStaffGroupList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox medicStaffGroupList = (Listbox)win.getFellow("medicStaffGroupList");
		super.init(win);
		
		constraints.registerComponent(kode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(nama, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(nama, ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		getMedicStaffGroupDataList(medicStaffGroupList);
	}

	public static void getMedicStaffGroupDataList(Listbox medicStaffGroupList){
			
			medicStaffGroupList.getItems().clear();
			MsMedicStaffGroup msg = null;
			
			Listitem item;
			List list = MasterServiceLocator.getMedicStaffGroupManager().getAllMedicStaffGroup();
			Iterator it = list.iterator();
			while(it.hasNext()){
				msg = (MsMedicStaffGroup)it.next();
				item = new Listitem();
				item.setValue(msg.getNMsgroupId());
				item.setParent(medicStaffGroupList);
				Listcell id = new Listcell(msg.getVMsgroupCode());
				id.setParent(item);
				Listcell name = new Listcell(msg.getVMsgroupName());
				name.setParent(item);
			}
			
	}	

	public static void getMedicStaffGroupList(Listbox medicStaffGroupList) {
		
		medicStaffGroupList.getItems().clear();
		Listitem pilih = new Listitem();
		pilih.setLabel(MedisafeConstants.LABELKOSONG);
		pilih.setValue(MedisafeConstants.LISTKOSONG);
		pilih.setParent(medicStaffGroupList);

		MsMedicStaffGroup msg = null;

		List list = MasterServiceLocator.getMedicStaffGroupManager()
				.getAllMedicStaffGroup();
		int i = 1;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			msg = (MsMedicStaffGroup) it.next();
			pilih = new Listitem();
			pilih.setValue(msg.getNMsgroupId());
			pilih.setLabel(i + ". " + msg.getVMsgroupName());
			pilih.setParent(medicStaffGroupList);
			i++;
		}

	}
	
}
