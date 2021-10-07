package com.vone.medisafe.common.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tree;

import com.vone.medisafe.mapping.TbMedicalRecord;

public class HistoryPatientListener implements EventListener{

	private Label mrAndName;
	private Component win;
	private Tabbox tbroot;
	private Listbox histtoryList;
	
	public HistoryPatientListener(Tabbox tbroot, Label mrName, Component winComponent, Listbox historyList){
		this.mrAndName = mrName;
		this.win = winComponent;
		this.tbroot = tbroot;
		this.histtoryList = historyList;
	}
	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0) {
		// TODO Auto-generated method stub
		
		if(tbroot.getSelectedTab().getId().equals("history")){
			
			Session session = win.getDesktop().getSession();
			if(session.getAttribute(MedisafeConstants.PATIENT_HISTROY) != null){
				TbMedicalRecord mr = (TbMedicalRecord)win.getDesktop().getSession().getAttribute("mr");
				
								
				if(mr != null) mrAndName.setValue(mr.getMsPatient().getVPatientName()+" ("+mr.getVMrCode()+")");
				
			}else{
				mrAndName.setValue(null);
				histtoryList.getItems().clear();
				session.removeAttribute("mr");
				Radiogroup rgoption = (Radiogroup)win.getFellow("rgoption");
				Tree tree = (Tree)win.getFellow("treeListPerawatan");
				rgoption.setSelectedIndex(0);
				tree.clearSelection();
			}
			
		}
		
	}

}
