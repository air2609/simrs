package com.vone.medisafe.antrian.bpjs;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.Service;

public class DaftarController {
	
	Rows rowsPoli;
	Rows rowsDoctor;
	Rows rowsQueueFull;
	Vbox vboxLbDoctor;
	Vbox vboxQueueFull;
	Vbox vboxInputBpjs;
	Label lblPoli;
	Label lblPilihPoli;
	Label lblDoctorName;
//	Label nextDate;
	Listbox nextDate;
	
	Textbox bpjsNo, hpNo;
	
	Integer doctorId;
	Date doctorScheduleDate;
	Integer queueNo;
	
	String buttonStyle="color:blue;\r\n" + 
			"    border: 1px solid powderblue;\r\n" + 
			"    padding: 5px;\r\n" + 
			"    background-color: #eeeeee;\r\n" + 
			"    font-weight:bold;\r\n" + 
			"    font-size: 20px;\r\n" + 
			"    width : 450px;\r\n" + 
			"    height : 70px;\r\n" + 
			"    font-family: \"Times New Roman\", Times, serif;";
	String rowStyle="padding-left:15px;padding-top:7px; padding-bottom: 7px;background-color: #ffffff;";
	
	AntrianManager antrianService = Service.getAntrianManager();

	public void init(Component cmp) throws VONEAppException{
		rowsPoli = (Rows)cmp.getFellow("rowsPoli");
		rowsDoctor = (Rows) cmp.getFellow("rowsDoctor");
		rowsQueueFull = (Rows)cmp.getFellow("rowsQueueFull");
		vboxLbDoctor = (Vbox)cmp.getFellow("vboxLbDoctor");
		lblPoli = (Label) cmp.getFellow("lblPoli");
		lblPilihPoli = (Label)cmp.getFellow("lblPilihPoli");
		lblDoctorName = (Label)cmp.getFellow("lblDoctorName");
		nextDate = (Listbox)cmp.getFellow("nextDate");
		vboxQueueFull = (Vbox)cmp.getFellow("vboxQueueFull");
		vboxInputBpjs = (Vbox)cmp.getFellow("vboxInputBpjs");
		
		bpjsNo = (Textbox)cmp.getFellow("bpjsNo");
		hpNo = (Textbox)cmp.getFellow("hpNo");
		
		List<String> polys = antrianService.getPolyNames();
		showPolys(polys);
		
		rowsDoctor.setVisible(false);
		rowsQueueFull.setVisible(false);
		vboxLbDoctor.setVisible(false);
		vboxQueueFull.setVisible(false);
		vboxInputBpjs.setVisible(false);
	}
	
	public void reset() {
		rowsPoli.setVisible(true);
		lblPoli.setVisible(true);
		
		rowsDoctor.setVisible(false);
		rowsQueueFull.setVisible(false);
		vboxLbDoctor.setVisible(false);
		vboxQueueFull.setVisible(false);
		vboxInputBpjs.setVisible(false);
		doctorId = null;
		doctorScheduleDate = null;
		queueNo = null;
		bpjsNo.setValue(null);
		hpNo.setValue(null);
		
		if(nextDate.getItems().size() > 0) nextDate.clearSelection();
	}
	
	public void inputBpjs() {
		vboxQueueFull.setVisible(false);
		vboxInputBpjs.setVisible(true);
	}
	
	private void getDoctorPoli(String poli) throws VONEAppException {
		
		antrianService.getDoctorByPoli(poli, this);
		
	}
	
	public void saveQueue() throws VONEAppException{
		antrianService.saveQueue(this);
	}
	
	public void register(Button doctorBtn) throws VONEAppException {
		antrianService.register(doctorBtn, this);
	}

	private void showPolys(List<String> polys) {
		int counter = 0;
		Row row = null;
		this.rowsPoli.getChildren().clear();
		for(String poli : polys) {
			if(counter %2 == 0) {
				row = new Row();
				row.setStyle(rowStyle);
				row.setParent(rowsPoli);
			}
			Button b = new Button(poli);
			b.setStyle(buttonStyle);
			b.addEventListener("onClick", new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					rowsPoli.setVisible(false);
					rowsDoctor.setVisible(true);
					rowsQueueFull.setVisible(false);
					
					lblPoli.setVisible(false);
					vboxLbDoctor.setVisible(true);
					vboxQueueFull.setVisible(false);
					lblPilihPoli.setValue(b.getLabel());
					getDoctorPoli(b.getLabel());
				}
			});
			b.setParent(row);
			counter = counter + 1;
		}
		
	}

}
