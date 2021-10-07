package com.vone.medisafe.service.iface.master.item;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.ui.master.item.VendorController;


public interface VendorManager {

	public List getAllVendor() throws VONEAppException;
	
	public MsVendor getVendorById(Integer id) throws VONEAppException;
	
	public boolean save(MsVendor vendor) throws VONEAppException;
	
	public boolean delete(MsVendor vendor) throws VONEAppException;
	
	public List searchVendor(String code, String name) throws VONEAppException;
	
	public MsVendor getVendorByCode (String code) throws VONEAppException;
	
	public void prepreModify(VendorController controller) throws VONEAppException;
	
	public void searchVendoryByCriteria(VendorController ctr) throws VONEAppException;
}
