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
import com.vone.medisafe.common.util.MedisafeConstants;

public class EditReturListener implements EventListener{
	
	private Listitem item;
	private Listbox listbox;
	private Decimalbox total;
	
	Listcell cell;
	Intbox jumlah;
	Decimalbox hargaSatuan;
	
	public EditReturListener(Listitem item, Listbox listbox, Decimalbox total){
		this.item = item;
		this.listbox = listbox;
		this.total = total;
		
		hargaSatuan = new Decimalbox();
		hargaSatuan.setFormat("#,###.##");
	}

	public boolean isAsap() {
		return true;
	}

	public void onEvent(Event arg0) {
		
		int qty = 0;
		cell = (Listcell)item.getChildren().get(6);
		jumlah = (Intbox)cell.getChildren().get(0);
		
		cell = (Listcell)item.getChildren().get(5);
		hargaSatuan.setText(cell.getLabel());
		
		cell = (Listcell)item.getChildren().get(4);
		Integer max = new Integer(cell.getLabel());
		
		if(jumlah.getValue() == null) qty = 0;
		
		else if(jumlah.getValue().intValue() > max.intValue()){
			try {
				Messagebox.show(MessagesService.getKey("retur.lebih.besar.dari.yang.diizinkan"));
				jumlah.setValue(max);
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		else if(jumlah.getValue().intValue() < 0)
		{
			try {
				Messagebox.show(MessagesService.getKey("discount.listener.negatif.notallowed"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		else qty = jumlah.intValue();
		
		
		double nilaiSebelumDiskon = qty * hargaSatuan.getValue().doubleValue();
		hargaSatuan.setValue(new BigDecimal(nilaiSebelumDiskon));
		
		
		cell = (Listcell)item.getChildren().get(7);
		cell.setLabel(hargaSatuan.getText());
		
		double nilaiAkhir = 0;
		List<Listitem> items = listbox.getItems();
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(7);
			hargaSatuan.setText(cell.getLabel());
			nilaiAkhir = nilaiAkhir + hargaSatuan.getValue().doubleValue();
		}
		
		total.setValue(new BigDecimal(nilaiAkhir));
			

	}
	

}
