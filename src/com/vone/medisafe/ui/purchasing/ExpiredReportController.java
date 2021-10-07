package com.vone.medisafe.ui.purchasing;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class ExpiredReportController extends BaseController{
	
	Listbox listItem;
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		
		super.init(cmp);
		this.listItem = (Listbox)cmp.getFellow("listItem");
		
		Service.getItemInventoryManager().getExpiredReport(listItem);
		
	}

}
