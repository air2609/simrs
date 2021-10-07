package com.vone.medisafe.ui.master.treatment;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Decimalbox;
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
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentRoom;
import com.vone.medisafe.mapping.MsTreatmentRoomFee;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.accounting.CoaController;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.TreatmentClassController;

public class TreatmentRoomController extends BaseController {
	
	private MsTreatmentRoom troom;
	private MsTreatmentRoomFee troomFee;
	
	Textbox treatmentCode;
	Textbox roomName;
	Listbox tclass;
	Decimalbox treatmentFee;
	Listbox coa;
	Listbox treatmentRoomList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint constraints = new ZulConstraint();

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		treatmentCode.setValue(null);
		roomName.setValue(null);
		tclass.setSelectedIndex(0);
		treatmentFee.setValue(null);
		coa.setSelectedIndex(0);
		super.doCancel(cmp);
		treatmentCode.focus();
		treatmentRoomList.clearSelection();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		if(treatmentRoomList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatment.list.notselected"));
			treatmentCode.focus();
			return;
		}
		troomFee = (MsTreatmentRoomFee)treatmentRoomList.getSelectedItem().getValue();
		troom = troomFee.getMsTreatmentRoom();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = treatmentRoomList.getSelectedIndex();
		if(TreatmentService.getTreatmentRoomManager().delete(troom)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			treatmentRoomList.removeItemAt(indexSelected);
			doCancel(cmp);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
	}

	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		if(treatmentRoomList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.treatmentroom.list.notselected"));
			return;
		}
		
		troomFee = (MsTreatmentRoomFee)treatmentRoomList.getSelectedItem().getValue();
		treatmentCode.setValue(troomFee.getMsTreatmentRoom().getVTroomCode());
		roomName.setValue(troomFee.getMsTreatmentRoom().getVTroomName());
		treatmentFee.setValue(new BigDecimal(troomFee.getNTroomFee()));
		
		for(int i=1; i < tclass.getItems().size(); i++){
			if(troomFee.getMsTreatmentClass().getNTclassId().equals(((MsTreatmentClass)tclass.getItemAtIndex(i).getValue())
					.getNTclassId()))
			{
				tclass.setSelectedIndex(i);
				break;
			}
		}
		
		for(int i=1; i < coa.getItems().size(); i++){
			if(troomFee.getMsCoa().getNCoaId().equals(((MsCoa)coa.getItemAtIndex(i).getValue()).getNCoaId()))
			{
				coa.setSelectedIndex(i);
				break;
			}
		}
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		if(!constraints.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		
		super.doSaveAdd(cmp);
		
		troomFee = new MsTreatmentRoomFee();
		troom = new MsTreatmentRoom();
		
		troom.setVTroomCode(treatmentCode.getValue());
		troom.setVTroomName(roomName.getValue());
		
		troomFee.setMsTreatmentClass((MsTreatmentClass)tclass.getSelectedItem().getValue());
		troomFee.setMsCoa((MsCoa)coa.getSelectedItem().getValue());
		troomFee.setNTroomFee(treatmentFee.getValue().doubleValue());
		
		if(TreatmentService.getTreatmentRoomManager().save(troom, troomFee)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(troomFee);
			item.setParent(treatmentRoomList);
			
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomCode());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomName());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell(treatmentFee.getText());
			cell.setParent(item);
			
			doCancel(cmp);
		}else{
			Messagebox.show("common.add.fail");
		}
	}

	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		
		item = treatmentRoomList.getSelectedItem();
		troomFee = (MsTreatmentRoomFee)item.getValue();
		troom = troomFee.getMsTreatmentRoom();
		
		troom.setVTroomCode(treatmentCode.getValue());
		troom.setVTroomName(roomName.getValue());
		
		troomFee.setMsTreatmentClass((MsTreatmentClass)tclass.getSelectedItem().getValue());
		troomFee.setMsCoa((MsCoa)coa.getSelectedItem().getValue());
		troomFee.setNTroomFee(treatmentFee.getValue().doubleValue());
		
		if(TreatmentService.getTreatmentRoomManager().save(troom, troomFee)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			item.getChildren().clear();
			item.setValue(troomFee);
						
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomCode());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomName());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			cell = new Listcell(treatmentFee.getText());
			cell.setParent(item);
						
			doCancel(cmp);
		}else{
			Messagebox.show("common.modify.fail");
		}
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		treatmentCode = (Textbox)win.getFellow("treatmentCode");
		roomName = (Textbox)win.getFellow("roomName");
		tclass = (Listbox)win.getFellow("tclass");
		treatmentFee = (Decimalbox)win.getFellow("treatmentFee");
		coa = (Listbox) win.getFellow("coa");
		treatmentRoomList = (Listbox) win.getFellow("treatmentRoomList"); 
		
		super.init(win);
		
		constraints.registerComponent(treatmentCode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentCode, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(roomName, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(roomName, ZulConstraint.UPPER_CASE);
		
		constraints.registerComponent(tclass, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(treatmentFee, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(coa, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
		//TreatmentClassController.getTClass(tclass);
		TreatmentClassController.getTClassDataList(tclass);
		CoaController.getCoaForSelect(coa, MedisafeConstants.COA_ALL);
		getTreatmentRoomData();
		treatmentCode.focus();
	}
	
	public void getTreatmentRoomData(){
		treatmentRoomList.getItems().clear();
		
		List list = TreatmentService.getTreatmentRoomManager().getTreatmentRooms();
		Iterator it = list.iterator();
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,##0.##");
		
		while(it.hasNext()){
			troomFee = (MsTreatmentRoomFee)it.next();
			
			item = new Listitem();
			item.setValue(troomFee);
			item.setParent(treatmentRoomList);
			
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomCode());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentRoom().getVTroomName());
			cell.setParent(item);
			
			cell = new Listcell(troomFee.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			db.setValue(new BigDecimal(troomFee.getNTroomFee()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
		}
		MiscTrxController.setFont(treatmentRoomList);
	}
	
}
