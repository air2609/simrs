package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsRoomDAO;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.RoomManager;

public class RoomManagerImpl implements RoomManager{
	
	private MsRoomDAO dao;

	/**
	 * @return Returns the dao.
	 */
	public MsRoomDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(MsRoomDAO dao) {
		this.dao = dao;
	}

	public void save(MsRoom room) {
		// TODO Auto-generated method stub
		dao.save(room);
	}

	public void delete(MsRoom room) {
		// TODO Auto-generated method stub
		dao.delete(room);
	}

	public MsRoom getRoomById(Integer id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	public List getAllRoom() {
		// TODO Auto-generated method stub
		return dao.getAllRoom(MsRoom.class);
	}

	public List getRoomsByHall(MsHall hall) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getRoomByHall(hall);
	}

	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	public void searchRoomByName(Listbox roomList, String name) {
		
		List<MsRoom> result = dao.searchRoomByName(name);
		
		roomList.getItems().clear();
		
		Listitem item;
		Listcell cell;
				
		
		for(MsRoom room : result){
			
			item = new Listitem();
			item.setValue(room.getNRoomId());
			item.setParent(roomList);
			String[] roomName = room.getVRoomName().split("-");
			
			cell = new Listcell(room.getVRoomName());
			cell.setValue(room.getMsHall().getVHallCode());
			cell.setParent(item);
			
			cell = new Listcell(roomName[1]);
			cell.setValue(room.getMsTreatmentClass().getNTclassId());
			cell.setParent(item);
			
			cell = new Listcell(roomName[2]);
			cell.setParent(item);
		}
		
		result.clear();
	}

	public MsRoom getByName(String name) {
		// TODO Auto-generated method stub
		return dao.getRoomByName(name);
	}

	@Override
	public List getDuplicateRoom() {
		// TODO Auto-generated method stub
		return dao.getDuplicateRoom();
	}

	@Override
	public boolean isRoomDuplicate(String roomName) {
		List roomList = this.dao.getListRoomByName(roomName);
		if(roomList.size() > 1) return true;
		return false;
	}

	@Override
	public boolean isRoomAlreadyExist(String roomName) {
		List roomList = this.dao.getListRoomByName(roomName);
		if(roomList.size() > 0) return true;
		return false;
	}

	@Override
	public void fillRoomDetail(Listbox roomList, Bandbox namaRuangan,
			Textbox kode, Textbox nama) throws InterruptedException{
		
		Integer roomId = (Integer)roomList.getSelectedItem().getValue();
		MsRoom room = this.dao.findById(roomId);
		
		if(room.getMsHall() != null){
			namaRuangan.setValue(room.getMsHall().getVHallName());
			String tclassCode = room.getMsHall().getMsTreatmentClass().getVTclassCode();
			String roomCode = room.getVRoomCode();
			kode.setValue(roomCode);
			String roomName = room.getMsHall().getVHallName()+"-"+tclassCode+"-"+roomCode;
			nama.setValue(roomName);
			
			if(MasterServiceLocator.getRoomManager().isRoomDuplicate(roomName)){
				Messagebox.show("KAMAR "+ roomName + " DUPLIKAT. SILAHKAN DIPERBAIKI!!!");
			}
		}
		else{
			String roomCode = room.getVRoomCode();
			kode.setValue(roomCode);
		}
		
		
		
	}

}
