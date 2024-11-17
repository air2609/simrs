package com.vone.medisafe.service.ifaceimpl.master;


import java.util.Iterator;
import java.util.List;

import com.vone.medisafe.satusehat.masterdata.Location;
import com.vone.medisafe.satusehat.service.LocationService;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.mapping.MsVillageDAO;
import com.vone.medisafe.service.iface.master.VillageManager;

public class VillageManagerImpl implements VillageManager{

	private MsVillageDAO dao;
	
	public MsVillageDAO getDao() {
		return dao;
	}

	public void setDao(MsVillageDAO dao) {
		this.dao = dao;
	}

	public void save(MsVillage village) {
		// TODO Auto-generated method stub
		this.dao.save(village);
	}

	

	public MsVillage getVillage(MsVillage village) {
		// TODO Auto-generated method stub
		MsVillage vlg = null;
		List kel = dao.findByExample(village);
		Iterator it = kel.iterator();
	    
		while(it.hasNext()) {
	            vlg = (MsVillage) it.next();
	    }
		return vlg;
	}

	public MsVillage getVillageById(Integer id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	public void delete(MsVillage village) {
		// TODO Auto-generated method stub
		this.dao.delete(village);
	}

	
	public void update(MsVillage village) {
		// TODO Auto-generated method stub
		dao.update(village);
	}

	public MsVillage getVillageByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getVillageByCode(code);
	}

	public List searchVillage(String villageCode, String villageName) {
		// TODO Auto-generated method stub
		return dao.searchVillage(villageCode+"%", villageName+"%");
	}

	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}
	

	public void getVillageForSelect(Listbox kelurahanList) throws VONEAppException {
		
		kelurahanList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);;
		item.setParent(kelurahanList);
		
		List<MsVillage> list = this.dao.getAllVillage();
		
		
		for(MsVillage village : list){
			
			item = new Listitem();
			item.setValue(village);
			item.setLabel(village.getVVillageName());
			item.setParent(kelurahanList);
		}
		
		kelurahanList.setSelectedIndex(0);
		
	}

	public void getVillageData(Listbox villageList) throws VONEAppException {
		
		villageList.getItems().clear();
				
		Listitem item;
		Listcell cell;
		
		List<MsVillage> list = dao.getAllVillage();
		
		
		for(MsVillage village : list){
			item = new Listitem();
			item.setValue(village.getNVillageId());
			item.setParent(villageList);
			
			cell = new Listcell(village.getVVillageCode());
			cell.setParent(item);
			
			cell = new Listcell(village.getVVillageName());
			cell.setParent(item);
		}
		
	}

	@Override
	public void getVillageBySubdistrict(Listbox kelurahanList, Listbox kecamatanList) throws VONEAppException {
kelurahanList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);;
		item.setParent(kelurahanList);

		//change localmaster data to satusehat master data
		/**MsSubDistrict subdistrict = (MsSubDistrict)kecamatanList.getSelectedItem().getValue();
		
		List<MsVillage> list = this.dao.getAllVillageBySubdistrict(subdistrict);
		
		
		for(MsVillage village : list){
			
			item = new Listitem();
			item.setValue(village);
			item.setLabel(village.getVVillageName());
			item.setParent(kelurahanList);
		}*/
		String districtCode = kecamatanList.getSelectedItem().getValue().toString();
		List<Location> subdistrictList = LocationService.getSubdistrictsByDistrictCode(districtCode);
		for(Location subdistrict : subdistrictList){
			item = new Listitem();
			item.setValue(subdistrict.getCode());
			item.setLabel(subdistrict.getName().toUpperCase());
			item.setParent(kelurahanList);
		}
		
		kelurahanList.setSelectedIndex(0);
		
		
	}

	

}
