package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.mapping.MsMedicStaffGroup;
import com.vone.medisafe.mapping.MsMedicStaffGroupDAO;
import com.vone.medisafe.service.iface.master.MedicStaffGroupManager;

public class MedicStaffGroupManagerImpl implements MedicStaffGroupManager{

	private MsMedicStaffGroupDAO dao;
	
	/**
	 * @return Returns the dao.
	 */
	public MsMedicStaffGroupDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(MsMedicStaffGroupDAO dao) {
		this.dao = dao;
	}

	public void save(MsMedicStaffGroup msg) {
		// TODO Auto-generated method stub
		this.dao.save(msg);
	}

	public void delete(MsMedicStaffGroup msg) {
		// TODO Auto-generated method stub
		this.dao.delete(msg);
	}

	public MsMedicStaffGroup getById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public List getAllMedicStaffGroup() {
		// TODO Auto-generated method stub
		return this.dao.getAllMedicStaffGroup(MsMedicStaffGroup.class);
	}

	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	public MsMedicStaffGroup getByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getByCode(code);
	}

}
