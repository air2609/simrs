package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsRegency;

public interface RegenyManager {
	
	public void save(MsRegency regency);
			
	public MsRegency findById(Integer id);
	
	public void delete(MsRegency regency);
	
	public MsRegency getRegencyByCode(String code);
	
	public boolean deleteById(Integer id);
	
	public List searchRegency(String code, String name);
	
	public void getRegencyForSelect(Listbox kabupatenList) throws VONEAppException;

	public void getRegencyData(Listbox regencyList) throws VONEAppException;
	
	public void getRegencyBaseOnProvince(Listbox kabupatenList, Listbox provinceList) throws VONEAppException;
}
