package com.vone.medisafe.ui.polyclinic;


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

public class EditListener implements EventListener{
	
	private Listitem item;
	private Listbox listbox;
	private Decimalbox total;
	
	Listcell cell;
	Intbox jumlah;
	Decimalbox diskon;
	Listbox opsiDiskon;
	Decimalbox hargaSatuan;
	
	public EditListener(Listitem item, Listbox listbox, Decimalbox total){
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
		
		
		cell = (Listcell)item.getChildren().get(2);
		jumlah = (Intbox)cell.getChildren().get(0);
		
		cell = (Listcell)item.getChildren().get(4);
		hargaSatuan.setText(cell.getLabel());
		
		cell = (Listcell)item.getChildren().get(5);
		diskon = (Decimalbox)cell.getChildren().get(0);
		opsiDiskon = (Listbox)cell.getChildren().get(1);
		
		if(diskon.getValue() == null){
			diskon.setValue(new BigDecimal(0));
		}
		if(jumlah.getValue() == null){
			jumlah.setValue(1);
		}
		
		Integer jumlahSbl = jumlah.getValue(); //todo: tdk bisa ngambil nilai sebelum diedit ??
		BigDecimal diskonSbl = diskon.getValue();
		
		if(diskon.getValue().doubleValue() < 0)
		{
			try {
				Messagebox.show(MessagesService.getKey("discount.listener.negatif.notallowed"));
				diskon.setValue(diskonSbl);
				//diskon.setValue(new BigDecimal(0));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		if(jumlah.getValue().intValue() < 0)
		{
			try {
				Messagebox.show(MessagesService.getKey("discount.listener.jumlah.negatif.notallowed"));
				jumlah.setValue(jumlahSbl);
				//jumlah.setValue(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		double nilaiSebelumDiskon = jumlah.intValue() * hargaSatuan.getValue().doubleValue();
		double nilaiAkhir = 0;
		
		cell = (Listcell)item.getChildren().get(6);
		

		if(opsiDiskon.getSelectedItem().getValue().equals(MedisafeConstants.RP))
		{
			nilaiAkhir = nilaiSebelumDiskon - diskon.getValue().doubleValue();
			if(nilaiAkhir >= 0){
				hargaSatuan.setValue(new BigDecimal(nilaiAkhir));
				cell.setLabel(hargaSatuan.getText());
			}else{
				try {
					Messagebox.show(MessagesService.getKey("discount.listener.jumlah.negatif.notallowed"));
					jumlah.setValue(jumlahSbl);
					diskon.setValue(diskonSbl);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
		}
		else
		{
			
			nilaiAkhir = (diskon.getValue().doubleValue()/100) * nilaiSebelumDiskon;
			nilaiAkhir = nilaiSebelumDiskon - nilaiAkhir;
			
			
			if(nilaiAkhir >= 0){
				hargaSatuan.setValue(new BigDecimal(nilaiAkhir));
				cell.setLabel(hargaSatuan.getText());
			}else{
				try {
					Messagebox.show(MessagesService.getKey("discount.listener.jumlah.negatif.notallowed"));
					jumlah.setValue(jumlahSbl);
					diskon.setValue(diskonSbl);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		nilaiAkhir = 0;
		List<Listitem> items = listbox.getItems();
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(6);
			hargaSatuan.setText(cell.getLabel());
			nilaiAkhir = nilaiAkhir + hargaSatuan.getValue().doubleValue();
		}
		
		if(nilaiAkhir<0){
			try {
				Messagebox.show(MessagesService.getKey("discount.listener.jumlah.negatif.notallowed"));
				//jumlah.setValue(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			total.setValue(new BigDecimal(nilaiAkhir));
		}

	}
	

}
