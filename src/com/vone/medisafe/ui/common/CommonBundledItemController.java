package com.vone.medisafe.ui.common;

/**
 * CommonBundledItemController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Oct, 17 2006
 * @category  user interface and cotroller (VC)
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
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
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class CommonBundledItemController extends BaseController{

	
	Textbox itemCode;
	Textbox itemName;
	Intbox quantity;
	Listbox itemList;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	Session session;
	Integer whouseId;
	
	ZulConstraint constrains = new ZulConstraint();
	

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		itemCode = (Textbox)win.getFellow("itemCode");
		itemName = (Textbox)win.getFellow("itemName");
		quantity = (Intbox)win.getFellow("quantity");
		itemList = (Listbox)win.getFellow("itemList");
		
		
		this.win = win;
		
		session = win.getDesktop().getSession();
			
		listbox = (Listbox)session.getAttribute("bundlelist");
		Listbox location = (Listbox)session.getAttribute("location");
		super.init(win);
		
		constrains.registerComponent(itemCode, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(itemName, ZulConstraint.UPPER_CASE);
		
		UserInfoBean ub = getUserInfoBean();
		if(ub == null){
			Messagebox.show("uib kosong");
			return;
		}
		
		
		
		
		
		
		
		MsUnit unit = (MsUnit)location.getSelectedItem().getValue();
			
		if(session.getAttribute("isRanap").equals("YES")){
			//ambil kelas tarif ranap
			kelasTarif = session.getAttribute("kelasTarif").toString();
		}else{
			//default kelas tarif : Kelas II
			kelasTarif = "KELAS II";
		}
		if(unit.getMsWarehouse() != null)
			whouseId = unit.getMsWarehouse().getNWhouseId();

		itemList.getItems().clear();
			
		
	 itemCode.focus(); 
	 itemList.getItems().clear();
	
	}
	
	
	
	public void getItems() throws InterruptedException{
		
		Listitem listitem;
		Listcell listcell;
		Label harga;
		Iterator it = itemList.getSelectedItems().iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			
			Listcell jumlahDariGudang = (Listcell)item.getChildren().get(1);
//			Integer stok = new Integer(jumlahDariGudang.getLabel());
			Float stok = new Float(jumlahDariGudang.getLabel());
			
			Listcell jumlah = (Listcell)item.getChildren().get(5);
			Intbox jumlahValue = (Intbox)jumlah.getChildren().get(0);
			
			if(jumlahValue.getValue() == null){
				Messagebox.show(MessagesService.getKey("common.item.jumlah.empty"));
				jumlahValue.focus();
				return;
			}
			if(jumlahValue.getValue().intValue() <= 0){
				Messagebox.show(MessagesService.getKey("common.input.negatif.notallowed"));
				jumlahValue.focus();
				return;
			}
			if(jumlahValue.getValue().intValue() > stok.intValue()){
				Messagebox.show(MessagesService.getKey("common.input.greater.than.stok"));
				jumlahValue.focus();
				return;
			}
			
			listitem = new Listitem();
			Object[] obj = (Object[])item.getValue();
//			itemSellingPrice = (MsItemSellingPrice)item.getValue();
//			listitem.setValue(itemSellingPrice);
			listitem.setValue(obj);
			listitem.setParent(listbox);
			
			listcell = new Listcell(obj[1].toString());
			listcell.setParent(listitem);
			
//			Listcell jumlah = (Listcell)item.getChildren().get(5);
//			Intbox jumlahValue = (Intbox)jumlah.getChildren().get(0);
			
			listcell = new Listcell(""+jumlahValue.getValue().intValue());
			listcell.setParent(listitem);
			
			listcell = new Listcell(obj[2].toString());
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setValue(new BigDecimal((Double)obj[4]));
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
			this.win.detach();
			
			/**
			
			Listcell diskon  = new Listcell();
			diskon.setParent(listitem);
			Decimalbox decimalbox = new Decimalbox();
			decimalbox.setParent(diskon);
			decimalbox.setWidth("50%");
			decimalbox.setFormat("#,###");
			decimalbox.setHeight("14px");
			Listbox diskonList = new Listbox();
			diskonList.setParent(diskon);
			diskonList.setWidth("40%");
			diskonList.setMold("select");
			diskonList.setStyle("font-size:8pt");
			item = new Listitem();
			item.setValue("RP");
			item.setSelected(true);
			item.setLabel("1. Rp");
			item.setParent(diskonList);
			item = new Listitem();
			item.setValue("PERSEN");
			item.setLabel("2. %");
			item.setParent(diskonList);
			
			double hargaFinal = itemSellingPrice.getNSellingPrice() * jumlahValue.getValue().intValue();
			dbharga.setValue(new BigDecimal(hargaFinal));
			
			Label finalPrice = new Label(dbharga.getText());
			listcell = new Listcell(finalPrice.getValue());
			listcell.setParent(listitem);
			decimalbox.addEventListener("onChange", new DiscontListener(diskonList,finalPrice,decimalbox,listcell));
			diskonList.addEventListener("onSelect", new DiscontListener(diskonList,finalPrice,decimalbox,listcell));
			*/
		}
		MiscTrxController.setFont(this.listbox);
		
	}
	
	public void getOut(){
		session.removeAttribute("bundlelist");
	}
	
	public void searchItems() throws InterruptedException, VONEAppException{
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.#");
		
		boolean selected = false;
		Set treatments = itemList.getSelectedItems();
		Listitem[] listitem = new Listitem[treatments.size()];
		
		Iterator it = treatments.iterator();
		int counter = 0;
		while(it.hasNext()){
			listitem[counter] = (Listitem)it.next();
			counter++;
		}
		
		itemList.getItems().clear();
		
		
		for(int i=0; i < listitem.length; i++){
			item = listitem[i];
			item.setParent(itemList);
		}
		
		
		//add search result to listbox, if result size is 1, add result to selection
		if(itemCode.getText().trim().equals("") && itemName.getText().trim().equals("")){
			Messagebox.show(MessagesService.getKey("salah.satu.field.harus.diisi"));
			itemCode.focus();
			return;
		}
		
		List list = MasterServiceLocator.getItemManager().searchItemByWarehoueseAndTclass(
				whouseId, kelasTarif,"%"+itemCode.getText()+"%","%"+itemName.getText()+"%");
	
	
		//each list element contains array of object
		if (list == null)return;
		
		if(list.size() == 1){
			//obj[0] = item id			
			//obj[1] = item code
			//ojb[2] = item name
			//obj[4] = item price
			//ojb[5] = item quantify
			//obj[6] = item qty
			Object[] obj = (Object[])list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
//				if(((MsItemSellingPrice)item.getValue()).getNItemSellingPriceId().
//									equals(itemSellingPrice.getNItemSellingPriceId()))
				Object[] object = (Object[])item.getValue();
				if(((Integer)object[0]).equals((Integer)obj[0])){
					itemCode.focus();
					return;
				}
			}
			item = new Listitem();
//			item.setValue(itemSellingPrice);
			item.setValue(obj);
			item.setParent(itemList);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
			
//			float itemQuantity = MasterServiceLocator.getItemManager().getItemQuantity(itemSellingPrice.getMsItem(),kelasTarif, whouseId);
			cell = new Listcell(obj[6].toString());
			cell.setParent(item);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getVItemName());
			cell = new Listcell(obj[2].toString());
			cell.setParent(item);
			
