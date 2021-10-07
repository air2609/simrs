package com.vone.medisafe.ui;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LaboratoryController {
	public static void onLoad(Window win){
		Textbox registrationNumber = (Textbox)win.getFellow("registrationNumber");
		Textbox transactionNumber = (Textbox)win.getFellow("transactionNumber");
		Listbox laboratoryList =(Listbox)win.getFellow("laboratoryList");
		
		registrationNumber.setDisabled(true);
		transactionNumber.setDisabled(false);
		laboratoryList.getItems().clear();
	
	}	
	
	public static void Save(Window win){
		Checkbox referencePatient = (Checkbox)win.getFellow("referencePatient");
		Bandbox MRNumber = (Bandbox)win.getFellow("MRNumber");
		Textbox registrationNumber = (Textbox)win.getFellow("registrationNumber");
		Textbox patientName = (Textbox)win.getFellow("patientName");
		Textbox transactionNumber = (Textbox)win.getFellow("transactionNumber");
		Radio male = (Radio)win.getFellow("male");
		Radio female = (Radio)win.getFellow("female");
		Textbox address = (Textbox)win.getFellow("address");
		Listbox patientType = (Listbox)win.getFellow("patientType");
		Datebox dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
		Textbox age = (Textbox)win.getFellow("age");
		Textbox mainDoctor = (Textbox)win.getFellow("mainDoctor");
		Textbox patientEscort = (Textbox)win.getFellow("patientEscort");
		Listbox laboratoryList =(Listbox)win.getFellow("laboratoryList");
		
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
		referencePatient.setDisabled(true);
		MRNumber.setDisabled(true);
		registrationNumber.setDisabled(true);
		patientName.setDisabled(true);
		transactionNumber.setDisabled(false);
		male.setDisabled(true);
		female.setDisabled(true);
		patientType.setDisabled(true);
		address.setDisabled(true);
		dateOfBirth.setDisabled(true);
		age.setDisabled(true);
		mainDoctor.setDisabled(true);
		patientEscort.setDisabled(true);
		laboratoryList.setDisabled(true);
		
			
	}	
	
	public static void NewPages(Window win){
		Checkbox referencePatient = (Checkbox)win.getFellow("referencePatient");
		Bandbox MRNumber = (Bandbox)win.getFellow("MRNumber");
		Textbox registrationNumber = (Textbox)win.getFellow("registrationNumber");
		Textbox patientName = (Textbox)win.getFellow("patientName");
		Textbox transactionNumber = (Textbox)win.getFellow("transactionNumber");
		Radio male = (Radio)win.getFellow("male");
		Radio female = (Radio)win.getFellow("female");
		Textbox address = (Textbox)win.getFellow("address");
		Listbox patientType = (Listbox)win.getFellow("patientType");
		Datebox dateOfBirth = (Datebox)win.getFellow("dateOfBirth");
		Textbox age = (Textbox)win.getFellow("age");
		Textbox mainDoctor = (Textbox)win.getFellow("mainDoctor");
		Textbox patientEscort = (Textbox)win.getFellow("patientEscort");
		Listbox laboratoryList =(Listbox)win.getFellow("laboratoryList");
		
		referencePatient.setChecked(false);
		MRNumber.setValue("");
		registrationNumber.setValue("");
		patientName.setValue("");
		transactionNumber.setValue("");
		age.setValue("");
		male.setChecked(false);
		female.setChecked(false);
		address.setValue("");
		patientType.clearSelection(); 
		dateOfBirth.setValue(null);
		mainDoctor.setValue("");
		patientEscort.setValue("");
		laboratoryList.getItems().clear();
	
		MRNumber.focus();
		
		referencePatient.setDisabled(false);
		MRNumber.setDisabled(false);
		registrationNumber.setDisabled(true);
		patientName.setDisabled(false);
		transactionNumber.setDisabled(false);
		male.setDisabled(false);
		female.setDisabled(false);
		patientType.setDisabled(false);
		address.setDisabled(false);
		dateOfBirth.setDisabled(false);
		age.setDisabled(false);
		mainDoctor.setDisabled(false);
		patientEscort.setDisabled(false);
		laboratoryList.setDisabled(false);
		
	}
}
