package com.vone.medisafe.common.util;

import java.math.BigDecimal;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.services.MessagesService;


public class DiscontListener implements EventListener{
	
	private Listitem item;
	private Listbox diskonOption;
	private Label price;
	private Decimalbox input;
	private Listcell finalPrice;
	private Intbox quantity;
	private Decimalbox jumlah;
	private Short jasaR;
	
	/**
	 * @param item
	 * @param discountList
	 * @param price
	 * @param input
	 * @param finalPrice
	 * @param quantity
	 * @param jasaR 
	 */
	public DiscontListener(Listitem item,Listbox discountList, Label price, Decimalbox input, Listcell finalPrice,Intbox quantity, Short x){
		this.item = item;
		this.diskonOption=discountList;
		this.price=price;
		this.input=input;
		this.finalPrice=finalPrice;
		this.quantity = quantity;
		this.jumlah = (Decimalbox)item.getParent().getAttribute("jumlah");
		this.jasaR = x;
	}

	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0){
		// TODO Auto-generated method stub
		jumlah.setValue(null);
		double pengurang = 0;
		if(input.getValue() != null){
			pengurang = input.getValue().doubleValue();
			if(input.getValue().doubleValue() < 0){
				try {
					Messagebox.show(MessagesService.getKey("discount.listener.negatif.notallowed"));
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		if(arg0 instanceof SelectEvent){
			try{
				
				Decimalbox price = new Decimalbox();
				price.setFormat("#,###");
				price.setText(this.price.getValue());
				double hargaFinal= 0;

								
				if(diskonOption.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
					if(pengurang > price.getValue().doubleValue()){
						Messagebox.show(MessagesService.getKey("common.discount.not.allowed"));
						finalPrice.setLabel(price.getText());
						return;
					}
					hargaFinal = quantity.getValue() * price.getValue().doubleValue() - pengurang;
				}
			
				else
				{
					hargaFinal = (pengurang/100) * price.getValue().doubleValue();
					if(hargaFinal > price.getValue().doubleValue()){
						Messagebox.show(MessagesService.getKey("common.discount.not.allowed"));
						finalPrice.setLabel(price.getText());
						return;
					}
					hargaFinal = quantity.getValue() * price.getValue().doubleValue() - hargaFinal;
				}
					
				
				price.setValue(new BigDecimal(hargaFinal));
				finalPrice.setLabel(price.getText());
				Object[] obj = new Object[]{new BigDecimal(price.getValue().doubleValue()),new BigDecimal(hargaFinal),
						diskonOption.getSelectedItem().getValue().toString(),quantity.getValue(),new BigDecimal(pengurang), jasaR};
				
				item.setAttribute("manipulation",obj );
							
					
			}catch (Exception e) {
				// TODO: handle exception Auto-generated
			}
			
		}
		else{
			Decimalbox price = new Decimalbox();
			price.setFormat("#,###");
//			String a = this.price.getValue();
//			System.out.println(a);
			price.setText(this.price.getValue());
			double hargaFinal= 0;
			if(diskonOption.getSelectedItem().getValue().toString().equals(MedisafeConstants.RP)){
				if(pengurang > price.getValue().doubleValue()){
					try {
						Messagebox.show(MessagesService.getKey("common.discount.not.allowed"));
						finalPrice.setLabel(price.getText());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				hargaFinal = quantity.getValue() * price.getValue().doubleValue() - pengurang;
			}
			else
			{
				hargaFinal = (pengurang/100) * price.getValue().doubleValue();
				if(hargaFinal > price.getValue().doubleValue()){
					try {
						Messagebox.show(MessagesService.getKey("common.discount.not.allowed"));
						finalPrice.setLabel(price.getText());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				hargaFinal = quantity.getValue() * price.getValue().doubleValue() - hargaFinal;
			}
			
			price.setValue(new BigDecimal(hargaFinal));
			finalPrice.setLabel(price.getText());
			
			Object[] obj = new Object[]{new BigDecimal(price.getValue().doubleValue()),new BigDecimal(hargaFinal),
					diskonOption.getSelectedItem().getValue().toString(),quantity.getValue(), new BigDecimal(pengurang), jasaR};
			item.setAttribute("manipulation",obj );
		}		
	}
}
