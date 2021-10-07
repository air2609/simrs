package com.vone.medisafe.ui.mr;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.TbDrugIngredients;
import com.vone.medisafe.mapping.TbDrugIngredientsDetail;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.ui.base.BaseController;

public class RacikanMRController extends BaseController{
	

	
//	private MsItemSellingPrice itemSellingPrice;
	private Integer racikanId;
	
	Textbox code;
	Intbox quantity;
	Listbox quantify;
	Listbox itemSelecetd;
	Listbox itemRoot;
	Component win;
		
	Session session;
	Listitem item;
	Listbox listbox;
	ZulConstraint constraints = new ZulConstraint();
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
	
	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(win);
		
		
		code = (Textbox)win.getFellow("code");
		quantity = (Intbox)win.getFellow("quantity");
		quantify = (Listbox)win.getFellow("quantify");
		
		session = win.getDesktop().getSession();
		itemSelecetd = (Listbox)session.getAttribute("selectedItem");
		itemRoot = (Listbox)session.getAttribute("listObatDokter");
		this.win = win;
		
		constraints.registerComponent(quantity, ZulConstraint.NO_EMPTY);
		constraints.validateComponent(false);
		
		racikanId = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.RACIKAN_ID);
		code.setValue(sdf.format(new Date()) +"-" +racikanId.toString());
		quantity.focus();
		
	}
	public void getOut(){
		session.removeAttribute("listObatDokter");
	}
	
	public void save() throws VONEAppException, InterruptedException{
		
		if(!constraints.validateComponent(true))return;
		
		Listitem listitem;
		Listcell listcell;
		String type = null;
		Decimalbox dbharga = new Decimalbox();
		dbharga.setFormat("#,###.#");
				
		double hargaObat = 0;
		
		Set items = itemSelecetd.getSelectedItems();
		Set<TbDrugIngredientsDetail> detilRacikan = new HashSet<TbDrugIngredientsDetail>(0);
		TbDrugIngredientsDetail drugDetil = null;
		
		String itemNames = "";
		Integer jasaR = 0;
		
		String racikanType = quantify.getSelectedItem().getValue().toString();
		if (racikanType.equalsIgnoreCase("BUNGKUS") || racikanType.equalsIgnoreCase("KAPSUL") ||
				racikanType.equalsIgnoreCase("BOTOL")){
			jasaR = new Integer(MessagesService.getKey("default.racikan"));
		}else jasaR = new Integer(MessagesService.getKey("salep.racikan"));
		
		Iterator it = items.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			Listcell jumlah = (Listcell)item.getChildren().get(5);
			Decimalbox jumlahValue = (Decimalbox)jumlah.getChildren().get(0);
					
		
			Object[] obj = (Object[])item.getValue();
			hargaObat = hargaObat + (jumlahValue.getValue().doubleValue() * (Double)obj[3]);
			
			drugDetil = new TbDrugIngredientsDetail();
			drugDetil.setNDingrDetQty(jumlahValue.getValue().floatValue());
			MsItem msItem = new MsItem();
			msItem.setNItemId((Integer)obj[0]);

			drugDetil.setMsItem(msItem);

			drugDetil.setVDingrDetQuantify(obj[4].toString());
			detilRacikan.add(drugDetil);
			jumlahValue.setValue(null);
			
			itemNames = itemNames + obj[2].toString()+" "+drugDetil.getNDingrDetQty()+", ";
			//jasaR = jasaR + ((Short)obj[7]).intValue();
			
		}
		
		type = "RACIKAN";
		itemNames = itemNames.substring(0, itemNames.length() - 1);
			
		TbDrugIngredients racikan = new TbDrugIngredients();
		racikan.setNDingrId(racikanId);
		racikan.setVDingrId(code.getText());
		racikan.setVItemComposition(itemNames);
		racikan.setNDingrQty((short)quantity.intValue());
		racikan.setNEr(jasaR.shortValue());
		racikan.setNDingrQuantify(quantify.getSelectedItem().getValue().toString());
		racikan.setTbDrugIngredientsDetails(detilRacikan);
		
		if(quantity.getValue().intValue() <= 0){
			Messagebox.show(MessagesService.getKey("common.input.negatif.notallowed"));
			code.focus();
			return;
		}
		
		listitem = new Listitem();
		listitem.setValue(racikan);
		listitem.setParent(itemRoot);
		
		listcell = new Listcell(code.getText());
		listcell.setParent(listitem);
		
		
		
		listcell = new Listcell(itemNames);
		listcell.setParent(listitem);
		
		
		listcell = new Listcell(type);
		listcell.setParent(listitem);
		
		listcell = new Listcell(quantify.getSelectedItem().getValue().toString());
		listcell.setParent(listitem);
		
		listcell = new Listcell(""+quantity.getValue().intValue());
		listcell.setParent(listitem);
		
		dbharga.setValue(new BigDecimal(hargaObat));
		listcell = new Listcell(dbharga.getText());
		listcell.setParent(listitem);
		
		
		Listcell aturanPakai  = new Listcell();
		aturanPakai.setParent(listitem);
		Textbox tbox = new Textbox();
		tbox.setParent(aturanPakai);
		tbox.setWidth("25%");
		tbox.setHeight("13px");
		Listbox lb = getAturanPakai();
		lb.setParent(aturanPakai);
		lb.setWidth("73%");
		lb.setMold("select");
		lb.setStyle("font-size:9pt");
		/**
		Decimalbox decimalbox = new Decimalbox();
		decimalbox.setParent(diskon);
		decimalbox.setValue(new BigDecimal(0));
		decimalbox.setWidth("50%");
		decimalbox.setFormat("#,###");
		decimalbox.setStyle("font-size:8pt");
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
		item.setParent(diskonList);*/
		
		double hargaFinal = hargaObat;
		dbharga.setValue(new BigDecimal(hargaFinal));
		
		Label finalPrice = new Label(dbharga.getText());
		listcell = new Listcell(finalPrice.getValue());
		listcell.setParent(listitem);
//		Object[] obj = new Object[]{new BigDecimal(hargaFinal),new BigDecimal(hargaFinal),"RP",quantity.getValue(),decimalbox.getValue(),jasaR.shortValue()};
//		listitem.setAttribute("manipulation", obj);
//		decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell,quantity.getValue()));
//		diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell, quantity.getValue()));
		
		itemSelecetd.clearSelection();
		win.detach();
		MiscTrxController.setFont(this.itemRoot);
	}
	
	private Listbox getAturanPakai() {
		String aturanPakai[] = {"(Setiap 24 Jam) sesudah makan","(Setiap 12 Jam) sesudah makan", "(Setiap 8 Jam) sesudah makan", 
				                "(Setiap 6 Jam) sesudah makan","(Setiap 24 Jam) sebelum makan", "(Setiap 12 Jam) sebelum makan",
				                "(Setiap 8 Jam) sebelum makan","(Setiap 6 Jam) sebelum makan"};
		
		Listbox lb = new Listbox();
		Listitem item = null;
		for(int i=0; i < aturanPakai.length; i++){
			item = new Listitem(aturanPakai[i]);
			item.setParent(lb);
		}
		lb.setSelectedIndex(0);
		return lb;
	}
	
	


}
