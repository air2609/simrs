package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsBankDAO;
import com.vone.medisafe.mapping.MsGim;
import com.vone.medisafe.mapping.MsGimDAO;
import com.vone.medisafe.service.iface.master.BankManager;
import com.vone.medisafe.service.iface.master.GimManager;

public class GimManagerImpl implements GimManager{

	private MsGimDAO dao;
	
	public MsGimDAO getDao() {
		return dao;
	}

	public void setDao(MsGimDAO dao) {
		this.dao = dao;
	}

	public List findByExample (MsGim pojo) throws VONEAppException {
		return this.dao.findByExample(pojo);
	}
	
	public void save (MsGim pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void saveOrUpdate(MsGim pojo) throws VONEAppException{
		this.dao.saveOrUpdate(pojo);
	}
	
	public void update (MsGim pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsGim pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}
	
	public MsGim getGimByCode(String code) throws VONEAppException {
		return this.dao.getGimByCode(code);
	}
}
