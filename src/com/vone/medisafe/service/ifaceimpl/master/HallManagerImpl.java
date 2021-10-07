package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsHall;
import com.vone.medisafe.mapping.MsHallDAO;
import com.vone.medisafe.mapping.MsRoom;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.service.iface.master.HallManager;
import com.vone.medisafe.ui.common.PencarianPasientRanapController;

public class HallManagerImpl implements HallManager{
	
	private MsHallDAO dao;

	public MsHallDAO getDao() {
		return dao;
	}

	public void setDao(MsHallDAO dao) {
		this.dao = dao;
	}

	public void save(MsHall hall) throws VONEAppException {
		// TODO Auto-generated method stub
		dao.save(hall);
	}

	public void delete(MsHall hall) throws VONEAppException {
		// TODO Auto-generated method stub
		dao.delete(hall);
	}

	public MsHall getById(Integer id) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	public List getAllHall() throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getAll(MsHall.class);
	}


//	public List getHallAndBedByTClassId(MsTreatmentClass tclasId) {
//		// TODO Auto-generated method stub
//		return dao.getHallAndBedByTClassId(tclasId);
//	}
	public List getHallAndBedByTClassId(MsTreatmentClass tclass) throws VONEAppException{
		return null;
	}
	
	public boolean deleteById(Integer Id) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.deleteById(Id);
	}

	public List searchHall(String name) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.searchHall(name);
	}

	public MsHall getHallByName(String name) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getByCode(name);
	}

	public void getAllHall(Listbox hallList) throws VONEAppException {
		List list = dao.getAll(MsHall.class);
		hallList.getItems().clear();
		MsHall hall;
		Listitem item;
		Iterator it = list.iterator();
		while(it.hasNext()){
			hall = (MsHall)it.next();
			item = new Listitem();
			item.setValue(hall);
			item.setParent(hallList);
			Listcell name = new Listcell(hall.getVHallName());
			name.setParent(item);
			Listcell divisi = new Listcell(hall.getMsTreatmentClass().getVTclassDesc());
			divisi.setParent(item);
			
		}
	}

	public void searchHall(String text, Listbox searchDataList) throws VONEAppException {
		searchDataList.getItems().clear();
		MsHall hall = null;
		Iterator it = dao.searchHall(text).iterator();
		
		while(it.hasNext()){
			hall = (MsHall)it.next();
			Listitem item = new Listitem();
			item.setValue(hall.getNHallId());
			item.setParent(searchDataList);
			
			Listcell hallName = new Listcell(hall.getVHallName());
			hallName.setParent(item);
			Listcell tclassName = new Listcell(hall.getMsTreatmentClass().getVTclassDesc());
			tclassName.setValue(hall.getMsTreatmentClass().getVTclassCode());
			tclassName.setParent(item);
		}
	}

	
	
	
	
	public void searchHall(PencarianPasientRanapController controller) throws VONEAppException {
		
		controller.hallList.getItems().clear();
		
		Listitem item;
		Listcell cell;
		
		List<MsHall> list = dao.searchHall("%"+controller.code.getText()+"%", "%"+controller.
												hallName.getText()+"%");
		
		for(MsHall msHall : list){
			
			item = new Listitem();
			item.setValue(msHall);
			item.setParent(controller.hallList);
			
			cell = new Listcell(msHall.getVHallCode());
			cell.setParent(item);
			
			cell = new Listcell(msHall.getVHallName());
			cell.setParent(item);
			
		}
		
	}

	
	public MsHall getHallByRoom(MsRoom room) throws VONEAppException {
		
		return this.dao.getHallByRoom(room);
		
	}

	public String getHallName(String name) throws VONEAppException {
		MsHall hall = dao.getByCode(name);
		
		return hall.getVHallName()+"-"+hall.getMsTreatmentClass().getVTclassCode();
	}


}
