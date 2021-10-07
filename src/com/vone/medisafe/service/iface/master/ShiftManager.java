package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsShift;

public interface ShiftManager {

	public List findAll () throws VONEAppException;
	
	public void save (MsShift pojo) throws VONEAppException;
	
	public void update (MsShift pojo) throws VONEAppException;
	
	public void delete (MsShift pojo) throws VONEAppException;
}
