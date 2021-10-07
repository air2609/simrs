package com.vone.medisafe.service.ifaceimpl.master.item;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.dao.item.ItemSellingPriceDAO;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;
import com.vone.medisafe.service.iface.master.item.ItemSellingPriceManager;

public class ItemSellingPriceManagerImpl implements ItemSellingPriceManager{
	
	private ItemSellingPriceDAO dao;

	public ItemSellingPriceDAO getDao() {
		return dao;
	}

	public void setDao(ItemSellingPriceDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsItemSellingPrice sellingPrice) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(sellingPrice);
	}

	public boolean save(MsItemSellingPrice sellingPrice) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(sellingPrice);
	}

	public void getItemSellingPrices(Listbox sellingPriceList, String value) throws VONEAppException {
		sellingPriceList.getItems().clear();
		
		Listitem item = null;
		Listcell cell = null;
		MsItemSellingPrice itemSellingPrice = null;
		Decimalbox decBox = new Decimalbox();
		decBox.setFormat(MedisafeConstants.CURRENCY_FORMAT);
		
		List list = dao.getItemSellingPrices(value);
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			itemSellingPrice = (MsItemSellingPrice)itr.next();
			item = new Listitem();
			item.setValue(itemSellingPrice);
			item.setParent(sellingPriceList);
			
			cell = new Listcell(itemSellingPrice.getMsItem().getVItemCode());
			cell.setParent(item);
			
			cell =  new Listcell(itemSellingPrice.getMsItem().getVItemName());
			cell.setParent(item);
			
			cell = new Listcell(itemSellingPrice.getMsTreatmentClass().getVTclassDesc());
			cell.setParent(item);
			
			decBox.setValue(new BigDecimal(itemSellingPrice.getNSellingPrice()));
			cell = new Listcell(decBox.getText());
			cell.setParent(item);
		}
	}

	
}
