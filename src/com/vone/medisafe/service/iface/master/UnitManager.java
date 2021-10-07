package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.ui.master.UnitController;

public interface UnitManager {

	public boolean save(MsUnit unit) throws VONEAppException ;
	
	
	
	public void delete(MsUnit unit) throws VONEAppException ;
	
	public MsUnit getById(Integer id) throws VONEAppException ;
	
	public MsUnit getUnitByCode(String code) throws VONEAppException ;
	
	public boolean deleteUnitById(Integer id) throws VONEAppException ;
	
	public void getMsUnitForSelect(Listbox listbox) throws VONEAppException;
	
	public void getRegistrationUnit(Listbox listbox) throws VONEAppException;

	public List getUnitForSelect() throws VONEAppException;

	public void getAllUnit(Listbox unitList) throws VONEAppException;



	public List getAllUnit() throws VONEAppException;



	public void prepareModify(UnitController controller) throws VONEAppException;


	
	//public void doModify(UnitController controller) throws VONEAppException;
}
