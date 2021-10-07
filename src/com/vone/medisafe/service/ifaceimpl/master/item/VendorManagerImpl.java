package com.vone.medisafe.service.ifaceimpl.master.item;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.dao.item.VendorDAO;
import com.vone.medisafe.mapping.pojo.item.MsVendor;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.item.VendorManager;
import com.vone.medisafe.ui.master.item.VendorController;

public class VendorManagerImpl implements VendorManager{
	private VendorDAO dao;
	
	public VendorDAO getDao() {
		return dao;
	}

	public void setDao(VendorDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsVendor vendor) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(vendor);
	}

	public List getAllVendor() throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getAllVendor();
	}

	public boolean save(MsVendor vendor) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(vendor);
	}
	
	public List searchVendor(String code, String name) throws VONEAppException{
		return this.dao.searchVendor(code, name);
	}
	
	public void searchVendoryByCriteria(VendorController ctr) throws VONEAppException  {
		
		ctr.getSupplierList().getItems().clear();
		Listitem item = null;
		Listcell cell = null;
		MsVendor vendor = null;
		
		List vendors = this.dao.searchVendorByCriteria(ctr.getCariTextbox().getText());

		Iterator it = vendors.iterator();
		while(it.hasNext()){
			vendor = (MsVendor)it.next();
			item = new Listitem();
			item.setValue(vendor);
			item.setParent(ctr.getSupplierList());
			
			cell = new Listcell(vendor.getVVendorCode());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorName());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorAddress());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactPerson());
			cell.setParent(item);
			
			cell = new Listcell(vendor.getVVendorContactNo());
			cell.setParent(item);
		}
		MiscTrxController.setFont(ctr.getSupplierList());
		
		
	}

	public MsVendor getVendorByCode (String code) throws VONEAppException{
		return this.dao.getVendorByCode(code);
	}

	
	public void prepreModify(VendorController controller) throws VONEAppException {
		
		MsVendor vendor = (MsVendor)controller.supplierList.getSelectedItem().getValue();
		
		vendor = this.dao.getVendorByCode(vendor.getVVendorCode());
		
		controller.vendorCode.setValue(vendor.getVVendorCode());
		controller.vendorName.setValue(vendor.getVVendorName());
		controller.vendorAddress.setValue(vendor.getVVendorAddress());
		controller.telpNo.setValue(vendor.getVVendorContactNo());
		controller.alvTelpNo.setValue(vendor.getVVendorAltContactNo());
		controller.faxNo.setValue(vendor.getVVendorFaxNo());
		controller.contactPerson.setValue(vendor.getVVendorContactPerson());
		
		MsCoa msCoa = vendor.getMsCoa();
		if(msCoa != null){
			controller.coa.setText(msCoa.getVAcctNo()+" - "+msCoa.getVAcctName());
			controller.coa.setAttribute("coa", msCoa);
		}
		
	}

	@Override
	public MsVendor getVendorById(Integer id) throws VONEAppException {
		
		return dao.getVendorById(id);
	}
}
