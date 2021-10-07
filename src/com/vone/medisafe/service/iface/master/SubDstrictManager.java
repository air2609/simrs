package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsSubDistrict;

public interface SubDstrictManager {
	
	public void save(MsSubDistrict subdistrict);
	
	public MsSubDistrict getSubDistrictbyId(Integer id);
	
	public void delete(MsSubDistrict subdistrict);
	
	public MsSubDistrict getSubdistrictByCode(String code);
	
	public boolean deleteSubdistrictById(Integer id);
	
	public List searchSubdistrict(String code, String name);
	
	public void getSubDistrictForSelect(Listbox kecamatanList) throws VONEAppException;

	public void getSubdistrictData(Listbox subdistrictList) throws VONEAppException;
	
	public void getSubDistrictByRegency(Listbox kecamatanList, Listbox kabupatenList) throws VONEAppException;
}
