package com.vone.medisafe.service.ifaceimpl.common;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.dao.common.NavigationDAO;
import com.vone.medisafe.service.iface.common.NavigationManager;

public class NavigationManagerImpl implements NavigationManager{
	
	private static final Logger log = Logger.getLogger(NavigationManagerImpl.class);

	NavigationDAO dao;
	
	int MAX_PAGE = 0;
	int MAX_RESULT = MedisafeConstants.RESULT_PER_PAGE;
	int CUR_PAGE = 0;

	public NavigationDAO getDao() {
		return dao;
	}

	public void setDao(NavigationDAO dao) {
		this.dao = dao;
	}
	
	public void init(String stQuery, int queryType) throws VONEAppException{
		
		Session session = null;
		
		if (queryType == HQL){
			
			
		}else if (queryType == SQL){
			
		}
	}
	
	public int getMaxResult(String stQuery, int queryType) throws VONEAppException{
		return this.dao.getMaxResult(stQuery, queryType);
	}
}
