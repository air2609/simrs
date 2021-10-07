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

public class EditApotikListener implements EventListener{
	
	private Listitem item;
	private Listbox listbox;
	private Decimalbox total;
	
	Listcell cell;
	Intbox jumlah;
	Decimalbox diskon;
	Listbox opsiDiskon;
	Decimalbox hargaSatuan;
	Decimalbox totalSementara;
	Decimalbox totalDiskon;
	
	
	
	public EditApotikListener(Listitem item, Listbox listbox, Decimalbox total, Decimalbox total2, Decimalbox discount){
		this.item = item;
		this.listbox = listbox;
		this.total = total;
		this.totalSementara = total2;
		this.totalDiskon = discount;
		
		hargaSatuan = new Decimalbox();
		hargaSatuan.setFormat("#,###.##");
	}

	public boolean isAsap() {
		return true;
	}

	public void onEvent(Event arg0) {
		
		cell = (Listcell)item.getChildren().get(4);
		jumlah = (Intbox)cell.getChildren().get(0);
		
		cell = (Listcell)item.getChildren().get(6);
		hargaSatuan.setText(cell.getLabel());
		
		cell = (Listcell)item.getChildren().get(7);
		diskon = (Decimalbox)cell.getChildren().get(0);
		opsiDiskon = (Listbox)cell.getChildren().get(1);
		
		
		if((diskon.getValue().doubleValue() < 0) || (jumlah.getValue().intValue() <= 0))
		{
			try {
				Messagebox.show(MessagesService.getKey("discount.listener.negatif.notallowed"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		
		double nilaiSebelumDiskon = jumlah.intValue() * hargaSatuan.getValue().doubleValue();
		double nilaiAkhir = 0;
		
		cell = (Listcell)item.getChildren().get(8);
		

		if(opsiDiskon.getSelectedItem().getValue().equals(MedisafeConstants.RP))
		{
			nilaiAkhir = nilaiSebelumDiskon - diskon.getValue().doubleValue();
			hargaSatuan.setValue(new BigDecimal(nilaiAkhir));
			cell.setLabel(hargaSatuan.getText());
		}
		else
		{
			nilaiAkhir = (diskon.getValue().doubleValue()/100) * nilaiSebelumDiskon;
			nilaiAkhir = nilaiSebelumDiskon - nilaiAkhir;
			hargaSatuan.setValue(new BigDecimal(nilaiAkhir));
			cell.setLabel(hargaSatuan.getText());
		}
		
		nilaiAkhir = 0;
		double totalSebelumDiskon = 0;
		Intbox ibox;
		double jumlah;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		double jumlahTotalDiskon = 0;
		
		List<Listitem> items = listbox.getItems();
		for(Listitem item : items)
		{
			cell = (Listcell)item.getChildren().get(8);
			db.setText(cell.getLabel());
			nilaiAkhir = nilaiAkhir + db.getValue().doubleValue();
			
			//jumlah
			cell = (Listcell)item.getChildren().get(4);
			ibox = (Intbox)cell.getChildren().get(0);
						
			
			cell = (Listcell)item.getChildren().get(6);
			hargaSatuan.setText(cell.getLabel());
			
			jumlah = ibox.getValue().intValue() * hargaSatuan.getValue().doubleValue();
			totalSebelumDiskon = totalSebelumDiskon + jumlah;
			
			jumlahTotalDiskon = jumlahTotalDiskon + (jumlah - db.getValue().doubleValue());
		}
		
		total.setValue(new BigDecimal(nilaiAkhir));
		totalSementara.setValue(new BigDecimal(totalSebelumDiskon));
		totalDiskon.setValue(new BigDecimal(jumlahTotalDiskon));
			

	}
	

}
