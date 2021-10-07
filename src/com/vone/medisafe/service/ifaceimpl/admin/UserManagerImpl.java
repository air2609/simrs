package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.LogoutListener;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.SessionManager;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.StringUtils;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.MsUserDAO;
import com.vone.medisafe.mapping.TbUserPrivilege;
import com.vone.medisafe.mapping.TbUserPrivilegeDAO;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.ui.admin.UserController;
import com.vone.medisafe.ui.admin.UserPrivController;


public class UserManagerImpl implements UserManager{
	
	Logger logger = Logger.getLogger(UserManagerImpl.class);
	
	private MsUserDAO dao;
	private TbUserPrivilegeDAO privdao;
	
	public TbUserPrivilegeDAO getPrivdao() {
		return privdao;
	}

	public void setPrivdao(TbUserPrivilegeDAO privdao) {
		this.privdao = privdao;
	}

	public MsUserDAO getDao() {
		return dao;
	}

	public void setDao(MsUserDAO dao) {
		this.dao = dao;
	}

	public List getAllUser(MsUser user) throws VONEAppException{
		// TODO Auto-generated method stub
		return this.dao.findByExample(user);
	}

	public MsUser getUser(MsUser user) throws VONEAppException{
		Collection list = this.dao.findByExample(user);
		MsUser usr = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			usr = (MsUser)it.next();
		}
		return usr;
	}
	
	public MsUser getUserById (Integer id) throws VONEAppException{
		return this.dao.findById(id);
	}
	
	public boolean isValidUser(String stUserName, String stPassword) throws VONEAppException{						
		
		MsUser userPojo = this.dao.getMsUser(stUserName);
		
		if (userPojo == null)
			return false;
		
		
		if ((new MD5(stPassword).toString()).equals(userPojo.getVPassword()))
		{
			Sessions.getCurrent().setAttribute(MedisafeConstants.USER_SESSION, userPojo);
			return true;
		}	
			
		
		return false;
	}

	public MsUser getMsUser (String stUserName) throws VONEAppException{
		
		return this.dao.getMsUser(stUserName);
	}
	
	public UserInfoBean getUserInfoBean(String stUserName)throws VONEAppException {
		
		UserInfoBean uib = new UserInfoBean();
		
		uib.setListPrivileges(this.dao.getPrivileges(stUserName));		

		uib.setScreenToUnit(this.dao.getScreenToUnit(stUserName));
		
		MsUser userPojo = getMsUser(stUserName);
		
		
		if (userPojo != null){
			uib.setStUserId(userPojo.getVUserName());
			uib.setStUserName(userPojo.getVUserFullName());
			uib.setIntGroupId(userPojo.getMsGroup().getNGroupId());
		
			
		}		
		
		return uib;
	}
	
	public List getScreenToUnit(String stUserName) throws VONEAppException{
		return this.dao.getScreenToUnit(stUserName);
	}
	
	public void delete(MsUser user)throws VONEAppException {
		// TODO Auto-generated method stub
	
		this.dao.delete(user);
	}

	public void save(MsUser user)throws VONEAppException{
		// TODO Auto-generated method stub
		
		this.dao.save(user);
	}

	public void update(MsUser user) throws VONEAppException{
		// TODO Auto-generated method stub
		this.dao.update(user);
	}

	
	
	public void validateLogin(String userId, String password) throws VONEAppException {
		
		if (!this.isValidUser(userId, password))
			throw new VONEAppException(MessagesService.getKey("login.invalid"));
		
		Session session = Sessions.getCurrent();
		session.setAttribute(Constant.USER_INFO, this.getUserInfoBean(userId));				
		
		//log IP address to SessionManager
		SessionManager sessionManager = SessionManager.getInstance();
		sessionManager.setSession(session.getClientAddr(), session);
		
		//create Logout Listener
        LogoutListener listener = new LogoutListener(session.getClientAddr());
        session.setAttribute("listener", listener);				        
        
        logger.debug("USER ID : "+userId);
		        
		Executions.sendRedirect("index.zul");
	}

	
	public void redrawList(UserController ctr) throws VONEAppException {
		ctr.getList().getItems().clear();
		
		List listUser = this.dao.findByExample(new MsUser());
		
		Iterator it = listUser.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsUser userPojo = (MsUser)it.next();
			
			item.appendChild(new Listcell(userPojo.getVUserName()));
			item.appendChild(new Listcell(userPojo.getVUserFullName()));
			
			Listcell cellGroup = new Listcell();
			cellGroup.setLabel(userPojo.getMsGroup().getVGroupName());
			cellGroup.setValue(userPojo.getMsGroup().getNGroupId());
			cellGroup.setParent(item);
			
			Listcell cellStaff = new Listcell();
			cellStaff.setLabel(userPojo.getMsStaff().getVStaffCode());
			cellStaff.setValue(userPojo.getMsStaff().getNStaffId());
			cellStaff.setParent(item);
			
			if (userPojo.getMsBranch() != null){
				Listcell cellBranch = new Listcell();
				cellBranch.setLabel(userPojo.getMsBranch().getVBranchName());
				cellBranch.setValue(userPojo.getMsBranch().getNBranchId());
				item.appendChild(cellBranch);
			}
			
			item.setValue(userPojo.getNUserId());
			
			item.setParent(ctr.getList());
		}
	}
	
	public void getUnitUser(MsUser user, Listbox locationList) throws VONEAppException {
		
		user = this.dao.findById(user.getNUserId());
		
		locationList.getItems().clear();
		
		MsStaffInUnit siu;
		Listitem item;
		
		Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray(); 
		for(int i=0; i < obj.length; i++){
			siu = (MsStaffInUnit)obj[i];
			item = new Listitem();
			item.setValue(siu.getId().getMsUnit());
			item.setLabel(siu.getId().getMsUnit().getVUnitCode()+"-"+siu.getId().getMsUnit().getVUnitName());
			item.setParent(locationList);
		}
		
		
		locationList.setSelectedIndex(0);
		
	}

	
	public void search(String input, Listbox listUser) throws VONEAppException {
	
		List<MsUser> userList = this.dao.searchUser(input);
		Listitem item;
		Listcell cell;
		
		listUser.getItems().clear();
		
		for(MsUser user : userList){
			
			item = new Listitem();
			item.setValue(user.getNUserId());
			item.setParent(listUser);
			
			
			cell = new Listcell(user.getVUserName());
			cell.setParent(item);
			
			cell = new Listcell(user.getVUserFullName());
			cell.setParent(item);
			
			cell = new Listcell(user.getMsGroup().getVGroupName());
			cell.setParent(item);
			
			cell = new Listcell(user.getMsStaff().getVStaffCode());
			cell.setParent(item);
			
			if(user.getMsBranch() != null)
				cell = new Listcell(user.getMsBranch().getVBranchName());
			else cell = new Listcell();
			cell.setParent(item);
			
		}
		
	}
	
}
