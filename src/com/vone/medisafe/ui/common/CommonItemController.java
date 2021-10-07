package com.vone.medisafe.ui.common;

/**
 * CommonItemController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Oct, 16 2006
 * @category  user interface and cotroller (VC)
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
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
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.radiology.RadiologyManager;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;


public class CommonItemController extends BaseController{
	
	MsStaffInUnit siu;
	MsItemSellingPrice itemSellingPrice;
	
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
	
	ZulConstraint constraints = new ZulConstraint();
	

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
		
		//Object[] obj = user.getMsStaff().getMsStaffInUnits().toArray(); 
		Object[] obj;
		//obj = user.getMsStaff().getMsStaffInUnits().toArray();
		RadiologyManager radiologyManager = Service.getRadiologyManager();
		obj = radiologyManager.getMsStaffInUnits(user);
		if(obj.length == 1){
			siu = (MsStaffInUnit)obj[0];
			
			if(session.getAttribute("isRanap").equals("YES")){
				//ambil kelas tarif ranap
				kelasTarif = session.getAttribute("kelasTarif").toString();
			}else{
				//default kelas tarif : Kelas II
				kelasTarif = MedisafeConstants.DEFAULT_TCLASS;
			}
			if(unit.getMsWarehouse() != null)
				whouseId = unit.getMsWarehouse().getNWhouseId();
//			getItemBaseOnUnitAndClass(kelasTarif);
//			getTreatmentBaseOnUnit(siu.getId().getMsUnit(), kelasTarif);
		}else{
			
			
			if(session.getAttribute("isRanap").equals("YES")){
				//ambil kelas tarif ranap
				kelasTarif = session.getAttribute("kelasTarif").toString();
			}else{
				//default kelas tarif : Kelas II
				kelasTarif = MedisafeConstants.DEFAULT_TCLASS;
			}
			if(unit.getMsWarehouse() != null)
				whouseId = unit.getMsWarehouse().getNWhouseId();
//			getItemBaseOnUnitAndClass(kelasTarif);
			itemList.getItems().clear();
			
		}
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
//			itemSellingPrice = (MsItemSellingPrice)item.getValue();
//			listitem.setValue(itemSellingPrice);
			Object[] object = (Object[])item.getValue();
			listitem.setValue(object);
			listitem.setParent(listbox);
			
//			code
			listcell = new Listcell(object[1].toString());
			listcell.setParent(listitem);
			
//			name
			listcell = new Listcell(object[2].toString());
			listcell.setParent(listitem);
			
//			Listcell jumlah = (Listcell)item.getChildren().get(5);
//			if(jumlah.getChildren().get(0) == null){
//				Messagebox.show(MessagesService.getKey("common.item.jumlah.empty"));
//				return;
//			}
			
			
			listcell = new Listcell(""+jumlahValue.getValue().intValue());
			listcell.setParent(listitem);
			
//			item end quantify;
			listcell = new Listcell(object[5].toString());
			listcell.setParent(listitem);
			
			Decimalbox dbharga = new Decimalbox();
			dbharga.setValue(new BigDecimal((Double)object[4]));
			dbharga.setFormat("#,###.##");
			harga = new Label(dbharga.getText());
			listcell = new Listcell(harga.getValue());
			listcell.setParent(listitem);
			
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
			
			double hargaFinal = (Double)object[4] * jumlahValue.getValue().intValue();
			dbharga.setValue(new BigDecimal(hargaFinal));
			
			Short jasaR = (Short)object[3];
			
			Label finalPrice = new Label(dbharga.getText());
			listcell = new Listcell(finalPrice.getValue());
			listcell.setParent(listitem);
			Object[] obj = new Object[]{new BigDecimal(hargaFinal),new BigDecimal(hargaFinal),"RP",jumlahValue.getValue(),decimalbox.getValue(),jasaR};
			listitem.setAttribute("manipulation", obj);
			decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell,jumlahValue,jasaR));
			diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,finalPrice,decimalbox,listcell, jumlahValue,jasaR));
			
			this.win.detach();
		}
		
		MiscTrxController.setFont(this.listbox);
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void searchItems() throws InterruptedException, WrongValueException, VONEAppException{
		
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
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
			
//			item code
			cell = new Listcell(obj[1].toString());
			cell.setParent(item);
			
//			item qty;
			cell = new Listcell(obj[6].toString());
			cell.setParent(item);
			
//			item name;
			cell = new Listcell(obj[2].toString());
			cell.setParent(item);
			
//			item selling price;
			db.setValue(new BigDecimal((Double)obj[4]));
			cell = new Listcell(db.getText());
			cell.setParent(item);
			
//			item end quantify;
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
				
//				item code;
				cell = new Listcell(obj[1].toString());
				cell.setParent(item);
				
//				item item quantity;
				cell = new Listcell(obj[6].toString());
				cell.setParent(item);
				
//				item name;
				cell = new Listcell(obj[2].toString());
				cell.setParent(item);
				
//				item selling price
				db.setValue(new BigDecimal((Double)obj[4]));
				cell = new Listcell(db.getText());
				cell.setParent(item);
				
//				item end quantify;
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
				
//				itemList.addItemToSelection(item);
				selected = false;
				
			}
				
		}
		MiscTrxController.setFont(this.itemList);
		itemCode.setValue(null);
		itemName.setValue(null);
		quantity.setValue(null);
		itemCode.focus();
	}

	
}
