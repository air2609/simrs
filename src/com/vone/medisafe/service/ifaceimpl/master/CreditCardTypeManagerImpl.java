package com.vone.medisafe.service.ifaceimpl.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsCreditCardType;
import com.vone.medisafe.mapping.dao.CreditCardTypeDAO;
import com.vone.medisafe.service.iface.master.CreditCardTypeManager;
import com.vone.medisafe.ui.master.CreditCardTypeController;

public class CreditCardTypeManagerImpl implements CreditCardTypeManager{
	
	private CreditCardTypeDAO dao;

	public CreditCardTypeDAO getDao() {
		return dao;
	}

	public void setDao(CreditCardTypeDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsCreditCardType ctype) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.delete(ctype);
	}

	public List<MsCreditCardType> getCreditCardTypes() throws VONEAppException {
		// TODO Auto-generated method stub
		List<MsCreditCardType> hasil = new ArrayList<MsCreditCardType>();
		
		
		List result = dao.getCreditCardTypes();
		
		Iterator itr = result.iterator();
		while(itr.hasNext()){
			MsCreditCardType ctype = (MsCreditCardType)itr.next();
			hasil.add(ctype);
		}
		
		return hasil;
	}

	public boolean save(MsCreditCardType ctype) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.save(ctype);
	}

	public List getCreditCardTypeBaseOnBank(MsBank bank, short type) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getCreditCardTypeBaseOnBank(bank, type);
	}

	public void getCreditCardTypes(Listbox cardTypeList) throws VONEAppException {
		List<MsCreditCardType> list = getCreditCardTypes();
		
		for(MsCreditCardType card : list){
			Listitem item = new Listitem();
			item.setValue(card);
			item.setParent(cardTypeList);
			
			Listcell cell = new Listcell(card.getMsBank().getVBankName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.convert2CardType(card.getNPaymentType()));
			cell.setParent(item);
			
			cell = new Listcell(card.getNCcTypeDesc());
			cell.setParent(item);
			
			cell = new Listcell(card.getMsCoa().getVAcctNo()+"-"+card.getMsCoa().getVAcctName());
			cell.setParent(item);
				
		}
	}

	
	public void prepareModify(CreditCardTypeController controller) throws VONEAppException {
		
		MsCreditCardType ctype = (MsCreditCardType)controller.cardTypeList.getSelectedItem().getValue();
		
		for(int i=1; i < controller.bankLis.getItems().size(); i++){
			if(ctype.getMsBank().getNBankId().equals(((MsBank)controller.bankLis.getItemAtIndex(i).getValue())
					.getNBankId())){
				controller.bankLis.setSelectedIndex(i);
			}
		}
		
		MsCoa msCoa = ctype.getMsCoa();
		if(msCoa != null){
			controller.coa.setText(msCoa.getVAcctNo()+" - "+ msCoa.getVAcctName());
			controller.coa.setAttribute("coa", msCoa);
		}
		
		
		for(int i=1; i < controller.cardList.getItems().size(); i++){
			if(ctype.getNPaymentType().toString().equals(controller.cardList.getItemAtIndex(i).
					getValue().toString())){
				controller.cardList.setSelectedIndex(i);
			}
		}
		
		controller.cardName.setValue(ctype.getNCcTypeDesc());
		
	}

}
