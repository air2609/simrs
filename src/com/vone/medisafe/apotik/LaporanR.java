package com.vone.medisafe.apotik;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanR extends BaseController{
	
	Datebox dateFrom;
	Datebox dateTo;
	Listbox rList;
	Intbox nilaiR, jumlahR;
	
	NoteManager manager = Service.getNotaManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		dateFrom = (Datebox)cmp.getFellow("dateFrom");
		dateTo = (Datebox) cmp.getFellow("dateTo");
		rList = (Listbox)cmp.getFellow("rList");
		nilaiR = (Intbox)cmp.getFellow("nilaiR");
		jumlahR = (Intbox)cmp.getFellow("jumlahR");
	}
	
	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	public void openClick() throws VONEAppException{
		
		manager.getNoteByRangeDate(dateFrom, dateTo, rList, jumlahR, nilaiR);
	}

}
