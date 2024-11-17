package com.vone.medisafe.service.ifaceimpl.master;

import java.util.List;

import com.vone.medisafe.satusehat.masterdata.Location;
import com.vone.medisafe.satusehat.service.LocationService;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.mapping.MsRegencyDAO;
import com.vone.medisafe.service.iface.master.RegenyManager;

public class RegencyManagerImpl implements RegenyManager{

	private MsRegencyDAO dao;
	
	public MsRegencyDAO getDao() {
		return dao;
	}

	public void setDao(MsRegencyDAO dao) {
		this.dao = dao;
	}

	public void save(MsRegency regency) {
		// TODO Auto-generated method stub
		this.dao.save(regency);
	}

	
	public MsRegency findById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}

	public void delete(MsRegency regency) {
		// TODO Auto-generated method stub
		this.dao.delete(regency);
	}

	public MsRegency getRegencyByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getByCode(code);
	}

	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}

	public List searchRegency(String code, String name) {
		// TODO Auto-generated method stub
		return dao.searchRegency(code, name);
	}

	
	public void getRegencyForSelect(Listbox kabupatenList) throws VONEAppException {
		
		kabupatenList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(kabupatenList);
		
		
		List<MsRegency> list = this.dao.getAllRegencyList();
		
		
		for(MsRegency kabupaten : list){
			
			item = new Listitem();
			item.setValue(kabupaten);
			item.setLabel(kabupaten.getVRegencyName());
			item.setParent(kabupatenList);
		}
		
		kabupatenList.setSelectedIndex(0);
		
	}

	
	public void getRegencyData(Listbox regencyList) throws VONEAppException {
		
		regencyList.getItems().clear();
		Listitem listitem = null;
		
		
		List<MsRegency> list = dao.getAllRegencyList();
		
		for(MsRegency kabupaten : list) {

			listitem = new Listitem();
			listitem.setValue(kabupaten.getNRegencyId());
			listitem.setParent(regencyList);

			Listcell nameCell = new Listcell(kabupaten.getVRegencyId());
			nameCell.setParent(listitem);
			Listcell surnameCell = new Listcell(kabupaten.getVRegencyName());
			surnameCell.setParent(listitem);
		}
		
		
		
	}

	@Override
	public void getRegencyBaseOnProvince(Listbox kabupatenList, Listbox provinceList) throws VONEAppException {
		kabupatenList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(kabupatenList);

		//change to satusehat master data
		/**MsProvince province = (MsProvince) provinceList.getSelectedItem().getValue();
		
		List<MsRegency> list = this.dao.getAllRegencyByProvince(province);
		
		
		for(MsRegency kabupaten : list){
			
			item = new Listitem();
			item.setValue(kabupaten);
			item.setLabel(kabupaten.getVRegencyName());
			item.setParent(kabupatenList);
		}*/

		String pronviceCode = provinceList.getSelectedItem().getValue().toString();
		List<Location> cityList = LocationService.getCitiesByPronvinceCode(pronviceCode);
		for(Location city : cityList){
			item = new Listitem();
			item.setValue(city.getCode());
			item.setLabel(city.getName().toUpperCase());
			item.setParent(kabupatenList);
		}
		
		kabupatenList.setSelectedIndex(0);
		
	}

}
