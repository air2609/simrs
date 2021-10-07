package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsProvince;

public interface ProvinceManager {
	
	public void save(MsProvince province) throws VONEAppException;
	
	public MsProvince getProvinceById(Integer id) throws VONEAppException;
	
	public void delete(MsProvince province) throws VONEAppException;
	
	public Integer getId() throws VONEAppException;
	
	public MsProvince getProvinceByCode(String code) throws VONEAppException;
	
	public boolean deleteProvincyByid(Integer id) throws VONEAppException;
	
	public void getProvinceForSelect(Listbox propinsiList) throws VONEAppException;

	public List getAllProvince() throws VONEAppException;
}
