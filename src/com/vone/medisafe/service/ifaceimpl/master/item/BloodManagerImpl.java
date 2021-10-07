package com.vone.medisafe.service.ifaceimpl.master.item;

import java.util.List;

import com.vone.medisafe.mapping.dao.item.BloodDAO;
import com.vone.medisafe.mapping.pojo.item.MsBlood;
import com.vone.medisafe.service.iface.master.item.BloodManager;

public class BloodManagerImpl implements BloodManager{
	
	private BloodDAO dao;
	
	public boolean delete(MsBlood blood) {
		// TODO Auto-generated method stub
		return dao.delete(blood);
	}

	public List getBloods() {
		// TODO Auto-generated method stub
		return dao.getBloods();
	}

	public boolean save(MsBlood blood) {
		// TODO Auto-generated method stub
		return dao.save(blood);
	}

	public BloodDAO getDao() {
		return dao;
	}

	public void setDao(BloodDAO dao) {
		this.dao = dao;
	}

}
