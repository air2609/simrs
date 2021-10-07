package com.vone.medisafe.ui.mr;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class DiagnoseHistoryController {
	
	Label mrNo;
	Label sex;
	Label patientName;
	Label tglLahir;
	Listbox diagoseList;
	Window mainWin;
	
	public void init(Component cmp) throws InterruptedException{
		this.mrNo = (Label)cmp.getFellow("mrNo");
		this.sex = (Label)cmp.getFellow("sex");
		this.patientName = (Label)cmp.getFellow("patientName");
		this.tglLahir = (Label)cmp.getFellow("tglLahir");
		this.diagoseList = (Listbox)cmp.getFellow("diagoseList");
		
		
		
		Page page = cmp.getDesktop().getPage("rmDiagnosePage");
		mainWin = (Window)page.getFellow("");
		page.getId();
		Messagebox.show(page.getId());
//		Messagebox.show(((Bandbox)cmp.getDesktop().getPage("rmDiagnosePage").getFellow("mrNo")).getText());
	}
	
}
