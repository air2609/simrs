package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;

import com.vone.medisafe.mapping.MsPatientEscort;

import com.vone.medisafe.mapping.dao.MsPatientEscortDAO;
import com.vone.medisafe.service.iface.master.PatientEscortManager;

public class PatientEscortManagerImpl implements PatientEscortManager{

	private MsPatientEscortDAO dao;
	
	public MsPatientEscortDAO getDao() {
		return dao;
	}

	public void setDao(MsPatientEscortDAO dao) {
		this.dao = dao;
	}

	public List findAll () throws VONEAppException {
		return this.dao.findAll();
	}
	
	public void save (MsPatientEscort pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsPatientEscort pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsPatientEscort pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
}
