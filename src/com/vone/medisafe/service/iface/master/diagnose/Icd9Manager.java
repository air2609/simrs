package com.vone.medisafe.service.iface.master.diagnose;

import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;

public interface Icd9Manager {
	
	public boolean save(MsIcd9cm icd9) throws VONEAppException;
	
	public boolean delete(MsIcd9cm icd9) throws VONEAppException;
	
	public List getIcd9s() throws VONEAppException;
	
	public List serachIcd9s(MsIcd9cm icd9) throws VONEAppException;

	
	public void serachIcd9ByCodeAndName(Listbox icd9cmList, Textbox code, 
			Textbox treatmentName) throws VONEAppException,InterruptedException;
}
