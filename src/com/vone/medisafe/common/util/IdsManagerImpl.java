package com.vone.medisafe.common.util;

import com.vone.medisafe.common.exception.VONEAppException;

public class IdsManagerImpl implements IdsManager{
	
	private IdsDAO dao;

	public IdsDAO getDao() {
		return dao;
	}

	public void setDao(IdsDAO dao) {
		this.dao = dao;
	}

	public Integer getSequence(String sequenceName) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getSequenceValue(sequenceName);
	}

}
