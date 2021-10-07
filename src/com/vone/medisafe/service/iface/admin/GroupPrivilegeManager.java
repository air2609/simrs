package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbGroupPrivilege;
import com.vone.medisafe.ui.admin.GroupPrivController;

public interface GroupPrivilegeManager {

	public List getAllPrivilege();
	
	public TbGroupPrivilege getAllPrivilegeByGroupId(TbGroupPrivilege pojo)throws VONEAppException;
	
	public List getAllPrivilege(TbGroupPrivilege pojo)throws VONEAppException;
	
	public List getAllPrivilegeByGroupCode(String groupCode) throws VONEAppException;
	
	public void delete (TbGroupPrivilege pojo) throws VONEAppException;
	
	public void save (TbGroupPrivilege pojo) throws VONEAppException;
	
	public void update (TbGroupPrivilege pojo) throws VONEAppException;
	
	public void redrawList(GroupPrivController ctr) throws VONEAppException;
}
