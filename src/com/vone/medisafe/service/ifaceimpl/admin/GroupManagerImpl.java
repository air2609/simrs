package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsGroup;
import com.vone.medisafe.mapping.MsGroupDAO;
import com.vone.medisafe.mapping.TbGroupPrivilege;
import com.vone.medisafe.service.iface.admin.GroupManager;
import com.vone.medisafe.ui.admin.GroupPrivController;

public class GroupManagerImpl implements GroupManager{

	private MsGroupDAO dao;

	public MsGroupDAO getDao() {
		return dao;
	}

	public void setDao(MsGroupDAO dao) throws VONEAppException{
		this.dao = dao;
	}

	public List getAllGroup(MsGroup group) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findByExample(group);
	}
	
	public void save(MsGroup group) throws VONEAppException{
		
		this.dao.save(group);
	}
	
	public void update(MsGroup group) throws VONEAppException{
		this.dao.update(group);
	}
	
	public void delete(MsGroup group) throws VONEAppException{
		this.dao.delete(group);
	}

	public MsGroup getGroup(MsGroup group)throws VONEAppException {

		Collection list = this.dao.findByExample(group);
		MsGroup grp = null;
		
		Iterator it = list.iterator();
		while (it.hasNext()){
			grp = (MsGroup)it.next();
		}
		
		return grp;
	}
	
	public MsGroup getGroupByCode (String groupCode) throws VONEAppException {
		
		return this.dao.findByGroupCode(groupCode);
	}
	
}
