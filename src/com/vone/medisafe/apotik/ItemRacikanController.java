package com.vone.medisafe.apotik;

/**
 * ItemRacikanController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 17 2006
 * @category user interface an controller
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class ItemRacikanController extends BaseController{

	private MsStaffInUnit siu;
	private MsItemSellingPrice itemSellingPrice;
	
	Textbox itemCode;
	Textbox itemName;
	Decimalbox quantity;
	Listbox itemList;
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	Session session;
	Integer whouseId;
	boolean isRajal = false;
	
	ZulConstraint constraints = new ZulConstraint();
	
	ApotikManager serv = Service.getApotikManager();

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
		quantity = (Decimalbox)win.getFellow("quantity");
		itemList = (Listbox)win.getFellow("itemList");
		
		
		this.win = win;
		
		constraints.registerComponent(itemCode, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(itemName,ZulConstraint.UPPER_CASE);
		
		session = win.getDesktop().getSession();
			
		listbox = (Listbox)session.getAttribute("listbox");
		Listbox location = (Listbox)session.getAttribute("location");
			
		super.init(win);
		
		UserInfoBean ub = getUserInfoBean();
		if(ub == null){
			Messagebox.show("uib kosong");
			return;
		}
		
		
		
		MsUser user = ub.getMsUser();
		
		MsUnit unit = (MsUnit)location.getSelectedItem().getValue();
		
					
			
		if(session.getAttribute("isRanap").equals("YES")){
			//ambil kelas tarif ranap
			kelasTarif = session.getAttribute("kelasTarif").toString();
			isRajal=false;
		}else{
				//default kelas tarif : Kelas II
			kelasTarif = "KELAS II";
			isRajal = true;
		}
		
		whouseId = unit.getMsWarehouse().getNWhouseId();

		
			
		
	 itemCode.focus();
	 itemList.getItems().clear();
	
	}
	
	public void getItemBaseOnUnitAndClass(String kelasTarif) throws InterruptedException,VONEAppException{
		itemList.getItems().clear();
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.#");
		
		List list = serv.getRacikanItems(whouseId, kelasTarif);
		Iterator it = list.iterator();
		while(it.hasNext()){
			itemSellingPrice = (MsItemSellingPrice)it.next();
			item = new Listitem();
			item.setValue(itemSellingPrice);
			item.setParent(itemList);
			
			cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
			cell.setParent(item);
			
			float itemQuantity = MasterServiceLocator.getItemManager().getItemQuantity(itemSellingPrice.getMsItem(),kelasTarif, whouseId);
			cell = new Listcell(""+itemQuantity);
			cell.setParent(item);
			
			cell = new Listcell(itemSellingPrice.getMsItem().getVItemName());
			cell.setParent(item);
			
			db.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
			cell = new Listcell(itemSellingPrice.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			cell.setParent(item);
			
			Listcell listcell = new Listcell();
			listcell.setParent(item);
			Decimalbox ibox = new Decimalbox();
			ibox.setWidth("90%");
			ibox.setParent(listcell);
			
		}
	}
	
	
	public void getSelectedItem() throws InterruptedException, VONEAppException{
		if(itemList.getSelectedItems().size() == 0){
			Messagebox.show(MessagesService.getKey("drugs.not.selected"));
			itemList.focus();
			return;
		}
		
		Iterator it = itemList.getSelectedItems().iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			Listcell jumlahDariGudang = (Listcell)item.getChildren().get(1);
			Float stok = new Float(jumlahDariGudang.getLabel());
			
			
			Listcell jumlah = (Listcell)item.getChildren().get(5);
			Decimalbox jumlahValue = (Decimalbox)jumlah.getChildren().get(0);
			
			if(jumlahValue.getValue() == null){
				Messagebox.show(MessagesService.getKey("common.item.jumlah.empty"));
				jumlahValue.focus();
				return;
			}
			if(jumlahValue.getValue().floatValue() <= 0){
				Messagebox.show(MessagesService.getKey("common.input.negatif.notallowed"));
				jumlahValue.focus();
				return;
			}
			if(jumlahValue.getValue().floatValue() > stok.floatValue()){
				Messagebox.show(MessagesService.getKey("common.input.greater.than.stok"));
				jumlahValue.focus();
				return;
			}
		}
		
		
		session.setAttribute("selectedItem", itemList);
		/*this.win.detach();*/
		Window win = (Window) Executions.createComponents("/zkpages/apotik/racikan.zul",null,null);
		win.setAttribute("isRajal", isRajal);
		win.doModal();
	}
	
		
	public void getOut(){
		session.removeAttribute("listbox");
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
		List list = serv.searchRacikanItems(whouseId, kelasTarif,"%"+itemCode.getText()+"%","%"+itemName.getText()+"%");
		
		if(list.size() == 1){
			
			Object[] obj = (Object[])list.get(0);
			
			for(int i=0; i < listitem.length; i++){
				item = listitem[i];
//				if(((MsItemSellingPrice)item.getValue()).getNItemSellingPriceId().
//									equals(itemSellingPrice.getNItemSellingPriceId()))
				Object[] object = (Object[])item.getValue();
				if(((Integer)object[0]).equals((Integer)obj[0]))
					return;
			}
			item = new Listitem();
//			item.setValue(itemSellingPrice);
			item.setValue(obj);
			item.setParent(itemList);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
			
//			float itemQuantity = MasterServiceLocator.getItemManager().getItemQuantity(itemSellingPrice.getMsItem(),kelasTarif, whouseId);
			cell = new Listcell(obj[5].toString());
			cell.setParent(item);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getVItemName());
			cell = new Listcell(obj[2].toString());
			cell.setParent(item);
			
//			db.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
			db.setValue(new BigDecimal((Double)obj[3]));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
//			cell = new Listcell(itemSellingPrice.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
			cell = new Listcell(obj[4].toString());
			cell.setParent(item);
			
			Listcell listcell = new Listcell();
			listcell.setParent(item);
			Decimalbox ibox = new Decimalbox();
			ibox.setWidth("90%");
			ibox.setStyle("font-size:9pt");
			ibox.setParent(listcell);
			
			if(quantity.getValue() != null){
				if(quantity.getValue().intValue() < (Double)obj[5]){
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
				//do nothing
				itemCode.setValue(null);
				itemName.setValue(null);
				quantity.setValue(null);
				itemCode.focus();
			}else{
				item = new Listitem();
//				item.setValue(itemSellingPrice);
				item.setValue(obj);
				item.setParent(itemList);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
				cell = new Listcell(obj[1].toString());
				cell.setParent(item);
				
//				float itemQuantity = MasterServiceLocator.getItemManager().getItemQuantity(itemSellingPrice.getMsItem(),kelasTarif, whouseId);
				cell = new Listcell(obj[5].toString());
				cell.setParent(item);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getVItemName());
				cell = new Listcell(obj[2].toString());
				cell.setParent(item);
				
//				db.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
				db.setValue(new BigDecimal((Double)obj[3]));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
//				cell = new Listcell(itemSellingPrice.getMsItem().getMsItemMeasurement().getVMitemEndQuantify());
				cell = new Listcell(obj[4].toString());
				cell.setParent(item);
				
				Listcell listcell = new Listcell();
				listcell.setParent(item);
				Decimalbox ibox = new Decimalbox();
				ibox.setWidth("90%");
				ibox.setStyle("font-size:8pt");
				ibox.setParent(listcell);
				
				if(quantity.getValue() != null){
					if(quantity.getValue().intValue() <= (Double)obj[5]){
						ibox.setValue(quantity.getValue());
					}
				}
				
//				itemList.addItemToSelection(item);
				MiscTrxController.setFont(this.itemList);
				selected = false;
				
			}
				
		}
		itemCode.setValue(null);
		itemName.setValue(null);
		quantity.setValue(null);
		itemCode.focus();
	}
}
