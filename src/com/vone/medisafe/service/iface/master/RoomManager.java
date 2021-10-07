package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsRoom;

public interface RoomManager {
	
	public void save(MsRoom room);
	
	public void delete(MsRoom room);
	
	public MsRoom getRoomById(Integer id);
	
	public List getAllRoom();
	
	public void searchRoomByName(Listbox searchDataList, String name) throws VONEAppException;
	
	public List getRoomsByHall(MsHall hall) throws VONEAppException;
	
	public boolean deleteById(Integer id);
	
	public MsRoom getByName(String name);
	
	public List getDuplicateRoom();
	
	public boolean isRoomDuplicate(String roomName);
	
	public boolean isRoomAlreadyExist(String roomName);

	public void fillRoomDetail(Listbox roomList, Bandbox namaRuangan,
			Textbox kode, Textbox nama) throws InterruptedException;
}
