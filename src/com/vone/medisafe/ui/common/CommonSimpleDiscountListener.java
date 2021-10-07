package com.vone.medisafe.ui.common;

import java.math.BigDecimal;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.util.MedisafeConstants;

public class CommonSimpleDiscountListener implements EventListener{

	Decimalbox initPrice = null;
	Decimalbox discAmt = null;
	Listbox discType = null;
	Decimalbox total = null;
	
	private CommonSimpleDiscountListener(){}
	
	public CommonSimpleDiscountListener(Decimalbox initPrice, Decimalbox discAmt, Listbox discType, Decimalbox total){
		this.initPrice = initPrice;
		this.discAmt = discAmt;
		this.discType = discType;
		this.total = total;
	}
	
	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0) {
		// TODO Auto-generated method stub
		
		if (this.initPrice == null || this.discAmt == null|| this.discType == null || this.total == null){
			return;
		}
		
		if (this.initPrice.getValue() == null){
			this.initPrice.setValue(new BigDecimal(0));
		}
		if (this.discAmt.getValue() == null){
			this.discAmt.setValue(new BigDecimal(0));
		}
		if (this.discType.getSelectedItem() == null){
			this.discType.setSelectedIndex(0);
		}
		
		Decimalbox initPrice = new Decimalbox();
		initPrice.setFormat("#,###");
		initPrice.setValue(this.initPrice.getValue());
		double hargaFinal= 0;
		if (MedisafeConstants.RP.equals(discType.getSelectedItem().getValue().toString()))
			hargaFinal = initPrice.getValue().doubleValue() - discAmt.getValue().doubleValue();
		else
		{
			hargaFinal = (discAmt.getValue().doubleValue()/100) * initPrice.getValue().doubleValue();
			hargaFinal = initPrice.getValue().doubleValue() - hargaFinal;
		}
				
		total.setValue(new BigDecimal(hargaFinal));
	}

	public static void manualCalculate(Decimalbox initPrice, Decimalbox discAmt, Listbox discType, Decimalbox total){
		if (initPrice == null || discAmt == null|| discType == null || total == null){
			return;
		}
		
		if (initPrice.getValue() == null){
			initPrice.setValue(new BigDecimal(0));
		}
		if (discAmt.getValue() == null){
			discAmt.setValue(new BigDecimal(0));
		}
		if (discType.getSelectedItem() == null){
			discType.setSelectedIndex(0);
		}
		
		Decimalbox initPrice2 = new Decimalbox();
		initPrice2.setFormat("#,###");
		initPrice2.setValue(initPrice.getValue());
		double hargaFinal= 0;
		if (MedisafeConstants.RP.equals(discType.getSelectedItem().getValue().toString()))
			hargaFinal = initPrice2.getValue().doubleValue() - discAmt.getValue().doubleValue();
		else
		{
			hargaFinal = (discAmt.getValue().doubleValue()/100) * initPrice.getValue().doubleValue();
			hargaFinal = initPrice2.getValue().doubleValue() - hargaFinal;
		}
				
		total.setValue(new BigDecimal(hargaFinal));
	}
}
