package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsScreenInUnit;

public interface ScreenInUnitManager {

	public List findAll () throws VONEAppException;
	
	public void save (MsScreenInUnit pojo) throws VONEAppException;
	
	public void update (MsScreenInUnit pojo) throws VONEAppException;
	
	public void delete (MsScreenInUnit pojo) throws VONEAppException;
	
	public List getUnitByScreenId (Integer scrId) throws VONEAppException;
}
