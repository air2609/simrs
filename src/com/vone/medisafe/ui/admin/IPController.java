package com.vone.medisafe.ui.admin;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.services.SessionManager;

public class IPController {

	public void redrawList(Component cmp){
		Listbox listIp = (Listbox)cmp.getFellow("listIp");
	
		listIp.getItems().clear();
		
		SessionManager sessionManager = SessionManager.getInstance();
		
		List listSession = sessionManager.getSessionList();		
		
		Iterator it = listSession.iterator();
		
		while (it.hasNext()){
			Listitem listItem = new Listitem();
			
			String value = (String)it.next();
			
			listItem.appendChild(new Listcell(value));
			listItem.setValue(value);
			
			listItem.setParent(listIp);
		}
	}
	
	public void invalidate(Component cmp){
		Listbox listIp = (Listbox)cmp.getFellow("listIp");
		
		if (listIp.getSelectedItem() == null){
			try {
				Messagebox.show("Select List First");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			return;
		}
		
		Listitem item = listIp.getSelectedItem();
		
		SessionManager sessionManager = SessionManager.getInstance();	
		
		sessionManager.removeSession((String)item.getValue());
		
		redrawList(cmp);
	}
}
