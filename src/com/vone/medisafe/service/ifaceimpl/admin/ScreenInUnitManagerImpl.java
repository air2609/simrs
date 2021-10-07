package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsScreenInUnit;
import com.vone.medisafe.mapping.MsScreenInUnitDAO;
import com.vone.medisafe.service.iface.admin.ScreenInUnitManager;

public class ScreenInUnitManagerImpl implements ScreenInUnitManager{

	private MsScreenInUnitDAO dao;
	
	public MsScreenInUnitDAO getDao() {
		return dao;
	}

	public void setDao(MsScreenInUnitDAO dao) {
		this.dao = dao;
	}

	public List findAll () throws VONEAppException {
		return this.dao.findAll();
	}
	
	public void save (MsScreenInUnit pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsScreenInUnit pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsScreenInUnit pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
	
	public List getUnitByScreenId (Integer scrId) throws VONEAppException{
		return this.dao.getUnitByScreenId(scrId);
	}
}
