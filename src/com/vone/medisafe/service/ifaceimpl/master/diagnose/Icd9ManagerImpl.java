package com.vone.medisafe.service.ifaceimpl.master.diagnose;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.dao.diagnose.MsIcd9cmDAO;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd9cm;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.master.diagnose.Icd9Manager;

public class Icd9ManagerImpl implements Icd9Manager{
	
	private MsIcd9cmDAO dao;

	public MsIcd9cmDAO getDao() {
		return dao;
	}

	public void setDao(MsIcd9cmDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsIcd9cm icd9) {
		// TODO Auto-generated method stub
		return dao.delete(icd9);
	}

	public List getIcd9s() {
		// TODO Auto-generated method stub
		return dao.getIcd9s();
	}

	public boolean save(MsIcd9cm icd9) {
		// TODO Auto-generated method stub
		return dao.save(icd9);
	}

	public List serachIcd9s(MsIcd9cm icd9) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.findByExample(icd9);
	}

	
	public void serachIcd9ByCodeAndName(Listbox icd9cmList, Textbox code, Textbox treatmentName) 
		throws VONEAppException, InterruptedException {
		
		boolean selected = false;
		
		Listitem item;
		Listcell cell;
		
		Set treatments = icd9cmList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		icd9cmList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(icd9cmList);
		}
		
		
		//add search result to listbox, if result size is 1, add result to selection
		
		if(code.getText().trim().equals("") && treatmentName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			code.focus();
			return;
		}
		
		List<MsIcd9cm> list = this.dao.searchIcd9("%"+code.getText()+"%", "%"+treatmentName.getText()+"%");
		
		MsIcd9cm icd9 = null;
		
		if(list.size() == 1){
			
			icd9 = list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				
				MsIcd9cm object = (MsIcd9cm)item.getValue();
				if(object.getNIcd9cmId().equals(icd9.getNIcd9cmId()))
					return;
			}
			item = new Listitem();
			item.setValue(icd9);
			item.setParent(icd9cmList);
			

			cell = new Listcell(icd9.getVIcd9cmCode());
			cell.setParent(item);
			
			cell = new Listcell(icd9.getVIcd9cmName());
			cell.setParent(item);
			
			icd9cmList.addItemToSelection(item);		
			return;
			
			
		}
		
		for(MsIcd9cm icd9cm : list){
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				
				icd9 = (MsIcd9cm)item.getValue();
				if(icd9.getNIcd9cmId().equals(icd9cm.getNIcd9cmId()))
				{
					selected = true;
					break;
				}
				else{
					selected = false;
				}
			}
			
			
			if(!selected){
				
				item = new Listitem();
				item.setValue(icd9cm);
				item.setParent(icd9cmList);
				
				cell = new Listcell(icd9cm.getVIcd9cmCode());
				cell.setParent(item);
				
				cell = new Listcell(icd9cm.getVIcd9cmName());
				cell.setParent(item);
				
				selected = false;
	
				
			}
		}
		
		MiscTrxController.setFont(icd9cmList);
		
	}

		
	

	

}
