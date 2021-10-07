package com.vone.medisafe.service.iface.admin;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.ui.admin.UserController;
import com.vone.medisafe.ui.admin.UserPrivController;

public interface UserManager {
	
	public List getAllUser(MsUser user)throws VONEAppException;
	
	public MsUser getUser(MsUser user)throws VONEAppException;
	
	public boolean isValidUser(String stUserName, String stPassword)throws VONEAppException;
	
	public UserInfoBean getUserInfoBean(String stUserName)throws VONEAppException;
	
	public MsUser getMsUser (String stUserName)throws VONEAppException;
	
	public void save(MsUser user)throws VONEAppException;
	
	public void update(MsUser user)throws VONEAppException;
	
	public void delete(MsUser user)throws VONEAppException;
	
	public MsUser getUserById(Integer id)throws VONEAppException;
	
	public List getScreenToUnit(String stUserName) throws VONEAppException;

	public void validateLogin(String userId, String password) throws VONEAppException;

	public void getUnitUser(MsUser user, Listbox locationList) throws VONEAppException;
	
	public void redrawList(UserController ctr) throws VONEAppException;

	public void search(String input, Listbox listUser) throws VONEAppException;
	
}
