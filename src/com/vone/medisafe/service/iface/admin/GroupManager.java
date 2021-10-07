package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsGroup;

public interface GroupManager {

	public List getAllGroup(MsGroup group) throws VONEAppException;
	public MsGroup getGroup(MsGroup group) throws VONEAppException;
	public void save(MsGroup group) throws VONEAppException;
	public void update(MsGroup group) throws VONEAppException;
	public void delete(MsGroup group) throws VONEAppException;
	public MsGroup getGroupByCode (String groupCode) throws VONEAppException;
}
