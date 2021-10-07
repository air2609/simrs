package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbUserPrivilege;
import com.vone.medisafe.mapping.TbUserPrivilegeId;
import com.vone.medisafe.ui.admin.UserPrivController;

public interface UserPrivilegeManager {

	public List getAllPrivilege(TbUserPrivilege pojo)throws VONEAppException;
	
	public TbUserPrivilege getUserPrivilege(TbUserPrivilege pojo)throws VONEAppException;
	public List getUserPrivByCriteria(TbUserPrivilege pojo)throws VONEAppException;
	public TbUserPrivilege getById(TbUserPrivilegeId id)throws VONEAppException;
	public List getByUser (String stUserName) throws VONEAppException;
	public void save (TbUserPrivilege pojo) throws VONEAppException;
	public void update (TbUserPrivilege pojo)throws VONEAppException;
	public boolean delete (int userId, int screenId) throws VONEAppException;
	public void redrawList(UserPrivController ctr) throws VONEAppException;
}
