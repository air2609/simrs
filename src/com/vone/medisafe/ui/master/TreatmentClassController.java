package com.vone.medisafe.ui.master;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.TreatmentClassManager;
import com.vone.medisafe.ui.base.BaseController;

public class TreatmentClassController extends BaseController{
	
	ZulConstraint constrains = new ZulConstraint();
	
	static TreatmentClassManager service = MasterServiceLocator.getTreatmentClassManager();
	
	
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
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		if(treatmentClassList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.tclass.tclasslist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getTreatmentClassManager().deleteById((Integer)treatmentClassList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getTClassDataList(treatmentClassList);
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
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		if(treatmentClassList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.tclass.tclasslist.notselected"));
			return;
		}
		super.doModify(win);
		kode.setText(((Listcell)treatmentClassList.getSelectedItem().getChildren().get(0)).getLabel());
		nama.setText(((Listcell)treatmentClassList.getSelectedItem().getChildren().get(1)).getLabel());
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constrains.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getTreatmentClassManager().getTClassByCode(kode.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		
		MsTreatmentClass tclass = new MsTreatmentClass();
		tclass.setDWhnCreate(new Date());
		tclass.setVTclassCode(kode.getText());
		tclass.setVTclassDesc(nama.getText());
		MasterServiceLocator.getTreatmentClassManager().save(tclass);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getTClassDataList(treatmentClassList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		super.doSaveModify(win);
		
		MsTreatmentClass tclass = new MsTreatmentClass();
		tclass.setNTclassId((Integer)treatmentClassList.getSelectedItem().getValue());
		tclass.setVTclassCode(kode.getText());
		tclass.setVTclassDesc(nama.getText());
		MasterServiceLocator.getTreatmentClassManager().save(tclass);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getTClassDataList(treatmentClassList);
		
		
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		super.init(win);
		
		constrains.registerComponent(kode,ZulConstraint.NO_EMPTY);
		constrains.registerComponent(kode, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(nama,ZulConstraint.NO_EMPTY);
		constrains.registerComponent(nama, ZulConstraint.UPPER_CASE);
		constrains.validateComponent(false);
		
		getTClassDataList(treatmentClassList);
		
	}


	public static void getTClassDataList(Listbox treatmentClassList) throws VONEAppException{
		
		service.getTreatmentClassData(treatmentClassList);
		MiscTrxController.setFont(treatmentClassList);
	}	


}
