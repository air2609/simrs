package com.vone.medisafe.service.ifaceimpl.master.item;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.dao.item.ItemMeasurementDAO;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;
import com.vone.medisafe.service.iface.master.item.ItemMeasurementManager;

public class ItemMeasurementManagerImpl implements ItemMeasurementManager{

	private ItemMeasurementDAO dao;
	
	public ItemMeasurementDAO getDao() {
		return dao;
	}

	public void setDao(ItemMeasurementDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsItemMeasurement itemMeasurement) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(itemMeasurement);
	}

	public List getAllItemMeasurement() throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getAllItemMeasurement();
	}

	public boolean save(MsItemMeasurement itemMeasurement) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(itemMeasurement);
	}

	public List getMeasurementType() throws VONEAppException {
		return dao.getMeasurementType();
	}
	
	public MsItemMeasurement getMsItemMeasurementByCode(String code) throws VONEAppException{
		return dao.getMsItemMeasurementByCode(code);
	}
	
	public Double convertMeasurement(String codeFrom, String codeTo, Double qty) throws VONEAppException {
		
		if (codeFrom.equals(codeTo))
			return new Double(1);
		
		//check availability of codeFrom
		MsItemMeasurement mim = this.dao.getMsItemMeasurementByCode(codeFrom, codeTo);
		
		if (mim == null)
			throw new VONEAppException(MessagesService.getKey("master.itemmeasurement.code.convert.not.valid"));
		
		return qty*mim.getNMitemEndQty();		
	}
	
	public List getMsItemMeasurementListByCode(String code) throws VONEAppException {
		
		return this.dao.getMsItemMeasurementListByCode(code);
	}
}
