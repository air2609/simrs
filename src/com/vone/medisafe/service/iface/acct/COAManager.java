package com.vone.medisafe.service.iface.acct;

import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tree;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsStaff;

public interface COAManager {
	
	public List getCoaHeader() throws VONEAppException;
	
	public List getCoaHeader(int status) throws VONEAppException;
	
	public List getCoaChild(MsCoa headPojo) throws VONEAppException;
	
	public List getCoaChild(MsCoa headPojo, int status) throws VONEAppException;
	
	public List getCoaBaseOnType(int type)throws VONEAppException;
	
	public void save(MsCoa pojo) throws VONEAppException;
	
	public void update(MsCoa pojo) throws VONEAppException;
	
	public MsCoa findByExample(MsCoa instance) throws VONEAppException;
	
	public List getCoaBaseOnTypeNoChild(int type)throws VONEAppException;
	
	public void delete(MsCoa persistentInstance) throws VONEAppException;
	
	public MsCoa getCoaByCode(String code) throws VONEAppException;
	
	public List getCoaType() throws VONEAppException;
	
	public void redrawCoaController(Tree tree, Listbox statusOnScreen) throws VONEAppException;

	public MsCoa getCoaByStaff(MsStaff staff) throws VONEAppException;

	public void searchCoa(String coaCode, String coaName, Listbox coaList) throws VONEAppException;

	public MsCoa checkCoa(String nomorKecoa) throws VONEAppException;
}
