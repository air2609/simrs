package com.vone.medisafe.service.iface.master.diagnose;

import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;

public interface IcdManager {
	
	public boolean save(MsIcd icd);
	
	public boolean delete(MsIcd icd);
	
	public List getIcds();
	
	public List serachIcds(MsIcd icd) throws VONEAppException;
	
	public List<MsIcd> searchIcd(String icdCode, String icdName) throws VONEAppException;

	public void serachIcdByCodeAndName(Listbox icdList, Textbox code, Textbox diseaseName)
		throws VONEAppException,InterruptedException;
}
