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
import com.vone.medisafe.mapping.dao.diagnose.MsIcdDAO;
import com.vone.medisafe.mapping.pojo.diagnose.MsIcd;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.iface.master.diagnose.IcdManager;

public class IcdManagerImpl implements IcdManager{
	
	private MsIcdDAO dao;

	public MsIcdDAO getDao() {
		return dao;
	}

	public void setDao(MsIcdDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsIcd icd) {
		// TODO Auto-generated method stub
		return dao.delete(icd);
	}

	public List getIcds() {
		// TODO Auto-generated method stub
		return dao.getIcds();
	}

	public boolean save(MsIcd icd) {
		// TODO Auto-generated method stub
		return dao.save(icd);
	}

	public List serachIcds(MsIcd icd) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.findByExample(icd);
	}

	public List<MsIcd> searchIcd(String icdCode, String icdName) throws VONEAppException {
		
		return dao.searchIcd(icdCode,icdName);
	}

	
	public void serachIcdByCodeAndName(Listbox icdList, Textbox code, Textbox diseaseName) throws VONEAppException, InterruptedException {
		
		boolean selected = false;
		Listitem item;
		Listcell cell;
		
		Set treatments = icdList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		icdList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(icdList);
		}
		
		
		//add search result to listbox, if result size is 1, add result to selection
		
		if(code.getText().trim().equals("") && diseaseName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			code.focus();
			return;
		}
		
		List<MsIcd> list = dao.searchIcd("%"+code.getText()+"%", "%"+diseaseName.getText()+"%");
		
		MsIcd icd = null;
		
		if(list.size() == 1){
			
			icd = list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				
				MsIcd object = (MsIcd)item.getValue();
				if(object.getNIcdId().equals(icd.getNIcdId())){
					icdList.setSelectedItem(item);
					return;
				}
					
			}
			item = new Listitem();
			item.setValue(icd);
			item.setParent(icdList);
			

			cell = new Listcell(icd.getVIcdCode());
			cell.setParent(item);
			
			cell = new Listcell(icd.getVIcdName());
			cell.setParent(item);
			
			icdList.addItemToSelection(item);		
			return;
			
			
		}
		
		for(MsIcd icd2 : list){
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
				
				icd = (MsIcd)item.getValue();
				if(icd.getNIcdId().equals(icd2.getNIcdId()))
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
				item.setValue(icd2);
				item.setParent(icdList);
				
				cell = new Listcell(icd2.getVIcdCode());
				cell.setParent(item);
				
				cell = new Listcell(icd2.getVIcdName());
				cell.setParent(item);
				
				selected = false;
	
				
			}
		}
		
		MiscTrxController.setFont(icdList);
	}

}
