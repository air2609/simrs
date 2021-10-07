package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;

import com.vone.medisafe.mapping.MsShift;

import com.vone.medisafe.mapping.dao.MsShiftDAO;
import com.vone.medisafe.service.iface.master.ShiftManager;

public class ShiftManagerImpl implements ShiftManager{

	private MsShiftDAO dao;
	
	public MsShiftDAO getDao() {
		return dao;
	}

	public void setDao(MsShiftDAO dao) {
		this.dao = dao;
	}

	public List findAll () throws VONEAppException {
		return this.dao.findAll();
	}
	
	public void save (MsShift pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsShift pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsShift pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
}
