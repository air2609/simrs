package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsWard;

public interface WardManager{
	
	public void save(MsWard ward);
	
	public MsWard findById(Integer id);
	
	public void delete(MsWard ward);
	
	public List getAllWard();
	
	public List getWardByCriteria(MsWard ward);
	
	public MsWard getWardByCode(String code);
	
	public boolean deleteById(Integer id);
	
	public List<MsWard> ambilSemuaDataWard() throws VONEAppException;
	
	public void test();
}
