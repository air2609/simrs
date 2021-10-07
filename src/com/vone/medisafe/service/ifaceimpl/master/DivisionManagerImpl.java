package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsDivision;
import com.vone.medisafe.mapping.MsDivisionDAO;
import com.vone.medisafe.service.iface.master.DivisionManager;

/**
 * DivisionManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since sep, 06 2006
 * @category service
 */

public class DivisionManagerImpl implements DivisionManager{
	
	MsDivisionDAO dao;

	public MsDivisionDAO getDao() {
		return dao;
	}

	public void setDao(MsDivisionDAO dao) {
		this.dao = dao;
	}

	public void save(MsDivision division) throws VONEAppException{
		// TODO Auto-generated method stub
		this.dao.save(division);
		
	}

	public List getAllDivision() throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.getAllDivision(MsDivision.class);
	}

	public MsDivision getById(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public void delete(MsDivision division) throws VONEAppException{
		// TODO Auto-generated method stub
		this.dao.delete(division);
	}

	public MsDivision getDivisionByCode(String code) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getDivisionByCode(code);
	}

	public boolean deleteById(Integer id) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	

		

}
