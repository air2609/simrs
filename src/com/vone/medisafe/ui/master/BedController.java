package com.vone.medisafe.ui.master;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
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
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;




public class BedController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	Listbox activeStatus;
	Listbox conditionStatus;
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Decimalbox price = (Decimalbox)win.getFellow("price");
		Listbox bedList = (Listbox)win.getFellow("bedList");
		Bandbox coa = (Bandbox)win.getFellow("coa");
		super.doCancel(win);
		
		tclassList.setSelectedIndex(0);
		namaKamar.setValue(null);
		kode.setValue(null);
		nama.setValue(null);
		price.setValue(null);
		coa.setValue(null);
		coa.removeAttribute("coa");
		namaKamar.focus();
		bedList.clearSelection();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox bedList = (Listbox)win.getFellow("bedList");
		super.doDelete(win);
		
		if(bedList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.bed.bedlist.notselected"));
			return;
		}
		
		int index = bedList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			
			if(MasterServiceLocator.getBedManager().deleteById((Integer)bedList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				bedList.removeItemAt(index);
//				getBedDataList(bedList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		super.doModify(win);

		MasterServiceLocator.getBedManager().doModify(win);
		
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
		Decimalbox price = (Decimalbox)win.getFellow("price");
		Listbox bedList = (Listbox)win.getFellow("bedList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Bandbox coa = (Bandbox)win.getFellow("coa");
		super.doSaveAdd(win);
		
		MsBed bed = new MsBed();
		
		MsRoom room = (MsRoom)win.getAttribute("room");
		MsTreatmentClass tclass = (MsTreatmentClass)win.getAttribute("tclass");
		
		bed.setVBedCode(kode.getText());
		bed.setVBedDesc(nama.getText());
		bed.setMsCoa((MsCoa)coa.getAttribute("coa"));
		bed.setVBedStatus(MedisafeConstants.BED_KOSONG);
		bed.setDWhnCreate(new Date());
		bed.setNBedPrice(price.getValue().doubleValue());
		bed.setMsRoom(room);
		bed.setMsTreatmentClass(tclass);
		bed.setVBedActiveStatus(activeStatus.getSelectedItem().getValue().toString());
		if(MasterServiceLocator.getBedManager().isBedExist(bed)){
			Messagebox.show(MessagesService.getKey("common.add.dataexist"));
			doCancel(win);
			return;
		}
		
		MasterServiceLocator.getBedManager().save(bed);
		
		win.removeAttribute("room");
		win.removeAttribute("tclass");
		
		Listitem item = new Listitem();
		item.setValue(bed.getNBedId());
		item.setParent(bedList);
		
		String ruang[] =namaKamar.getText().split("-");
		
		Listcell namaRuang = new Listcell(ruang[0]);
		namaRuang.setParent(item);
		
		Listcell cell = new Listcell(bed.getVBedDesc());
		cell.setParent(item);
		
//		MsTreatmentClass kelasTarif = (MsTreatmentClass)tclassList.getSelectedItem().getValue();
		
		cell = new Listcell(tclass.getVTclassDesc());
		cell.setParent(item);
		
		Listcell roomNo = new Listcell(ruang[2]);
		roomNo.setParent(item);
		
		Listcell id = new Listcell(bed.getVBedCode());
		id.setParent(item);

		price.setValue(new BigDecimal(bed.getNBedPrice()));
		Listcell harga = new Listcell(price.getText());
		harga.setParent(item);

		cell = new Listcell(activeStatus.getSelectedItem().getLabel());
		cell.setParent(item);
//		getBedDataList(bedList);
		doCancel(win);
		Messagebox.show(MessagesService.getKey("common.add.success"));
		MiscTrxController.setFont(bedList);
	}
	
	private void checkDuplicateBed() throws InterruptedException {
		List roomList = MasterServiceLocator.getBedManager().getDuplicateBed();
		if(roomList.size() > 0){
			String roomName = "";
			Iterator it = roomList.iterator();
			while(it.hasNext()){
				Object[] o = (Object[])it.next();
				roomName = roomName + o[1]  +",";
			}
			
			Messagebox.show("BED "+roomName.substring(0, roomName.length()-1) + " DUPLIKAT! SILAHKAN DIPERBAIKI!!!");
		}
		
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Decimalbox price = (Decimalbox)win.getFellow("price");
		Listbox bedList = (Listbox)win.getFellow("bedList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Bandbox coa = (Bandbox)win.getFellow("coa");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		super.doSaveModify(win);
		MsBed bed = MasterServiceLocator.getBedManager().getBedById((Integer)bedList.getSelectedItem().getValue());
		MsRoom room = (MsRoom)win.getAttribute("room");
		if(room == null){
			System.out.println(" room null ");
		}
//		MsTreatmentClass tclass = (MsTreatmentClass)win.getAttribute("tclass");
		MsTreatmentClass tclass = (MsTreatmentClass)tclassList.getSelectedItem().getValue();
		
		bed.setVBedCode(kode.getText());
		bed.setVBedDesc(nama.getText());
		bed.setMsCoa((MsCoa)coa.getAttribute("coa"));
		bed.setVBedStatus(conditionStatus.getSelectedItem().getValue().toString());
		bed.setNBedPrice(price.getValue().doubleValue());
		bed.setMsRoom(room);
		bed.setMsTreatmentClass(tclass);
		bed.setVBedActiveStatus(activeStatus.getSelectedItem().getValue().toString());
		
		MasterServiceLocator.getBedManager().save(bed);
		
//		Messagebox.show("coa id : "+((MsCoa)coa.getAttribute("coa")).getNCoaId());
		
		Listitem item = bedList.getSelectedItem();
		item.getChildren().clear();
		item.setValue(bed.getNBedId());
//		item.setParent(bedList);
		
		String ruang[] =namaKamar.getText().split("-");
		
		Listcell namaRuang = new Listcell(ruang[0]);
		namaRuang.setParent(item);
		
		Listcell cell = new Listcell(bed.getVBedDesc());
		cell.setParent(item);
		
//		MsTreatmentClass kelasTarif = (MsTreatmentClass)tclassList.getSelectedItem().getValue();
		
		cell = new Listcell(tclass.getVTclassDesc());
		cell.setParent(item);
		
		Listcell roomNo = new Listcell(ruang[2]);
		roomNo.setParent(item);
		
		Listcell id = new Listcell(bed.getVBedCode());
		id.setParent(item);

		price.setValue(new BigDecimal(bed.getNBedPrice()));
		Listcell harga = new Listcell(price.getText());
		harga.setParent(item);
		
		cell = new Listcell(activeStatus.getSelectedItem().getLabel());
		cell.setParent(item);
		
		win.removeAttribute("room");
		win.removeAttribute("tclass");
		doCancel(win);
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		MiscTrxController.setFont(bedList);
		
		
//		getBedDataList(bedList);
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Listbox roomList = (Listbox)win.getFellow("roomList");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Decimalbox price = (Decimalbox)win.getFellow("price");
		Listbox bedList = (Listbox)win.getFellow("bedList");
		Bandbox coa = (Bandbox)win.getFellow("coa");
		activeStatus = (Listbox)win.getFellow("activeStatus");
		conditionStatus = (Listbox)win.getFellow("conditionStatus");
		super.init(win);
		
		constraints.registerComponent(tclassList,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaKamar,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaKamar,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(kode,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kode,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(price,ZulConstraint.NO_EMPTY);
		constraints.registerComponent(coa,ZulConstraint.NO_EMPTY);
		constraints.validateComponent(false);
				
		//TreatmentClassController.getTClass(tclassList);
		MasterServiceLocator.getTreatmentClassManager().getTClassForSelect(tclassList);
		
		nama.setReadonly(true);
		roomList.getItems().clear();
				
		doCancel(win);
		getBedDataList(bedList);
		
		checkDuplicateBed();
		
		
	}
	
	public void getRoomNameBySearch(Component win){
		Listbox roomList = (Listbox)win.getFellow("roomList");
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		
		Listitem item = roomList.getSelectedItem();
		namaKamar.setValue(item.getLabel());
		
		String hallCode = ((Listcell)item.getChildren().get(0)).getValue().toString();
		Integer tclassId = (Integer)((Listcell)item.getChildren().get(1)).getValue();
		String tclassCode = ((Listcell)item.getChildren().get(1)).getLabel();
		String roomCode = ((Listcell)item.getChildren().get(2)).getLabel();
		String bedName = hallCode+"-"+tclassCode+"-"+roomCode;
		if(kode.getText().length() > 0)bedName=bedName+"-"+kode.getText();
		nama.setValue(bedName);
		for(int i=1; i < tclassList.getItems().size();i++){
			if(((MsTreatmentClass)tclassList.getItemAtIndex(i).getValue()).getNTclassId().equals(tclassId)){
				tclassList.setSelectedIndex(i);
				return;
			}
		}
		
	}
	
	public void getBedName(Component win) throws InterruptedException, VONEAppException{
		
		Bandbox namaKamar = (Bandbox)win.getFellow("namaKamar");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox tclassList = (Listbox)win.getFellow("tclassList");
		
		MsRoom room = MasterServiceLocator.getRoomManager().getByName(namaKamar.getText());
		if(room == null){
			Messagebox.show(MessagesService.getKey("master.room.name.notfound"));
			return;
		}
		if(tclassList.getSelectedItem().getValue().equals(MedisafeConstants.LISTKOSONG)){
			Messagebox.show(MessagesService.getKey("master.tclass.tclasslist.notselected"));
			return;
		}
		
		//nama = kode ruangan + kode kelas tarif + kode kamar + nomor bed
		MsHall hall = MasterServiceLocator.getHallManager().getHallByRoom(room);
		String strName = hall.getVHallCode();
		MsTreatmentClass tclass = (MsTreatmentClass)tclassList.getSelectedItem().getValue(); 
			
//			MasterServiceLocator.getTreatmentClassManager().getById((Integer)tclassList.getSelectedItem()
//				.getValue());
		strName = strName+"-"+tclass.getVTclassCode();
		strName = strName+"-"+room.getVRoomCode();
		if(kode.getText().length() > 0)strName=strName+"-"+kode.getText();
		nama.setValue(strName);
		win.setAttribute("room", room);
		win.setAttribute("tclass", tclass);
				
	}

	public static void getBedDataList(Listbox bedList) throws VONEAppException{
		
		//List list = MasterServiceLocator.getBedManager().getAllBed();
		MasterServiceLocator.getBedManager().getAllBed(bedList);
		MiscTrxController.setFont(bedList);
		
	}
	
	
	public void searchBed(Listbox bedList, Textbox cari) throws VONEAppException
	{
		cari.setText(cari.getText().toUpperCase());
		MasterServiceLocator.getBedManager().searchBed("%"+cari.getText()+"%", bedList);
	}

}
