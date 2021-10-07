package com.vone.medisafe.ui.master.treatment;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class TreatmentLabController extends BaseController{
	
	Session session;
	MsUser user;
	
	Listbox categoryList;
	Button btnCari;
	Listbox list;

	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		categoryList = (Listbox)cmp.getFellow("categoryList");
		btnCari = (Button)cmp.getFellow("btnCari");;
		list = (Listbox)cmp.getFellow("list");
		
		session = getCurrentSession();
		user = getUserInfoBean().getMsUser();
		
		MasterServiceLocator.getTreatmentGroupManager().getLabGroup(categoryList);
		getDetailTreatment();
	}
	
	public void setSession(){
		session.setAttribute("user", user);
		session.setAttribute("categoryList", categoryList);
		session.setAttribute("list", list);
	}
	
	public void getDetailTreatment() throws VONEAppException{
		Listitem item = categoryList.getSelectedItem();
		MsTreatmentGroup msTreatmentGroup = (MsTreatmentGroup)item.getValue();
		Service.getLaboratManager().getListboxDetail(list, msTreatmentGroup);
		MiscTrxController.setFont(list);
		if(list.getItems().size()>0)
			list.setSelectedIndex(0);
	}

}
