package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.mapping.TbUserPrivilege;
import com.vone.medisafe.mapping.TbUserPrivilegeDAO;
import com.vone.medisafe.mapping.TbUserPrivilegeId;
import com.vone.medisafe.service.iface.admin.UserPrivilegeManager;
import com.vone.medisafe.ui.admin.UserPrivController;

public class UserPrivilegeManagerImpl implements UserPrivilegeManager{

	TbUserPrivilegeDAO dao;
	
	public TbUserPrivilegeDAO getDao() {
		return dao;
	}

	public void setDao(TbUserPrivilegeDAO dao) {
		this.dao = dao;
	}

	public List getAllPrivilege(TbUserPrivilege pojo) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findByExample(pojo);
	}
	
	public List getByUser (String stUserName) throws VONEAppException{
		return this.dao.findByUser(stUserName);
	}

	public TbUserPrivilege getUserPrivilege(TbUserPrivilege pojo) throws VONEAppException{
		// TODO Auto-generated method stub
		Collection list = this.dao.findByExample(pojo);
		TbUserPrivilege usr = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			usr = (TbUserPrivilege)it.next();
		}
		return usr;
	}
	
	
	public List getUserPrivByCriteria(TbUserPrivilege pojo)throws VONEAppException{
		return this.dao.findByExample(pojo);
	}

	public TbUserPrivilege getById(TbUserPrivilegeId id) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public void save (TbUserPrivilege pojo) throws VONEAppException {
		
		this.dao.save(pojo);
	}
	
	public void update (TbUserPrivilege pojo)throws VONEAppException {
		
		this.dao.update(pojo);
	}
	
	public boolean delete (int userId, int screenId) throws VONEAppException {
		
		return this.dao.delete(userId, screenId);
	}
	
	public void redrawList(UserPrivController ctr) throws VONEAppException {
		ctr.getList().getItems().clear();
		
		String stUserCode = ctr.getUserCode().getText();
		
		if (!StringUtils.hasValue(stUserCode)) return;
		
		List listObj = this.dao.findByUser(stUserCode);
		
		Iterator it = listObj.iterator();
		
		while (it.hasNext()){						
			Listitem item = new Listitem();
			
			Object[] obj = (Object[])it.next();						
			
			TbUserPrivilege privPojo = (TbUserPrivilege)obj[0];
			
			item.appendChild(new Listcell(privPojo.getId().getMsScreen().getVScreenCode()));
			item.appendChild(new Listcell(privPojo.getId().getMsScreen().getVDesc()));
			item.appendChild(new Listcell(privPojo.getVAccessType()));
									
			item.setValue(privPojo.getId().getMsScreen().getNScreenId());
			
			item.setParent(ctr.getList());
		}		
	}	
}
