package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.TbGroupPrivilege;
import com.vone.medisafe.mapping.TbGroupPrivilegeDAO;
import com.vone.medisafe.service.iface.admin.GroupPrivilegeManager;
import com.vone.medisafe.ui.admin.GroupPrivController;

public class GroupPrivilegeManagerImpl implements GroupPrivilegeManager{

	TbGroupPrivilegeDAO dao;
	
	public List getAllPrivilege() {
		// TODO Auto-generated method stub
		return null;
	}

	public TbGroupPrivilege getAllPrivilegeByGroupId(TbGroupPrivilege pojo) throws VONEAppException{
		// TODO Auto-generated method stub
		Collection list = this.dao.findByExample(pojo);
		TbGroupPrivilege usr = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			usr = (TbGroupPrivilege)it.next();
		}
		return usr;
	}
	
	public List getAllPrivilege(TbGroupPrivilege pojo)throws VONEAppException{
		
		return this.dao.findByExample(pojo);
	}
	
	public List getAllPrivilegeByGroupCode(String groupCode) throws VONEAppException{
		return this.dao.findByGroupCode(groupCode);
	}

	public TbGroupPrivilegeDAO getDao() {
		return dao;
	}

	public void setDao(TbGroupPrivilegeDAO dao) {
		this.dao = dao;
	}
	
	public void delete (TbGroupPrivilege pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}

	public void save (TbGroupPrivilege pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (TbGroupPrivilege pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void redrawList(GroupPrivController ctr) throws VONEAppException {
		ctr.getList().getItems().clear();
		
		String stGroupCode = ctr.getGroupCode().getText();
		
		if (!StringUtils.hasValue(stGroupCode)) return;
		
		List listObj = getAllPrivilegeByGroupCode(stGroupCode);
		
		Iterator it = listObj.iterator();
		
		while (it.hasNext()){						
			Listitem item = new Listitem();
			
			Object[] obj = (Object[])it.next();						
			
			TbGroupPrivilege privPojo = (TbGroupPrivilege)obj[0];
			
			item.appendChild(new Listcell(privPojo.getId().getMsScreen().getVScreenCode()));
			item.appendChild(new Listcell(privPojo.getId().getMsScreen().getVDesc()));
			item.appendChild(new Listcell(privPojo.getVAccessType()));
									
			item.setValue(privPojo);
			
			item.setParent(ctr.getList());
		}
	}
}
