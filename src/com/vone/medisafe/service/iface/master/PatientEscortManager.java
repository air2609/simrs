package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatientEscort;
import com.vone.medisafe.mapping.MsWarehouse;

public interface PatientEscortManager {

	public List findAll () throws VONEAppException;
	
	public void save (MsPatientEscort pojo) throws VONEAppException;
	
	public void update (MsPatientEscort pojo) throws VONEAppException;
	
	public void delete (MsPatientEscort pojo) throws VONEAppException;
}
