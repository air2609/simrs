package com.vone.medisafe.antrian;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.apotik.ApotikManager;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class KontrolAntrianApotikController extends BaseController{

	Window validatedNoteWin;
	Window obatJadiWin;
	Listbox listPasien1;
	Listbox listPasien2;
	Textbox textAntrian;
	Button btnSave;
	Button btnEdit;
	Button btnRefresh;
	
	ApotikManager manager = Service.getApotikManager();
	
	 @Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		validatedNoteWin = (Window)cmp.getFellow("validatedNoteWin");
		obatJadiWin = (Window)cmp.getFellow("obatJadiWin");
		listPasien1 = (Listbox)validatedNoteWin.getFellow("listPasien1");
		listPasien2 = (Listbox)obatJadiWin.getFellow("listPasien2");
		textAntrian = (Textbox)cmp.getFellow("textAntrian");
		btnSave = (Button)cmp.getFellow("btnSave");
		btnEdit = (Button)cmp.getFellow("btnEdit");
		btnRefresh = (Button)cmp.getFellow("btnRefresh");
		
		manager.getValidatedNoteForAntrian(listPasien1, listPasien2);
		manager.getTextAntrian(btnSave, btnEdit, textAntrian);
	}
	 
	public void save() throws VONEAppException{
		manager.saveAntrian(btnSave, btnEdit, textAntrian);
	}
	
	public void edit() throws VONEAppException{
		btnSave.setDisabled(false);
		btnEdit.setDisabled(true);
		textAntrian.setReadonly(false);
	}
	
	public void refresh() throws VONEAppException{
		manager.getValidatedNoteForAntrian(listPasien1, listPasien2);
	}
}
