package com.vone.medisafe.service.iface.admission;



import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbRoomReservation;

public interface AntrianKamarManager {
	
	public void getAntrianList(Listbox roomReservationList) throws VONEAppException;
	
	public void save(Window window) throws VONEAppException, InterruptedException;
	
	public boolean delete(TbRoomReservation antrianKamar) throws VONEAppException;
	
	public void getAntrianBaseOnHallId(Listbox roomReservationList, Integer id) throws VONEAppException;
	
	public void getRegistrationDetil(Window win, int type) throws VONEAppException,InterruptedException;
}
