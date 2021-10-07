package com.vone.medisafe.ui.master.item;

import java.math.BigDecimal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Bandbox;
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
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.ui.base.BaseController;
import com.vone.medisafe.ui.master.TreatmentClassController;

public class ItemSellingPriceController extends BaseController{
	
//	private static final Logger log = Logger.getLogger(ItemSellingPriceController.class);
	private ZulConstraint constrains = new ZulConstraint();
	Textbox cariTextbox;
	Listbox sellingPriceList;

	public void doCancel(Component win) throws InterruptedException, VONEAppException {
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Decimalbox sellingPrice = (Decimalbox)win.getFellow("sellingPrice");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		
		
		super.doCancel(win);
		
		itemCode.setValue(null);
		itemName.setValue(null);
		sellingPrice.setValue(null);
		if(treatmentClassList.getItems().size()>0)treatmentClassList.setSelectedIndex(0);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	public void doDelete(Component win) throws InterruptedException, VONEAppException {
		Listbox sellingPriceList = (Listbox)win.getFellow("sellingPriceList");
		super.doDelete(win);
		if(sellingPriceList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.itemsellingprice.list.notselected"));
			return;
		}
		
		MsItemSellingPrice itemGroup = (MsItemSellingPrice)sellingPriceList.getSelectedItem().getValue();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		
		int indexSelected = sellingPriceList.getSelectedIndex();
		if(MasterServiceLocator.getItemSellingPriceManager().delete(itemGroup)){
			Messagebox.show(MessagesService.getKey("common.delete.success"));
			sellingPriceList.removeItemAt(indexSelected);
		}
		else{
			Messagebox.show(MessagesService.getKey("common.delete.fail"));
		}
	}

	public void doModify(Component win) throws InterruptedException, VONEAppException {
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Decimalbox sellingPrice = (Decimalbox)win.getFellow("sellingPrice");
		Listbox sellingPriceList = (Listbox)win.getFellow("sellingPriceList");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		
		super.doModify(win);
		
		if(sellingPriceList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("master.itemsellingprice.list.notselected"));
			return;
		}
		MsItemSellingPrice isp = (MsItemSellingPrice)sellingPriceList.getSelectedItem().getValue();
		itemCode.setValue(isp.getMsItem().getVItemCode());
		itemName.setValue(isp.getMsItem().getVItemName());
		sellingPrice.setValue(new BigDecimal(isp.getNSellingPrice()));
		
		itemSearchList.getItems().clear();
		
		Listitem listitem = new Listitem();
		listitem.setValue(isp.getMsItem());
		listitem.setParent(itemSearchList);
		
		Listcell code = new Listcell(isp.getMsItem().getVItemCode());
		code.setParent(listitem);
		
		Listcell name = new Listcell(isp.getMsItem().getVItemName());
		name.setParent(listitem);
		
		itemSearchList.addItemToSelection(listitem);
		
		for(int i=1; i < treatmentClassList.getItems().size(); i++){
			if(((MsTreatmentClass)treatmentClassList.getItemAtIndex(i).getValue()).getNTclassId().equals(
					isp.getMsTreatmentClass().getNTclassId())){
				treatmentClassList.setSelectedIndex(i);
				break;
			}
		}
		
	}

	public void doSave(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		if(!constrains.validateComponent(true))return;
		super.doSave(cmp);
	}

	public void doSaveAdd(Component win) throws InterruptedException, VONEAppException {
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Decimalbox sellingPrice = (Decimalbox)win.getFellow("sellingPrice");
		Listbox sellingPriceList = (Listbox)win.getFellow("sellingPriceList");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		
		super.doSaveAdd(win);
		
		MsItemSellingPrice itemSell = new MsItemSellingPrice();
		MsItem item = (MsItem)itemSearchList.getSelectedItem().getValue();
		MsTreatmentClass tclas = (MsTreatmentClass)treatmentClassList.getSelectedItem().getValue();
		itemSell.setMsItem(item);
		itemSell.setMsTreatmentClass(tclas);
		double price = sellingPrice.getValue().doubleValue();
		
		Listitem listitem = null;
		Listcell cell = null;
		
		itemSell.setNSellingPrice(price);
		if(MasterServiceLocator.getItemSellingPriceManager().save(itemSell)){
			Messagebox.show(MessagesService.getKey("common.add.success"));
			
			listitem = new Listitem();
			listitem.setValue(itemSell);
						
			cell = new Listcell(itemCode.getValue());
			cell.setParent(listitem);
			
			cell =  new Listcell(itemName.getValue());
			cell.setParent(listitem);
			
			cell = new Listcell(tclas.getVTclassDesc());
			cell.setParent(listitem);
			
			cell = new Listcell(sellingPrice.getText());
			cell.setParent(listitem);
			
			sellingPriceList.appendChild(listitem);
			doCancel(win);
		}
		else Messagebox.show(MessagesService.getKey("common.add.fail"));
	}

