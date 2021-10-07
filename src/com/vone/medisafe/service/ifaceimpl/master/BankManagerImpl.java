package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsBankDAO;
import com.vone.medisafe.service.iface.master.BankManager;
import com.vone.medisafe.ui.master.BankController;

public class BankManagerImpl implements BankManager{

	private MsBankDAO dao;
	
	public MsBankDAO getDao() {
		return dao;
	}

	public void setDao(MsBankDAO dao) {
		this.dao = dao;
	}

	public List findByExample (MsBank pojo) throws VONEAppException {
		return this.dao.findByExample(pojo);
	}
	
	public void save (MsBank pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsBank pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsBank pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}

	public void redrawList(Listbox list) throws VONEAppException {
		list.getItems().clear();
		
		List listBank = findByExample(new MsBank());
		
		Iterator it = listBank.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsBank bankPojo = (MsBank)it.next();
			
			item.appendChild(new Listcell(bankPojo.getVBankName()));			
			item.appendChild(new Listcell(bankPojo.getVBankAccNo()));			
			
			//set COA cell
			Listcell cellCoa = new Listcell(bankPojo.getMsCoa().getVAcctNo());
			cellCoa.setValue(bankPojo.getMsCoa());			
			item.appendChild(cellCoa);
			
			item.appendChild(new Listcell(bankPojo.getVBankContactNo()));
			
			item.setValue(bankPojo);
			
			item.setParent(list);
		}
	}

	
	public void prepareModify(BankController controller) throws VONEAppException {
		
		MsBank bankPojo = (MsBank)controller.list.getSelectedItem().getValue();
		bankPojo = this.dao.findById(bankPojo.getNBankId());
		
		controller.bankName.setText(bankPojo.getVBankName());
		controller.bankAddress.setText(bankPojo.getVBankAddr());
		controller.acctNo.setText(bankPojo.getVBankAccNo());
		controller.telpNo.setText(bankPojo.getVBankContactNo());
		controller.altTelpNo.setText(bankPojo.getVBank2ndCtcNo());
		
		if(bankPojo.getMsCoa() != null){
			
			controller.coa.setValue(bankPojo.getMsCoa().getVAcctNo()+" - "+bankPojo.getMsCoa().getVAcctName());
			controller.coa.setAttribute("coa", bankPojo.getMsCoa());
			
		}		
		
		
		controller.bankName.select();
		
		controller.list.setDisabled(true);
		
	}
}
