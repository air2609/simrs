package com.vone.medisafe.general.authorization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsScreen;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.services.locator.ServiceLocator;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: Jun 4, 2005
 * Time: 12:19:28 AM
 */
public class UserInfoBean implements Serializable{
	
	Logger logger = Logger.getLogger(UserInfoBean.class);
	
	private List listPrivileges;
	private List screenToUnit;
    private String stUserId;
    private Integer intGroupId;
    private String stUserName;
    private Integer intBranchId;
    private MsUser msUser = null;
    
    //screenToUnit contain screenCode+objectUnitCode[]
    //ex. SC0002   UnitCode[]--{"POLYCLINIC ANAK", "POLYCLINIC IBU"}
    // where Unit[] is an array of String of Unit
//    private HashMap screenToUnit;

    /**
     * Constructor
     */
    public UserInfoBean() {

    }

    public String getStUserName() {
        return stUserName;
    }

    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }

	public Integer getIntBranchId() {
		return intBranchId;
	}

	public void setIntBranchId(Integer intBranchId) {
		this.intBranchId = intBranchId;
	}

	public Integer getIntGroupId() {
		return intGroupId;
	}


	public void setIntGroupId(Integer intGroupId) {
		this.intGroupId = intGroupId;
	}


	public String getStUserId() {
        return stUserId;
    }

    public void setStUserId(String stUserId) {
        this.stUserId = stUserId;
    }

	public List getListPrivileges() {
		return listPrivileges;
	}

	public void setListPrivileges(List listPrivileges) {
		this.listPrivileges = listPrivileges;
	}

	public List getScreenToUnit() {
		return screenToUnit;
	}

	/**
	 * set ScreenToUnit List
	 * rid off screens that are not related to user'privileges
	 * @param screenToUnit
	 */
	public void setScreenToUnit(List screenToUnit) {
		if (screenToUnit == null) return;
		
		this.screenToUnit = screenToUnit;
				
		if (this.listPrivileges != null && this.listPrivileges.size() > 0){
			
			List screenList = getListScreen();
			
			Iterator it = this.screenToUnit.iterator();
			
			while (it.hasNext()){
				
				Object[] obj = (Object[])it.next();
								
				if (!screenList.contains(obj[0]))
					it.remove();
			}
		}
		
	}
	
	public List getUnitIdByScreenId(Integer screenId){
		if (this.screenToUnit == null) return null;
		if (screenId == null) return null;
		
		List listUnit = new ArrayList();
		
		Iterator it = this.screenToUnit.iterator();
		
		while (it.hasNext()){
			
			Object[] obj = (Object[])it.next();
			
			if (screenId.equals(obj[0]))
				listUnit.add(obj[1]);
		}
		
		return listUnit;
	}
	
	public List getUnitIdByScreenCode(String screenCode){
		if (this.screenToUnit == null) return null;
		if (screenCode == null) return null;
		
		List listUnit = new ArrayList();
		
		Iterator it = this.screenToUnit.iterator();
		
		while (it.hasNext()){
			
			Object[] obj = (Object[])it.next();			
			
			if (screenCode.equals(obj[2]))
				listUnit.add(obj[1]);
		}
		
		return listUnit;
	}
	
	public List getMsUnitByScreenCode(String screenCode) throws VONEAppException{
		if (this.screenToUnit == null) return null;
		if (screenCode == null) return null;
		
		List listUnit = getUnitIdByScreenCode(screenCode);
		List result = new ArrayList();
		
		Iterator it = listUnit.iterator();
		
		while (it.hasNext())
			result.add(MasterServiceLocator.getUnitManager().getById((Integer)it.next()));			
		
		return result;
	}
	
	public Listbox getUnitListboxByScreenCode(String screenCode){
		if (this.screenToUnit == null) return null;
		if (screenCode == null) return null;
		
		Listbox listBox = new Listbox();		
		
		Iterator it = this.screenToUnit.iterator();
		
		while (it.hasNext()){
			
			Object[] obj = (Object[])it.next();			
			
			if (screenCode.equals(obj[2])){
			
				Listitem listItem = new Listitem();
				listItem.setParent(listBox);
				
				//setValue
				listItem.setValue(obj[1]);
				//setText
				listItem.appendChild(new Listcell(obj[3]+"-"+obj[4]));
			}
		}
		
		return listBox;
	}

	public List getListModule(){
		List list = new ArrayList();
	
		if (this.listPrivileges == null) return null;
		
		Object[] obj = null;
		
		for (int i=0; i<this.listPrivileges.size(); i++){
			if (this.listPrivileges.get(i) instanceof Object[]){			
				obj = (Object[])this.listPrivileges.get(i);

				if (!list.contains(obj[0]))
					list.add(obj[0]);
			}
		}
		return list;
	}
	
	public List getListScreen(){
		List list = new ArrayList();
		 
		if (this.listPrivileges == null) return null;
		
		Object[] obj = null;
		
		for (int i=0; i<this.listPrivileges.size(); i++){
			if (this.listPrivileges.get(i) instanceof Object[]){
				obj = (Object[])this.listPrivileges.get(i);
				
				if (!list.contains(obj[1]))
					list.add(obj[1]);
			}
		}
		return list;
	}
	
	public List getListScreenPerModule(int iModuleId){
		List list = new ArrayList();
		
		if (this.listPrivileges == null) return null;
		
		Object[] obj = null;
		
		for (int i=0; i<this.listPrivileges.size(); i++){
			if (this.listPrivileges.get(i) instanceof Object[]){
			
				obj = (Object[])this.listPrivileges.get(i);
				
				if (!list.contains(obj[1]) && 
						(obj[0] instanceof Integer) &&
						((Integer)obj[0]).intValue() == iModuleId)
					list.add(obj[1]);	
			}
		}
		
		return list;		
	}
	
	public String getScreenPermission(int iScreenId){
		
		if (this.listPrivileges == null) return "";
		
		Object[] obj = null;
		
		for (int i=0; i<this.listPrivileges.size(); i++){
			if (this.listPrivileges.get(i) instanceof Object[]){
			
				obj = (Object[])this.listPrivileges.get(i);
				
				if ( (obj[1] instanceof Integer) && ((Integer)obj[1]).intValue() == iScreenId)
					return (String)obj[2];
			}
		}
		
		return "";			
	}
	
	public String getScreenPermission(String screenCode) throws VONEAppException{
		
		if (this.listPrivileges == null) return "";
		
		MsScreen msScreen = ServiceLocator.getScreenManager().getScreenByCode(screenCode);
		
		if (msScreen == null) return "";
		
		int iScreenId = msScreen.getNScreenId().intValue();		
		
		Object[] obj = null;
		
		for (int i=0; i<this.listPrivileges.size(); i++){
			if (this.listPrivileges.get(i) instanceof Object[]){
			
				obj = (Object[])this.listPrivileges.get(i);
				
				if ( (obj[1] instanceof Integer) && ((Integer)obj[1]).intValue() == iScreenId)
					return (String)obj[2];
			}
		}
		
		return "";			
	}
	
	public boolean isPermitted(String screenCode, String privCode) throws VONEAppException {
		
		if (getScreenPermission(screenCode).equals(privCode))
			return true;
		
		return false;
	}
	
	/**
	 * List ScreenToUnit contains all screen 
	 * This method will filter which screen is valid for specific user
	 */
	public void trimScreenToUnitByPrivilege(){
		
	}
	
	
	
	public MsUser getMsUser(){
		if (msUser == null){
			try {
				msUser = ServiceLocator.getUserManager().getMsUser(this.stUserId);
			} catch (VONEAppException e) {
				// TODO Auto-generated catch block
				logger.error("getMsUser", e);
			}
		}
				
		return this.msUser;
	}
}
