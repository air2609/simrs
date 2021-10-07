package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCreditCardType;
import com.vone.medisafe.ui.master.CreditCardTypeController;

public interface CreditCardTypeManager {
	
	public List<MsCreditCardType> getCreditCardTypes() throws VONEAppException;
	public boolean save(MsCreditCardType ctype) throws VONEAppException;
	public boolean delete(MsCreditCardType ctype) throws VONEAppException;
	public List getCreditCardTypeBaseOnBank(MsBank bank, short type) throws VONEAppException;
	public void getCreditCardTypes(Listbox cardTypeList) throws VONEAppException;
	
	public void prepareModify(CreditCardTypeController controller) throws VONEAppException;

}
