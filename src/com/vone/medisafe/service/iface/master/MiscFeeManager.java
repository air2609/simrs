package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.MsMiscFee;

public interface MiscFeeManager {
	public List getMiscFees() throws VONEAppException;
	public boolean save(MsMiscFee mfee) throws VONEAppException;
	public boolean delete(MsMiscFee mfee) throws VONEAppException;
	public void getMiscFees(Listbox miscFeeList) throws VONEAppException;
}
