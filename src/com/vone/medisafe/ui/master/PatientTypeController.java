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
import com.vone.medisafe.mapping.MsPatientType;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.PatientTypeManager;
import com.vone.medisafe.ui.base.BaseController;

/**
 * PatientTypeController.java
 * @author Arifullah Ibn Rusyd
 * @since 29-07-2006
 * @version 0.3 -- last revised at 03-08-2006
 * used to controll all data flow from.zul file related to patient type data 
 */
public class PatientTypeController extends BaseController
{
	ZulConstraint constraints = new ZulConstraint();
	
	static PatientTypeManager service = MasterServiceLocator.getPatientTypeManager();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		
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
		Listbox prov = (Listbox)win.getFellow("patientTypeList");
		if(prov.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.ptype.ptypelist.notselected"));
			return;
		}
		super.doDelete(win);
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getPatientTypeManager().deleteById((Integer)prov.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getListPatientTypeData(prov);
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
		Listbox prov = (Listbox)win.getFellow("patientTypeList");
		super.doModify(win);
		if(prov.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.ptype.ptypelist.notselected"));
			return;
		}
		id.setText(((Listcell)prov.getSelectedItem().getChildren().get(0)).getLabel());
		name.setText(((Listcell)prov.getSelectedItem().getChildren().get(1)).getLabel());
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
		Listbox prov = (Listbox)win.getFellow("patientTypeList");
		super.doSaveAdd(win);
		if(MasterServiceLocator.getPatientTypeManager().getByCode(id.getText()) != null){
			Messagebox.show(MessagesService.getKey("master.common.code.exist"));
			return;
		}
		MsPatientType ptype = new MsPatientType();
		ptype.setVTpatient(id.getText());
		ptype.setVTpatientDesc(name.getText());
		ptype.setDWhnCreate(new Date());
		MasterServiceLocator.getPatientTypeManager().save(ptype);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getListPatientTypeData(prov);
	}
	
	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("patientTypeList");
		super.doSaveModify(win);
		
		MsPatientType ptype = new MsPatientType();
		ptype.setVTpatient(id.getText());
		ptype.setVTpatientDesc(name.getText());
		ptype.setNPatientTypeId((Integer)prov.getSelectedItem().getValue());
		MasterServiceLocator.getPatientTypeManager().save(ptype);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getListPatientTypeData(prov);
	}
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox id = (Textbox)win.getFellow("id");
		Textbox name = (Textbox)win.getFellow("name");
		Listbox prov = (Listbox)win.getFellow("patientTypeList");
		super.init(win);
		
		constraints.registerComponent(id, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(name, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(id, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		getListPatientTypeData(prov);
	}
	
	public static void getListPatientTypeData(Listbox prov) throws VONEAppException{
		
		service.getAllPatientTypeData(prov);
		MiscTrxController.setFont(prov);
	}

	
	
	
	public static void getAllPatientTypeList2(Listbox listbox) throws VONEAppException{
		
		service.getPatientTypeForSelect(listbox);
		
	}
	
	
	public static void getPatientTypeForSelect(Listbox listbox) throws VONEAppException{
		
		service.getPatientTypeForSelect(listbox);
		
	}

}
