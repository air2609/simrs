package com.vone.medisafe.ui.common;

import java.math.BigDecimal;

import org.zkoss.zk.ui.WrongValueException;
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
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.util.MedisafeConstants;

public class CommonGlobalDiscountListener implements EventListener{
	
	Listitem item = null;
	Listbox discType = null;
	Decimalbox initPrice = null;
	Intbox qty = null;
	Decimalbox discAmt = null;
	Label finalPrice = null;
	
	private CommonGlobalDiscountListener(){}
	
	/**
	 * 
	 * @param item
	 * @param discountType
	 * @param initPrice
	 * @param discount
	 * @param finalPrice
	 * @param quantity
	 */
	public CommonGlobalDiscountListener(Listitem item, Listbox discType, Decimalbox initPrice, Intbox qty, Decimalbox discAmt, Label finalPrice){
		this.item = item;
		this.discType = discType;
		this.initPrice = initPrice;
		this.qty = qty;
		this.discAmt = discAmt;
		this.finalPrice = finalPrice;
	}

	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0){
		// TODO Auto-generated method stub
		

		if(discAmt.getValue() == null){
			discAmt.setValue(new BigDecimal(0));				
		}
		if (initPrice.getValue() == null){
			initPrice.setValue(new BigDecimal(0));
		}
		if (qty.getValue() == null){
			qty.setValue(new Integer(0));
		}

		
		if(arg0 instanceof SelectEvent){
			try{
				Decimalbox initPrice = new Decimalbox();
				initPrice.setFormat("#,###");
				initPrice.setValue(this.initPrice.getValue());
				double hargaFinal= 0;
				if (MedisafeConstants.RP.equals(discType.getSelectedItem().getValue().toString()))
					hargaFinal = initPrice.getValue().doubleValue()*qty.getValue().intValue() - discAmt.getValue().doubleValue();
				else
				{
					hargaFinal = ((discAmt.getValue().doubleValue()/100) * initPrice.getValue().doubleValue())*qty.getValue().intValue();
					hargaFinal = initPrice.getValue().doubleValue()*qty.getValue().intValue() - hargaFinal;
				}
					
				
				initPrice.setValue(new BigDecimal(hargaFinal));
				finalPrice.setValue(initPrice.getText());
				Object[] obj = new Object[]{this.initPrice.getValue(),
						discType.getSelectedItem().getValue().toString(),qty,discAmt.getValue(),new BigDecimal(hargaFinal)};
				
				item.setAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE,obj );
							
					
			}catch (Exception e) {
				// 
			}
			
		}
		else{
			Decimalbox initPrice = new Decimalbox();
			initPrice.setFormat("#,###");
			initPrice.setValue(this.initPrice.getValue());
			double hargaFinal= 0;
			if (MedisafeConstants.RP.equals(discType.getSelectedItem().getValue().toString()))
				hargaFinal = initPrice.getValue().doubleValue()*qty.getValue().intValue() - discAmt.getValue().doubleValue();
			else
			{
				hargaFinal = ((discAmt.getValue().doubleValue()/100) * initPrice.getValue().doubleValue())*qty.getValue().intValue();
				hargaFinal = initPrice.getValue().doubleValue()*qty.getValue().intValue() - hargaFinal;
			}
			
			initPrice.setValue(new BigDecimal(hargaFinal));
			finalPrice.setValue(initPrice.getText());
			
			Object[] obj = new Object[]{this.initPrice.getValue(),
					discType.getSelectedItem().getValue().toString(),qty, discAmt.getValue(),new BigDecimal(hargaFinal)};
			item.setAttribute(MedisafeConstants.COMMON_DISCOUNT_ATTRIBUTE,obj );
		}
		
	}
	
	public static BigDecimal getFinalAmount (Object obj){
		
		if (obj instanceof Object[]){
		
			Object[] row = (Object[])obj;
			return (BigDecimal)row[4];
		}
		
		return null;
	}
}
