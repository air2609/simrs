package com.vone.medisafe.accounting;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;

public class OpenJournalController  extends BaseController {
	
	JournalManager journalManager = Service.getJournalManager();
	
	Session session;
	MsUser user;
	
	Textbox voucherNo;
	Listbox journalList;
	Datebox dateFrom;
	Datebox dateTo;
		
	public void init(Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
		voucherNo = (Textbox)cmp.getFellow("voucherNo");
		dateFrom = (Datebox)cmp.getFellow("dateFrom");
		dateTo = (Datebox)cmp.getFellow("dateTo");
		journalList = (Listbox) cmp.getFellow("journalList");
		
		session = cmp.getDesktop().getSession();
		session.setAttribute("listbox", journalList);

		user = getUserInfoBean().getMsUser();
	}

	public void openClick() throws VONEAppException, InterruptedException{
		
		if(dateFrom.getValue() == null || dateTo.getValue() == null ){
			
			Messagebox.show("Please Fill Date From and Date To..!");
			return;
		}
		
//		journalManager.getJournal(journalList, voucherNo);
		
		journalManager.getJournalByRange(journalList,voucherNo, dateFrom, dateTo);
	}
}
