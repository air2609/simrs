package com.vone.medisafe.service.iface.master.treatment;

import java.util.List;

import com.vone.medisafe.mapping.MsTreatmentRoom;
import com.vone.medisafe.mapping.MsTreatmentRoomFee;

public interface TreatmentRoomManager {

	public boolean save(MsTreatmentRoom troom, MsTreatmentRoomFee troomFee);
	public boolean delete(MsTreatmentRoom troom);
	public List getTreatmentRooms();
}