//			db.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
			db.setValue(new BigDecimal((Double)obj[4]));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			cell = new Listcell(obj[5].toString());
			cell.setParent(item);
			
			Listcell listcell = new Listcell();
			listcell.setParent(item);
			Intbox ibox = new Intbox();
			ibox.setWidth("90%");
			ibox.setParent(listcell);
			
			if(quantity.getValue() != null){
				if(quantity.getValue().intValue() <= (Double)obj[6]){
					ibox.setValue(quantity.getValue());
				}
			}
			
			itemList.addItemToSelection(item);		
			MiscTrxController.setFont(this.itemList);
			itemCode.setValue(null);
			itemName.setValue(null);
			quantity.setValue(null);
			itemCode.focus();
			return;
			
		}
		
		it = list.iterator();
		
		while(it.hasNext()){
			
			Object[] obj = (Object[])it.next();
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
//				if(((MsItemSellingPrice)item.getValue()).getNItemSellingPriceId().equals(itemSellingPrice.getNItemSellingPriceId()))
//				{
//					selected = true;
//					break;
//				}
//				else{
//					selected = false;
//				}
				Object[] object = (Object[])item.getValue();
				if(((Integer)object[0]).equals((Integer)obj[0]))
				{
					selected = true;
					itemCode.setValue(null);
					itemName.setValue(null);
					quantity.setValue(null);
					itemCode.focus();
					break;
				}
				else{
					selected = false;
				}
			}
			if(selected){
				itemCode.setValue(null);
				itemName.setValue(null);
				quantity.setValue(null);
				itemCode.focus();
				//do nothing
			}else{
				item = new Listitem();
//				item.setValue(itemSellingPrice);
				item.setValue(obj);
				item.setParent(itemList);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
				cell = new Listcell(obj[1].toString());
				cell.setParent(item);
				
//				float itemQuantity = MasterServiceLocator.getItemManager().getItemQuantity(itemSellingPrice.getMsItem(),kelasTarif, whouseId);
				cell = new Listcell(obj[6].toString());
				cell.setParent(item);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getVItemName());
				cell = new Listcell(obj[2].toString());
				cell.setParent(item);
				
//				db.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
				db.setValue(new BigDecimal((Double)obj[4]));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
				cell = new Listcell(obj[5].toString());
				cell.setParent(item);
				
				Listcell listcell = new Listcell();
				listcell.setParent(item);
				Intbox ibox = new Intbox();
				ibox.setWidth("90%");
				ibox.setStyle("font-size:8pt");
				ibox.setParent(listcell);
				
				if(quantity.getValue() != null){
					if(quantity.getValue().intValue() <= (Double)obj[6]){
						ibox.setValue(quantity.getValue());
					}
				}
				
//				itemList.addItemToSelection(item);
				selected = false;
				
			}
				
		}
		MiscTrxController.setFont(this.itemList);
		itemCode.focus();
		itemCode.setValue(null);
		itemName.setValue(null);
		quantity.setValue(null);
	}
}
