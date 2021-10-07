package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

//import com.lowagie.text.RomanList;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsWard;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;


public class RoomController extends BaseController{
	
	ZulConstraint constraints = new ZulConstraint();
	
	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Bandbox namaRuangan = (Bandbox)win.getFellow("namaRuangan");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		super.doCancel(win);
		kode.setValue(null);
		namaRuangan.setValue(null);
		nama.setValue(null);
		namaRuangan.focus();
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox roomList = (Listbox)win.getFellow("roomList");
		super.doDelete(win);
		if(roomList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.room.roomlist.notselected"));
			return;
		}
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			int selected = roomList.getSelectedIndex();
			if(MasterServiceLocator.getRoomManager().deleteById((Integer)roomList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				roomList.removeItemAt(selected);
//				getDataRoomList(roomList,MasterServiceLocator.getRoomManager().getAllRoom());
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox roomList = (Listbox)win.getFellow("roomList");
		Bandbox namaRuangan = (Bandbox)win.getFellow("namaRuangan");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		super.doModify(win);
		if(roomList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.room.roomlist.notselected"));
			return;
		}
		
		MasterServiceLocator.getRoomManager().fillRoomDetail(roomList, namaRuangan, kode, nama);
	
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
		Listbox roomList = (Listbox)win.getFellow("roomList");
		super.doSaveAdd(win);
		MsRoom room = new MsRoom();
		room.setDWhnCreate(new Date());
		room.setVRoomCode(kode.getText());
		room.setVRoomName(nama.getText());
		MsHall hall = (MsHall)win.getAttribute("hall");
		MsTreatmentClass tclass = (MsTreatmentClass)win.getAttribute("tclass");
		MsWard ward = (MsWard)win.getAttribute("ward");
		room.setMsHall(hall);
		room.setMsTreatmentClass(tclass);
		room.setMsWard(ward);
		if(MasterServiceLocator.getRoomManager().isRoomAlreadyExist(room.getVRoomName())){
			Messagebox.show("NAMA KAMAR "+ room.getVRoomName() + " SUDAH ADA. SILAHKAN GANTI DENGAN YANG LAIN!");
			return;
		}
		MasterServiceLocator.getRoomManager().save(room);
		win.removeAttribute("hall");
		win.removeAttribute("tclass");
		win.removeAttribute("ward");
		Messagebox.show(MessagesService.getKey("common.add.success"));
		String kt = null;
		Listitem item = new Listitem();
		item.setValue(room.getNRoomId());
		item.setParent(roomList);
		String[] roomName = room.getVRoomName().split("-");
		Listcell id = new Listcell(roomName[0]);
		id.setParent(item);
		if(roomName[1].equalsIgnoreCase("0"))
			kt = "VIP";
		else if(roomName[1].equalsIgnoreCase("9"))
			kt = "VVIP";
		else if(roomName[1].equalsIgnoreCase("1"))
			kt = "KELAS I";
		else if(roomName[1].equalsIgnoreCase("2"))
			kt = "KELAS II";
		else if(roomName[1].equalsIgnoreCase("3"))
			kt = "KELAS III";
		Listcell name = new Listcell(kt);
		name.setAttribute("kl", roomName[1]);
		name.setParent(item);
		Listcell divisi = new Listcell(roomName[2]);
		divisi.setParent(item);
		
		Listcell cell = new Listcell(room.getVRoomName());
		cell.setParent(item);
	
		roomList.appendChild(item);
		roomList.invalidate();
		doCancel(win);
		MiscTrxController.setFont(roomList);
		
//		getDataRoomList(roomList,MasterServiceLocator.getRoomManager().getAllRoom());
		
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Listbox roomList = (Listbox)win.getFellow("roomList");
		super.doSaveModify(win);
		
		MsRoom room = new MsRoom();
		int selected = roomList.getSelectedIndex();
		room.setNRoomId((Integer)roomList.getSelectedItem().getValue());
		room.setVRoomCode(kode.getText());
		room.setVRoomName(nama.getText());
		MsHall hall = (MsHall)win.getAttribute("hall");
		MsTreatmentClass tclass = (MsTreatmentClass)win.getAttribute("tclass");
		MsWard ward = (MsWard)win.getAttribute("ward");
		room.setMsHall(hall);
		room.setMsTreatmentClass(tclass);
		room.setMsWard(ward);
		if(MasterServiceLocator.getRoomManager().isRoomAlreadyExist(room.getVRoomName())){
			Messagebox.show("NAMA KAMAR "+ room.getVRoomName() + " SUDAH ADA. SILAHKAN GANTI DENGAN YANG LAIN!");
			return;
		}
		MasterServiceLocator.getRoomManager().save(room);
		win.removeAttribute("hall");
		win.removeAttribute("tclass");
		win.removeAttribute("ward");
		Messagebox.show(MessagesService.getKey("common.modify.success"));
		doCancel(win);
		
		Listitem item = new Listitem();
		item.setValue(room.getNRoomId());
		item.setParent(roomList);
		String[] roomName = room.getVRoomName().split("-");
		Listcell id = new Listcell(roomName[0]);
		id.setParent(item);
		String kt = null;
		if(roomName[1].equalsIgnoreCase("0"))
			kt = "VIP";
		else if(roomName[1].equalsIgnoreCase("9"))
			kt = "VVIP";
		else if(roomName[1].equalsIgnoreCase("1"))
			kt = "KELAS I";
		else if(roomName[1].equalsIgnoreCase("2"))
			kt = "KELAS II";
		else if(roomName[1].equalsIgnoreCase("3"))
			kt = "KELAS III";
		Listcell name = new Listcell(kt);
		name.setAttribute("kl", roomName[1]);
		name.setParent(item);
		Listcell divisi = new Listcell(roomName[2]);
		divisi.setParent(item);
		
		Listcell cell = new Listcell(room.getVRoomName());
		cell.setParent(item);
		
		roomList.removeItemAt(selected);
		roomList.appendChild(item);
		roomList.invalidate();
		MiscTrxController.setFont(roomList);
//		getDataRoomList(roomList,MasterServiceLocator.getRoomManager().getAllRoom());
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Listbox ruanganSearchList = (Listbox)win.getFellow("ruanganSearchList");
		Listbox roomList = (Listbox)win.getFellow("roomList");
		Bandbox namaRuangan = (Bandbox)win.getFellow("namaRuangan");
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Textbox crNamaRuangan = (Textbox)win.getFellow("crNamaRuangan");
		super.init(win);
		
		constraints.registerComponent(namaRuangan, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(namaRuangan, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(kode, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(kode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNamaRuangan, ZulConstraint.UPPER_CASE);
		
		constraints.validateComponent(false);
		ruanganSearchList.getItems().clear();
		getDataRoomList(roomList,MasterServiceLocator.getRoomManager().getAllRoom());
		nama.setReadonly(true);
		
		checkDuplicateRoom();
		
		
	}
	
	private void checkDuplicateRoom() throws InterruptedException {
		List roomList = MasterServiceLocator.getRoomManager().getDuplicateRoom();
		if(roomList.size() > 0){
			String roomName = "";
			Iterator it = roomList.iterator();
			while(it.hasNext()){
				Object[] o = (Object[])it.next();
				roomName = roomName + o[1]  +",";
			}
			
			Messagebox.show("KAMAR "+roomName.substring(0, roomName.length()-1) + " DUPLIKAT! SILAHKAN DIPERBAIKI");
		}
		
	}

	public static void getDataRoomList(Listbox roomList, List queryResult){
		
		roomList.getItems().clear();
		MsRoom room = null;
		String kt = null;
				
		Listitem item;
		Iterator it = queryResult.iterator();
		while(it.hasNext()){
			room = (MsRoom)it.next();
			item = new Listitem();
			item.setValue(room.getNRoomId());
			item.setParent(roomList);
			String[] roomName = room.getVRoomName().split("-");
			Listcell id = new Listcell(roomName[0]);
			id.setParent(item);
			if(roomName[1].equalsIgnoreCase("0"))
				kt = "VIP";
			else if(roomName[1].equalsIgnoreCase("9"))
				kt = "VVIP";
			else if(roomName[1].equalsIgnoreCase("1"))
				kt = "KELAS I";
			else if(roomName[1].equalsIgnoreCase("2"))
				kt = "KELAS II";
			else if(roomName[1].equalsIgnoreCase("3"))
				kt = "KELAS III";
			
			Listcell name = new Listcell(kt);
			name.setAttribute("kl", roomName[1]);
			name.setParent(item);
			Listcell divisi = new Listcell(roomName[2]);
			divisi.setParent(item);
			
			Listcell cell = new Listcell(room.getVRoomName());
			cell.setParent(item);
		}
		MiscTrxController.setFont(roomList);
	}
	
	public void getRoomName(Component win) throws InterruptedException, VONEAppException{
		Textbox kode = (Textbox)win.getFellow("id");
		Textbox nama = (Textbox)win.getFellow("name");
		Bandbox namaRuangan = (Bandbox)win.getFellow("namaRuangan");
		
		MsHall hall = MasterServiceLocator.getHallManager().getHallByName(namaRuangan.getText().toUpperCase());
		if(hall == null){
			Messagebox.show(MessagesService.getKey("master.hall.name.notfound"));
			kode.setValue(null);
			return;
		}
		//kena lazy
//		String hallName = hall.getVHallName()+"-"+hall.getMsTreatmentClass().getVTclassCode()+"-"+kode.getText().toUpperCase();
		
		//:kun
		String hallName = MasterServiceLocator.getHallManager().getHallName(namaRuangan.getText().toUpperCase())
			+ "-" + kode.getText().toUpperCase();;
		//:end of kun
		nama.setValue(hallName);
		if(MasterServiceLocator.getRoomManager().isRoomAlreadyExist(hallName)){
			Messagebox.show("NAMA KAMAR "+ hallName + " SUDAH ADA. SILAHKAN GANTI DENGAN YANG LAIN!");
		}
		win.setAttribute("ward", hall.getMsWard());
		win.setAttribute("tclass",hall.getMsTreatmentClass());
		win.setAttribute("hall", hall);
	}
	
	public static void getRoomList(Listbox listbox){
		listbox.getItems().clear();
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(listbox);
		MsRoom room = null;
		
		List list = MasterServiceLocator.getRoomManager().getAllRoom();
		Iterator it = list.iterator();
		while(it.hasNext()){
			room = (MsRoom)it.next();
			item = new Listitem();
			item.setLabel(room.getVRoomName());
			item.setValue(room.getNRoomId());
			item.setParent(listbox);
		}
		listbox.setSelectedItem(listbox.getItemAtIndex(0));
	}
	
	public static void searchRoom(Listbox searchDataList, Textbox name) 
		throws InterruptedException,VONEAppException{
		
		MasterServiceLocator.getRoomManager().searchRoomByName(searchDataList, name.getText().toUpperCase());
//		List result = MasterServiceLocator.getRoomManager().searchRoomByName(name.getText().toUpperCase());
//		if(result.size() == 0){
//			Messagebox.show(MessagesService.getKey("master.common.search.notfound"));
//			return;
//		}
		
		
	}

	
}
