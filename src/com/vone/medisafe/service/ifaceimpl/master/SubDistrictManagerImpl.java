package com.vone.medisafe.service.ifaceimpl.master;



import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsRegency;
import com.vone.medisafe.mapping.MsSubDistrict;
import com.vone.medisafe.mapping.MsSubDistrictDAO;
import com.vone.medisafe.service.iface.master.SubDstrictManager;

public class SubDistrictManagerImpl implements SubDstrictManager{
	
	private MsSubDistrictDAO dao;
	
	public MsSubDistrictDAO getDao() {
		return dao;
	}
	public void setDao(MsSubDistrictDAO dao) {
		this.dao = dao;
	}
	public void save(MsSubDistrict subdistrict) {
		// TODO Auto-generated method stub
		this.dao.save(subdistrict);
	}
	public MsSubDistrict getSubDistrictbyId(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}
	
	
	public void delete(MsSubDistrict subdistrict) {
		// TODO Auto-generated method stub
		this.dao.delete(subdistrict);
	}
	public MsSubDistrict getSubdistrictByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getByCode(code);
	}
	public boolean deleteSubdistrictById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}
	public List searchSubdistrict(String code, String name) {
		// TODO Auto-generated method stub
		return dao.searchSubdistrict(code, name);
	}
	
	
	public void getSubDistrictForSelect(Listbox kecamatanList) throws VONEAppException {
		
		kecamatanList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(kecamatanList);
		
		
		List<MsSubDistrict> list = this.dao.getAllSubdistrict();
		
		for(MsSubDistrict kecamatan : list){
			
			item = new Listitem();
			item.setValue(kecamatan);
			item.setLabel(kecamatan.getVSubDistrictName());
			item.setParent(kecamatanList);
		}
		
		kecamatanList.setSelectedIndex(0);
		
	}
	
	
	public void getSubdistrictData(Listbox subdistrictList) throws VONEAppException {
		
		subdistrictList.getItems().clear();
		
		Listitem listitem = null;
		Listcell cell = null;
		
		
		
		List<MsSubDistrict> list = this.dao.getAllSubdistrict();

		for (MsSubDistrict subdistrict : list) {

			listitem = new Listitem();
			listitem.setValue(subdistrict.getNSubdistrictId());
			listitem.setParent(subdistrictList);

			cell = new Listcell(subdistrict.getVSubDistrictId());
			cell.setParent(listitem);
			
			cell = new Listcell(subdistrict.getVSubDistrictName());
			cell.setParent(listitem);
		}
		 
	}
	@Override
	public void getSubDistrictByRegency(Listbox kecamatanList, Listbox kabupatenList) throws VONEAppException {
		kecamatanList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(kecamatanList);
		
		MsRegency regency = (MsRegency)kabupatenList.getSelectedItem().getValue();
		
		List<MsSubDistrict> list = this.dao.getAllSubdistrictByRegency(regency);
		
		for(MsSubDistrict kecamatan : list){
			
			item = new Listitem();
			item.setValue(kecamatan);
			item.setLabel(kecamatan.getVSubDistrictName());
			item.setParent(kecamatanList);
		}
		
		kecamatanList.setSelectedIndex(0);
		
	}
	

}
