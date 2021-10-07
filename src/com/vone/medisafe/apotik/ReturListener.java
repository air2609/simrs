package com.vone.medisafe.apotik;

import java.math.BigDecimal;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.services.MessagesService;

public class ReturListener implements EventListener{
	
	private Intbox jumlah;
	private Decimalbox total;
	private double hargaSatuan;
	Listcell cell;
	Listcell maxRetur;
	Listbox list;
	
	public ReturListener(Intbox jumlah, Decimalbox total, double hargaSatuan, Listcell cell, 
			Listcell maxRetur, Listbox list){
		this.jumlah = jumlah;
		this.total = total;
		this.hargaSatuan = hargaSatuan;
		this.cell = cell;
		this.maxRetur = maxRetur;
		this.list = list;
	}

	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0) {
		
		if(jumlah.getValue() == null){
			jumlah.setValue(0);
		}
		
		if(jumlah.getValue().intValue() > new Integer(maxRetur.getLabel())){
			
			try {
				Messagebox.show(MessagesService.getKey("retur.lebih.besar.dari.yang.diizinkan"));
				jumlah.setValue(new Integer(maxRetur.getLabel()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		double totalTmp = this.total.getValue().doubleValue();
		if(jumlah.intValue() < 0){
			try {
				Messagebox.show(MessagesService.getKey("input.negatif.not.allowed"));
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		double nilaiRetur = jumlah.getValue() * hargaSatuan;
		total.setValue(new BigDecimal(nilaiRetur));
		cell.setLabel(total.getText());
		
		
		nilaiRetur = 0;
		Listcell cell;
		List<Listitem> items = list.getItems();
		for(Listitem item : items){
			
			cell = (Listcell)item.getChildren().get(7);
			
			
			total.setText(cell.getLabel());
			
			
			if(total.getValue() != null)
			
				nilaiRetur = nilaiRetur + total.getValue().doubleValue();
			
			
		}
		
		total.setValue(new BigDecimal(nilaiRetur));
		
		
	}

}
