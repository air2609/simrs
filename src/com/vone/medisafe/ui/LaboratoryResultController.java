package com.vone.medisafe.ui;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LaboratoryResultController {
	public static void onLoad(Window win){
		Textbox labResultNumber = (Textbox)win.getFellow("labResultNumber");
		Listbox LabaroratoryResultList =(Listbox)win.getFellow("LabaroratoryResultList");
		
		labResultNumber.setDisabled(true);
		LabaroratoryResultList.getItems().clear();
	
	}	
	
	public static void Save(Window win){
		Textbox MRNumber = (Textbox)win.getFellow("MRNumber");
		Textbox labResultNumber = (Textbox)win.getFellow("labResultNumber");
		Textbox patientName = (Textbox)win.getFellow("patientName");
		Bandbox transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		Textbox age = (Textbox)win.getFellow("age");
		Textbox mainDoctor = (Textbox)win.getFellow("mainDoctor");
		Listbox LabaroratoryResultList =(Listbox)win.getFellow("LabaroratoryResultList");
		
		/*if(MRNumber.getValue()!=null){
			try {
				Messagebox.show("nilai yang dilempar "+MRNumber.getValue());
			} catch (WrongValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
		}	*/
		MRNumber.setDisabled(true);
		labResultNumber.setDisabled(true);
		patientName.setDisabled(true);
		transactionNumber.setDisabled(false);
		age.setDisabled(true);
		mainDoctor.setDisabled(true);
		LabaroratoryResultList.setDisabled(true);
		
			
	}	
	
	public static void NewPages(Window win){
		Textbox MRNumber = (Textbox)win.getFellow("MRNumber");
		Textbox labResultNumber = (Textbox)win.getFellow("labResultNumber");
		Textbox patientName = (Textbox)win.getFellow("patientName");
		Bandbox transactionNumber = (Bandbox)win.getFellow("transactionNumber");
		Textbox age = (Textbox)win.getFellow("age");
		Textbox mainDoctor = (Textbox)win.getFellow("mainDoctor");
		Listbox LabaroratoryResultList =(Listbox)win.getFellow("LabaroratoryResultList");
		
		MRNumber.setValue("");
		labResultNumber.setValue("");
		patientName.setValue("");
		transactionNumber.setValue("");
		age.setValue("");
		mainDoctor.setValue("");
		LabaroratoryResultList.getItems().clear();
	
		transactionNumber.focus();
		
		MRNumber.setDisabled(false);
		labResultNumber.setDisabled(true);
		patientName.setDisabled(false);
		transactionNumber.setDisabled(false);
		age.setDisabled(false);
		mainDoctor.setDisabled(false);
		LabaroratoryResultList.setDisabled(false);
		
	}

}
