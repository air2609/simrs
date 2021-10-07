package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;


import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.ui.common.PencarianPasientRanapController;

public interface HallManager {
	
	public void save(MsHall hall) throws VONEAppException;
	
	public void delete(MsHall hall) throws VONEAppException;
	
	public MsHall getById(Integer id) throws VONEAppException;
	
	public List getAllHall()throws VONEAppException;
	
	public List searchHall(String name) throws VONEAppException;
	
	public List getHallAndBedByTClassId(MsTreatmentClass tclass) throws VONEAppException;
	
	public MsHall getHallByName(String name) throws VONEAppException;
	
	public boolean deleteById(Integer Id) throws VONEAppException;
	
	public void getAllHall(Listbox hallList) throws VONEAppException;
	
	public void searchHall(String text, Listbox searchDataList) throws VONEAppException;
	
	public void searchHall(PencarianPasientRanapController controller) throws VONEAppException;

	public MsHall getHallByRoom(MsRoom room) throws VONEAppException;

	public String getHallName(String name) throws VONEAppException;
	
	
}