	public void doSaveModify(Component win) throws InterruptedException, VONEAppException {
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Decimalbox sellingPrice = (Decimalbox)win.getFellow("sellingPrice");
		Listbox sellingPriceList = (Listbox)win.getFellow("sellingPriceList");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		
		super.doSaveModify(win);
		
		Listitem listitem = sellingPriceList.getSelectedItem();
		MsItemSellingPrice itemSell = (MsItemSellingPrice)listitem.getValue(); 
		MsItem item = (MsItem)itemSearchList.getSelectedItem().getValue();
		MsTreatmentClass tclas = (MsTreatmentClass)treatmentClassList.getSelectedItem().getValue();
		itemSell.setMsItem(item);
		itemSell.setMsTreatmentClass(tclas);
		double price = sellingPrice.getValue().doubleValue();
		
		
		Listcell cell = null;
		itemSell.setNSellingPrice(price);
		if(MasterServiceLocator.getItemSellingPriceManager().save(itemSell)){
			Messagebox.show(MessagesService.getKey("common.modify.success"));
			
			listitem.getChildren().clear();
			listitem.setValue(itemSell);
						
			cell = new Listcell(itemCode.getValue());
			cell.setParent(listitem);
			
			cell =  new Listcell(itemName.getValue());
			cell.setParent(listitem);
			
			cell = new Listcell(tclas.getVTclassDesc());
			cell.setParent(listitem);
			
			cell = new Listcell(sellingPrice.getText());
			cell.setParent(listitem);
						
			doCancel(win);
		}
		else Messagebox.show(MessagesService.getKey("common.modify.fail"));
	}

	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Textbox code = (Textbox)win.getFellow("code");
		Textbox names = (Textbox)win.getFellow("names");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Decimalbox sellingPrice = (Decimalbox)win.getFellow("sellingPrice");
		Listbox treatmentClassList = (Listbox)win.getFellow("treatmentClassList");
		sellingPriceList = (Listbox)win.getFellow("sellingPriceList");
		cariTextbox = (Textbox)win.getFellow("cariTextbox");
		
		super.init(win);
		
		itemName.setDisabled(true);
		constrains.registerComponent(itemCode, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(itemCode, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(treatmentClassList, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(sellingPrice, ZulConstraint.NO_EMPTY);
		constrains.registerComponent(code, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(names, ZulConstraint.UPPER_CASE);
		constrains.registerComponent(cariTextbox, ZulConstraint.UPPER_CASE);
		
		constrains.validateComponent(false);
		itemSearchList.getItems().clear();
		//TreatmentClassController.getTClass(treatmentClassList);
		TreatmentClassController.getTClassDataList(treatmentClassList);
		getSellingPriceListData(sellingPriceList);
	
	}

	public void getSellingPriceListData(Listbox sellingPriceList) throws VONEAppException{
		
		MasterServiceLocator.getItemSellingPriceManager().getItemSellingPrices(sellingPriceList, "%");
		MiscTrxController.setFont(sellingPriceList);


	}
	
	public void getItem(Window win){
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		
		MsItem item = (MsItem)itemSearchList.getSelectedItem().getValue();
		itemCode.setValue(item.getVItemCode());
		itemName.setValue(item.getVItemName());
	}
	
	public void getItemByCode(Window win) throws InterruptedException, WrongValueException, VONEAppException{
		Bandbox itemCode = (Bandbox)win.getFellow("itemCode");
		Listbox itemSearchList = (Listbox)win.getFellow("itemSearchList");
		Textbox itemName = (Textbox)win.getFellow("itemName");
		
		MsItem item = MasterServiceLocator.getItemManager().getItemByCode(itemCode.getValue().toUpperCase());
		if(item == null){
			Messagebox.show(MessagesService.getKey("item.notfound"));
			return;
		}
		itemSearchList.getItems().clear();
		
		Listitem listitem = new Listitem();
		listitem.setValue(item);
		listitem.setParent(itemSearchList);
		
		Listcell code = new Listcell(item.getVItemCode());
		code.setParent(listitem);
		
		Listcell name = new Listcell(item.getVItemName());
		name.setParent(listitem);
		
		itemSearchList.addItemToSelection(listitem);
		itemName.setValue(item.getVItemName());
		itemCode.setValue(item.getVItemCode());
	}
	
	public void cariClick() throws VONEAppException{
		MasterServiceLocator.getItemSellingPriceManager().getItemSellingPrices(sellingPriceList, cariTextbox.getValue());
		MiscTrxController.setFont(sellingPriceList);
	}
}
