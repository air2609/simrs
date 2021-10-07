package com.vone.medisafe.service.iface.master;

import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.ui.master.StaffController;

public interface StaffManager {
	
	public boolean save(MsStaff staff, Set units) throws VONEAppException;
	public boolean delete(MsStaff staff) throws VONEAppException;
	public MsStaff getByStaffId(Integer id) throws VONEAppException;
	public List getAllStaff() throws VONEAppException;
	public List getStaffByCriteria(MsStaff staff) throws VONEAppException;
	public List getStaffByRole(short role) throws VONEAppException;
	public MsStaff getStaffByCode(String code) throws VONEAppException;
	public List searchStaff(String code,String name) throws VONEAppException;
	
	public void searchStaff(String code,String name, Listbox staffList) throws VONEAppException;
	
	public void getStaffByRole(Listbox dataStaffList) throws VONEAppException;
	
	public void prepareModify(StaffController controller) throws VONEAppException;
	
	

}
