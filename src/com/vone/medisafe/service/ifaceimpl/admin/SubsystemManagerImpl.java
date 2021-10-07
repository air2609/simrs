package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsSubsystem;
import com.vone.medisafe.mapping.MsSubsystemDAO;
import com.vone.medisafe.service.iface.admin.SubsystemManager;

public class SubsystemManagerImpl implements SubsystemManager{

	MsSubsystemDAO dao;

	public List getAllSubsystem(MsSubsystem subsystem) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findByExample(subsystem);
	}

	public MsSubsystemDAO getDao() {
		return dao;
	}

	public void setDao(MsSubsystemDAO dao) {
		this.dao = dao;
	}

	public MsSubsystem getSubsystemById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public void update(MsSubsystem subsystem) {
		// TODO Auto-generated method stub
		this.dao.save(subsystem);
	}

	public MsSubsystem getSubsystem(MsSubsystem subsystem) throws VONEAppException{
		// TODO Auto-generated method stub
		Collection list = this.dao.findByExample(subsystem);
		MsSubsystem usr = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			usr = (MsSubsystem)it.next();
		}
		return usr;
	}	
	
	public String[] getSubsystemNameDesc (int iSubsystemId)throws VONEAppException{
		
		return this.dao.getModuleNameDesc(iSubsystemId);
	}
	
	public String[] getSubsystemNameDesc (Integer iSubsystemId)throws VONEAppException{
		
		return this.dao.getModuleNameDesc(iSubsystemId.intValue());
	}

	public void save(MsSubsystem subsystem) {
		// TODO Auto-generated method stub
		this.dao.saveOnly(subsystem);
	}

	public void delete(MsSubsystem subsystem) {
		// TODO Auto-generated method stub
		this.dao.delete(subsystem);
	}

}
