package com.vone.medisafe.ui.common;

import java.math.BigDecimal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.ui.base.BaseController;

public class CommonMiscController extends BaseController{
	Textbox name;
	Intbox qty;
	Decimalbox price;
	Session session;
	Listbox listbox;
	TbMiscTrx tbMiscTrx;

	Component win;
	private Listitem item;
	
	Listitem listitem;
	Listcell listcell;
	Label harga;

	ZulConstraint constraints = new ZulConstraint();

	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		name = (Textbox)win.getFellow("name");
		qty = (Intbox)win.getFellow("qty");
		price = (Decimalbox)win.getFellow("price");

		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
		constraints.registerComponent(name, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(name, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(qty, ZulConstraint.NO_EMPTY);
		constraints.registerComponent(price, ZulConstraint.NO_EMPTY);
		
		constraints.validateComponent(false);
		
	}
	
	public void getMisc() throws InterruptedException{
//		if(qty.getValue() == null){
//			Messagebox.show("QTY HARUS DIISI");
//			return;
//		}
//		if(price.getValue() == null){
//			Messagebox.show("HARGA HARUS DIISI");
//			return;
//		}
		if(!constraints.validateComponent(true))
			return;
		
		tbMiscTrx = new TbMiscTrx();
		tbMiscTrx.setVMiscName(name.getText());
		tbMiscTrx.setNQty(qty.getValue().shortValue());
		tbMiscTrx.setNItemPrice(price.getValue().doubleValue());

		listitem = new Listitem();
		listitem.setValue(tbMiscTrx);
		listitem.setParent(listbox);
		
		
			
		
		//CODE
		listcell = new Listcell(MedisafeConstants.MISC_CODE);
		listcell.setParent(listitem);
		
		//KET
		listcell = new Listcell(tbMiscTrx.getVMiscName());
		listcell.setParent(listitem);
		
		//QTY
		listcell = new Listcell(""+qty.getValue().intValue());
		listcell.setParent(listitem);
		
		//SATUAN
		listcell = new Listcell("-");
		listcell.setParent(listitem);
		
		//HARGA
		Decimalbox dbharga = new Decimalbox();
		dbharga.setValue(price.getValue());
		dbharga.setFormat("#,###");
		harga = new Label(dbharga.getText());
		listcell = new Listcell(harga.getValue());
		listcell.setParent(listitem);
		
		//DISKON
		Listcell diskon  = new Listcell();
		diskon.setParent(listitem);
		Decimalbox decimalbox = new Decimalbox();
		decimalbox.setParent(diskon);
		decimalbox.setValue(new BigDecimal(0));
		decimalbox.setWidth("50%");
		decimalbox.setStyle("font-size:8pt");
		decimalbox.setFormat("#,###");
		decimalbox.setHeight("14px");
		Listbox diskonList = new Listbox();
		diskonList.setParent(diskon);
		diskonList.setWidth("42%");
		diskonList.setMold("select");
		diskonList.setStyle("font-size:9pt");
		item = new Listitem();
		item.setValue(MedisafeConstants.RP);
		item.setSelected(true);
		item.setLabel("1. RP");
		item.setParent(diskonList);
		item = new Listitem();
		item.setValue(MedisafeConstants.PERCENT);
		item.setLabel("2. %");
		item.setParent(diskonList);
		
		//SUBTOTAL
		double hargaFinal = price.getValue().doubleValue() * qty.getValue().intValue();
		dbharga.setValue(new BigDecimal(hargaFinal));
		
		Label finalPrice = new Label(dbharga.getText());
		listcell = new Listcell(finalPrice.getValue());
		listcell.setParent(listitem);
		Object[] obj = new Object[]{new BigDecimal(hargaFinal),new BigDecimal(hargaFinal),"RP",qty.getValue(),decimalbox.getValue()};
		listitem.setAttribute("manipulation", obj);
		decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell,qty,new Short("0")));
		diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell,qty, new Short("0")));
		MiscTrxController.setFont(listbox);
		win.detach();
	}
	

}
