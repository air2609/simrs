package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.ui.master.BankController;

public interface BankManager {

	public List findByExample (MsBank pojo) throws VONEAppException;
	
	public void save (MsBank pojo) throws VONEAppException;
	
	public void update (MsBank pojo) throws VONEAppException;
	
	public void delete (MsBank pojo) throws VONEAppException;

	public void redrawList(Listbox list) throws VONEAppException;

	public void prepareModify(BankController controller) throws VONEAppException;
}
