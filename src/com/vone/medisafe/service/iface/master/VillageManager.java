package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsVillage;

public interface VillageManager {
	
	public void save(MsVillage village);
	
	public void update(MsVillage village);
	
	public MsVillage getVillage(MsVillage village);
	
	public MsVillage getVillageById(Integer id);
	
	public void delete(MsVillage village);
	
	public MsVillage getVillageByCode(String code);
	
	public List searchVillage(String villageCode, String villageName);
	
	public boolean deleteById(Integer id);
	
	public void getVillageForSelect(Listbox kelurahanList) throws VONEAppException;

	public void getVillageData(Listbox villageList) throws VONEAppException;
	
	public void getVillageBySubdistrict(Listbox kelurahanList, Listbox kecamatanList) throws VONEAppException;
	
}
