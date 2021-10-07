package com.vone.medisafe.ui.master;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;


public class HallController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		super.doCancel(win);
		kode.setValue(null);
		nama.setValue(null);
		wardList.focus();
		if(tclassList.getItems().size() > 0)tclassList.setSelectedIndex(0);
		if(wardList.getItems().size() > 0)wardList.setSelectedIndex(0);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox hallList = (Listbox)win.getFellow("hallList");
		super.doDelete(win);
		if(hallList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.hall.halllist.notselected"));
			return;
		}
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getHallManager().deleteById(((MsHall)hallList.getSelectedItem().getValue()).getNHallId())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				getHallDataList(hallList);
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
		Listbox hallList = (Listbox)win.getFellow("hallList");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		super.doModify(win);
		if(hallList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.hall.halllist.notselected"));
			return;
		}
		MsHall hall = (MsHall)hallList.getSelectedItem().getValue();
//		MsHall hall = MasterServiceLocator.getHallManager().getById((Integer)hallList.getSelectedItem().getValue());
		kode.setValue(hall.getVHallCode());
		nama.setValue(hall.getVHallName());
		
		for(int i=1; i< tclassList.getItems().size(); i++){
			if(((MsTreatmentClass)tclassList.getItemAtIndex(i).getValue()).getNTclassId().equals(hall.getMsTreatmentClass().getNTclassId())){
				tclassList.setSelectedIndex(i);
				break;
			}
		}
		for(int i=1; i< wardList.getItems().size(); i++){
			if(wardList.getItemAtIndex(i).getValue().equals(hall.getMsWard().getNWardId())){
				wardList.setSelectedIndex(i);
				break;
			}
		}
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
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		Listbox hallList = (Listbox)win.getFellow("hallList");
		super.doSaveAdd(win);

		
		MsHall hall = new MsHall();
		hall.setVHallCode(kode.getText());
		hall.setVHallName(nama.getText());
		hall.setDWhnCreate(new Date());
		hall.setMsTreatmentClass((MsTreatmentClass)tclassList.getSelectedItem().getValue());
//				MasterServiceLocator.
//				getTreatmentClassManager().getById((Integer)tclassList.getSelectedItem().getValue()));
		hall.setMsWard(MasterServiceLocator.getWardManager().findById((Integer)wardList.getSelectedItem().getValue()));
		MasterServiceLocator.getHallManager().save(hall);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		doCancel(win);
		getHallDataList(hallList);
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		Listbox hallList = (Listbox)win.getFellow("hallList");
		super.doSaveModify(win);
		
//		MsHall hall = new MsHall();
		MsHall hall = (MsHall)hallList.getSelectedItem().getValue();
		hall.setVHallCode(kode.getText());
		hall.setVHallName(nama.getText());
//		hall.setNHallId((Integer)hallList.getSelectedItem().getValue());
		hall.setMsTreatmentClass((MsTreatmentClass)tclassList.getSelectedItem().getValue());
				
//				MasterServiceLocator.
//				getTreatmentClassManager().getById((Integer)tclassList.getSelectedItem().getValue()));
		hall.setMsWard(MasterServiceLocator.getWardManager().findById((Integer)wardList.getSelectedItem().getValue()));
		MasterServiceLocator.getHallManager().save(hall);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		getHallDataList(hallList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		Listbox hallList = (Listbox)win.getFellow("hallList");
		super.init(win);
		
		constraints.registerComponent(kode,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kode,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(nama,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(tclassList,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(wardList,ZulConstraint.NO_EMPTY);
		nama.setReadonly(true);
		
		
		constraints.validateComponent(false);
		
		MasterServiceLocator.getTreatmentClassManager().getTClassForSelect(tclassList);
		
		WardController.getWardList(wardList);
		getHallDataList(hallList);
		this.doCancel(win);
		
	}

	public static void getHallDataList(Listbox hallList) throws VONEAppException{
		
		//List list = 
		MasterServiceLocator.getHallManager().getAllHall(hallList);
		MiscTrxController.setFont(hallList);
		
	}
	
	public void getHallName(Component win){
		Textbox nama = (Textbox)win.getFellow("name");
		Textbox kode = (Textbox)win.getFellow("id");
		Listbox wardList = (Listbox)win.getFellow("wardList");
		nama.setText(wardList.getSelectedItem().getLabel()+" "+kode.getText().toUpperCase());
	}
	
	public static void searchHall(Listbox searchDataList, Textbox name) throws WrongValueException, VONEAppException{
		//List result = 
		MasterServiceLocator.getHallManager().searchHall(name.getText(), searchDataList);
		MiscTrxController.setFont(searchDataList);
//		searchDataList.getItems().clear();
//		MsHall hall = null;
//		Iterator it = result.iterator();
//		
//		while(it.hasNext()){
//			hall = (MsHall)it.next();
//			Listitem item = new Listitem();
//			item.setValue(hall.getNHallId());
//			item.setParent(searchDataList);
//			
//			Listcell hallName = new Listcell(hall.getVHallName());
//			hallName.setParent(item);
//			Listcell tclassName = new Listcell(hall.getMsTreatmentClass().getVTclassDesc());
//			tclassName.setValue(hall.getMsTreatmentClass().getVTclassCode());
//			tclassName.setParent(item);
//		}
	}
	
}
