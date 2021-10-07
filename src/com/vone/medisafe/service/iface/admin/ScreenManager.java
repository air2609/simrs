package com.vone.medisafe.service.iface.admin;

import java.util.List;
import java.util.Set;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.ui.admin.ScreenController;

public interface ScreenManager {

	public List getAllScreen(MsScreen screen) throws VONEAppException;
	
	public MsScreen getScreen(MsScreen screen) throws VONEAppException; 
	
	public String[] getScreenCodeName (int iScreenId) throws VONEAppException;
	
	public String[] getScreenCodeName (Integer iScreenId) throws VONEAppException;
	
	public void saveScreen (MsScreen screen) throws VONEAppException;

	public void redrawListScreenByCriteria(ScreenController ctr)throws VONEAppException;
	
	public void deleteScreen (MsScreen screen) throws VONEAppException;
	
	public MsScreen getScreenById (Integer id) throws VONEAppException;
	
	public void updateScreen (int iScreenId, String stScreenCode, String stScreenName, int iSubsystemId) throws VONEAppException;
	
	public List searchByCriteria(String screenCode, String screenName) throws VONEAppException;
	
	public MsScreen getScreenByCode(String code) throws VONEAppException;
	
	public boolean save(MsScreen screen) throws VONEAppException;
	
	public boolean update(MsScreen screen) throws VONEAppException;
	
	public void redrawListScreen(ScreenController ctr) throws VONEAppException;
	
	public void doModify(ScreenController ctr) throws VONEAppException;
	
	public Integer getScreenIdByCode (String screenCode) throws VONEAppException;
}
