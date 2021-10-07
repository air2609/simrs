package com.vone.medisafe.service.ifaceimpl.master.diagnose;

import java.util.List;


import com.vone.medisafe.mapping.dao.diagnose.MsCauseOfDeathDAO;
import com.vone.medisafe.mapping.pojo.diagnose.MsCauseOfDeath;
import com.vone.medisafe.service.iface.master.diagnose.CodManager;

public class CodManagerImpl implements CodManager{
	
	private MsCauseOfDeathDAO dao;
	
	public MsCauseOfDeathDAO getDao() {
		return dao;
	}

	public void setDao(MsCauseOfDeathDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsCauseOfDeath cod) {
		// TODO Auto-generated method stub
		return dao.delete(cod);
	}

	public List getCods() {
		// TODO Auto-generated method stub
		return dao.getCods();
	}

	public boolean save(MsCauseOfDeath cod) {
		// TODO Auto-generated method stub
		return dao.save(cod);
	}

}
