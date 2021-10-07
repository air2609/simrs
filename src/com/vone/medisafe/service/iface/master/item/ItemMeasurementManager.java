package com.vone.medisafe.service.iface.master.item;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;



public interface ItemMeasurementManager {
	public boolean save(MsItemMeasurement itemMeasurement)throws VONEAppException;
	public boolean delete(MsItemMeasurement itemMeasurement)throws VONEAppException;
	public List getAllItemMeasurement()throws VONEAppException;
	public List getMeasurementType() throws VONEAppException;
	public MsItemMeasurement getMsItemMeasurementByCode(String code) throws VONEAppException;
	public Double convertMeasurement(String codeFrom, String codeTo, Double qty) throws VONEAppException;
	public List getMsItemMeasurementListByCode(String code) throws VONEAppException;
}
