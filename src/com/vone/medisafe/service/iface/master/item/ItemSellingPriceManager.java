package com.vone.medisafe.service.iface.master.item;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemSellingPrice;



public interface ItemSellingPriceManager {
	public boolean save(MsItemSellingPrice sellingPrice) throws VONEAppException;
	public boolean delete(MsItemSellingPrice sellingPrice) throws VONEAppException;
	public void getItemSellingPrices(Listbox sellingPriceList, String value) throws VONEAppException;
	
}
