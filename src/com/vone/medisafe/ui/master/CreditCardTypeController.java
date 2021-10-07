package com.vone.medisafe.ui.master;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsBank;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsCreditCardType;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.master.CreditCardTypeManager;
import com.vone.medisafe.ui.base.BaseController;

public class CreditCardTypeController extends BaseController{
	
	private MsCreditCardType ctype;

	public Listbox bankLis;
	public Bandbox coa;
	public Listbox cardList;
	public Textbox cardName;
	public Listbox cardTypeList;
	
	Listitem item;
	Listcell cell;
	
	ZulConstraint cons = new ZulConstraint();
	
	CreditCardTypeManager serv = Service.getCreditCardTypeManager();
			
	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
		
		bankLis.setSelectedIndex(0);
		coa.setText("");
		coa.removeAttribute("coa");
		cardList.setSelectedIndex(0);
		cardName.setValue(null);
		
	}

	@Override
	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	@Override
	public void doDelete(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doDelete(cmp);
		if(this.cardTypeList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		int index = cardTypeList.getSelectedIndex();
		int result = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if(result == Messagebox.YES){
			if(serv.delete((MsCreditCardType)this.cardTypeList.getSelectedItem().getValue())){
				Messagebox.show(MessagesService.getKey("common.delete.success"));
				cardTypeList.removeItemAt(index);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.delete.fail"));
			}
		}
	}

	@Override
	public void doModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doModify(cmp);
		if(this.cardTypeList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		
		serv.prepareModify(this);
		
		
	}

	@Override
	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!cons.validateComponent(true)) return;
		super.doSave(cmp);
		
	}

	@Override
	public void doSaveAdd(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveAdd(cmp);
		
		ctype = new MsCreditCardType();
		ctype.setNCcTypeDesc(cardName.getValue());
		ctype.setMsBank((MsBank)bankLis.getSelectedItem().getValue());
		ctype.setMsCoa((MsCoa)coa.getAttribute("coa"));
		ctype.setNPaymentType(new Short(cardList.getSelectedItem().getValue().toString()));
		ctype.setVWhoCreate(getUserInfoBean().getStUserName());
		ctype.setDWhnCreate(new Date());
		
		if(serv.save(ctype)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item = new Listitem();
			item.setValue(ctype);
			item.setParent(cardTypeList);
			
			cell = new Listcell(ctype.getMsBank().getVBankName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.convert2CardType(ctype.getNPaymentType()));
			cell.setParent(item);
			
			cell = new Listcell(ctype.getNCcTypeDesc());
			cell.setParent(item);
			
			cell = new Listcell(coa.getText());
			cell.setParent(item);
			doCancel(cmp);
		}
		
	}

	@Override
	public void doSaveModify(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doSaveModify(cmp);
		
		item = this.cardTypeList.getSelectedItem();
		ctype = (MsCreditCardType)item.getValue();
		ctype.setNCcTypeDesc(cardName.getValue());
		ctype.setMsBank((MsBank)bankLis.getSelectedItem().getValue());
		ctype.setMsCoa((MsCoa)coa.getAttribute("coa"));
		ctype.setNPaymentType(new Short(cardList.getSelectedItem().getValue().toString()));
				
		if(serv.save(ctype)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			item.getChildren().clear();
			item.setValue(ctype);
						
			cell = new Listcell(ctype.getMsBank().getVBankName());
			cell.setParent(item);
			
			cell = new Listcell(MedisafeUtil.convert2CardType(ctype.getNPaymentType()));
			cell.setParent(item);
			
			cell = new Listcell(ctype.getNCcTypeDesc());
			cell.setParent(item);
			
			cell = new Listcell(coa.getText());
			cell.setParent(item);
			doCancel(cmp);
		}
	}

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		bankLis = (Listbox)win.getFellow("bankLis");
		coa = (Bandbox)win.getFellow("coa");
		cardList = (Listbox)win.getFellow("cardList");
		cardName = (Textbox)win.getFellow("cardName");
		cardTypeList = (Listbox)win.getFellow("cardTypeList");
		
		cardTypeList.getItems().clear();
		
		cons.registerComponent(bankLis, ZulConstraint.NO_EMPTY);
		cons.registerComponent(coa, ZulConstraint.NO_EMPTY);
		cons.registerComponent(cardList, ZulConstraint.NO_EMPTY);
		cons.registerComponent(cardName, ZulConstraint.NO_EMPTY);
		
		cons.registerComponent(cardName, ZulConstraint.UPPER_CASE);
		
		cons.validateComponent(false);
		
		BankController.getBanks(bankLis);
//		CoaController.getCoaForSelect(coaList, MedisafeConstants.COA_ALL);
		getCardTypes();
	}
	
	
	public void getCardTypes() throws VONEAppException{
		
		serv.getCreditCardTypes(cardTypeList);
		
//		List<MsCreditCardType> list = serv.getCreditCardTypes();
//		
//		for(MsCreditCardType card : list){
//			item = new Listitem();
//			item.setValue(card);
//			item.setParent(cardTypeList);
//			
//			cell = new Listcell(card.getMsBank().getVBankName());
//			cell.setParent(item);
//			
//			cell = new Listcell(MedisafeUtil.convert2CardType(card.getNPaymentType()));
//			cell.setParent(item);
//			
//			cell = new Listcell(card.getNCcTypeDesc());
//			cell.setParent(item);
//			
//			cell = new Listcell(card.getMsCoa().getVAcctNo()+"-"+card.getMsCoa().getVAcctName());
//			cell.setParent(item);
//				
//		}
		
	}
	
	
	public static void getCardTypesForSelect(Listbox cards, MsBank bank, short type) throws VONEAppException
	{
		cards.getItems().clear();
		Listitem item = new Listitem();
		item.setParent(cards);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		cards.setSelectedIndex(0);
		
		MsCreditCardType ctype = null;
		
		CreditCardTypeManager serv = Service.getCreditCardTypeManager();
		
		List result = serv.getCreditCardTypeBaseOnBank(bank, type);
		Iterator it = result.iterator();
		while(it.hasNext()){
			ctype = (MsCreditCardType)it.next();
			item = new Listitem();
			item.setValue(ctype);
			item.setParent(cards);
			item.setLabel(ctype.getNCcTypeDesc());
		}
	}
	

}
