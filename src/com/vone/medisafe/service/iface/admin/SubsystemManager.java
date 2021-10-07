package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsSubsystem;

public interface SubsystemManager {

	public List getAllSubsystem(MsSubsystem subsystem)throws VONEAppException;
	
	public MsSubsystem getSubsystem(MsSubsystem subsystem)throws VONEAppException;	
	
	public String[] getSubsystemNameDesc (int iSubsystemId)throws VONEAppException;
	
	public String[] getSubsystemNameDesc (Integer iSubsystemId)throws VONEAppException;
	
	public void save(MsSubsystem subsystem);
	
	public void update(MsSubsystem subsystem);
	
	public MsSubsystem getSubsystemById (Integer id)throws VONEAppException;
	
	public void delete(MsSubsystem subsystem);
	
}
