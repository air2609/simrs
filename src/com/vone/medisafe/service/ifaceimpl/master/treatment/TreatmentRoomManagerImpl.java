package com.vone.medisafe.service.ifaceimpl.master.treatment;

import java.util.List;

import com.vone.medisafe.mapping.MsTreatmentRoom;
import com.vone.medisafe.mapping.MsTreatmentRoomFee;
import com.vone.medisafe.mapping.dao.treatment.TreatmentRoomDAO;
import com.vone.medisafe.service.iface.master.treatment.TreatmentRoomManager;

public class TreatmentRoomManagerImpl implements TreatmentRoomManager{
	
	private TreatmentRoomDAO dao;

	public TreatmentRoomDAO getDao() {
		return dao;
	}

	public void setDao(TreatmentRoomDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsTreatmentRoom troom) {
		// TODO Auto-generated method stub
		return dao.delete(troom);
	}

	public List getTreatmentRooms() {
		// TODO Auto-generated method stub
		return dao.getTreatmentRooms();
	}

	public boolean save(MsTreatmentRoom troom, MsTreatmentRoomFee troomFee) {
		// TODO Auto-generated method stub
		return dao.save(troom, troomFee);
	}

}
