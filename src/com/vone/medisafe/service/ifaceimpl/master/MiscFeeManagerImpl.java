package com.vone.medisafe.service.ifaceimpl.master;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.dao.MiscfeeDAO;
import com.vone.medisafe.mapping.pojo.MsMiscFee;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.MiscFeeManager;

public class MiscFeeManagerImpl implements MiscFeeManager{
	
	private MiscfeeDAO dao;

	public MiscfeeDAO getDao() {
		return dao;
	}

	public void setDao(MiscfeeDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsMiscFee mfee) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(mfee);
	}

	public List getMiscFees() throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getMiscfees();
	}

	public boolean save(MsMiscFee mfee) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(mfee);
	}

	public void getMiscFees(Listbox miscFeeList) throws VONEAppException {
		miscFeeList.getItems().clear();
		
		Decimalbox fn = new Decimalbox();
		fn.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		
		List list = getMiscFees();
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsMiscFee mfee = (MsMiscFee)it.next();
			
			Listitem item = new Listitem();
			item.setParent(miscFeeList);
			item.setValue(mfee);
			
			Listcell cell = new Listcell(mfee.getVMfeeCode());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeName());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getVMfeeDesc());
			cell.setParent(item);
			
			cell = new Listcell(mfee.getMsPatientType().getVTpatientDesc());
			cell.setParent(item);
			
			fn.setValue(new BigDecimal(mfee.getNMfeePrice()));
			cell = new Listcell(fn.getText());
			cell.setParent(item);
		}
	}

}
