package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsInsurance;
import com.vone.medisafe.ui.master.InsuranceController;

public interface InsuranceManager {

	public List findAll () throws VONEAppException;
	
	public void save (MsInsurance pojo) throws VONEAppException;
	
	public void update (MsInsurance pojo) throws VONEAppException;
	
	public void delete (MsInsurance pojo) throws VONEAppException;

	public void redrawList(Listbox list) throws VONEAppException;

	public void prepareModify(InsuranceController controller) throws VONEAppException;
}
