package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsGim;

public interface GimManager {

	public List findByExample (MsGim pojo) throws VONEAppException;
	
	public void save (MsGim pojo) throws VONEAppException;
	
	public void update (MsGim pojo) throws VONEAppException;
	
	public void delete (MsGim pojo) throws VONEAppException;
	
	public MsGim getGimByCode(String code) throws VONEAppException;
	
	public void saveOrUpdate(MsGim pojo) throws VONEAppException;
}
