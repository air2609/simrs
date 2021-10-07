package com.vone.medisafe.service.ifaceimpl.master;




import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsProvince;
import com.vone.medisafe.mapping.MsProvinceDAO;
import com.vone.medisafe.service.iface.master.ProvinceManager;

public class ProvinceManagerImpl implements ProvinceManager{

	private MsProvinceDAO dao;
	public MsProvinceDAO getDao() {
		return dao;
	}
	public void setDao(MsProvinceDAO dao) {
		this.dao = dao;
	}
	public void save(MsProvince province) {
		// TODO Auto-generated method stub
		this.dao.save(province);
	}
	
	
	public MsProvince getProvinceById(Integer id) {
		// TODO Auto-generated method stub
		return this.dao.findById(id);
	}
	public void delete(MsProvince province) {
		// TODO Auto-generated method stub
		this.dao.delete(province);
	}
	public Integer getId() {
		// TODO Auto-generated method stub
		return dao.getRegistrationId();
	}
	public MsProvince getProvinceByCode(String code) {
		// TODO Auto-generated method stub
		return dao.getProvinceByCode(code);
	}
	public boolean deleteProvincyByid(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id);
	}
	
	
	public void getProvinceForSelect(Listbox propinsiList) throws VONEAppException {
		
		propinsiList.getItems().clear();
		
		Listitem item = new Listitem();
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setParent(propinsiList);
		
		
		List<MsProvince> list = this.dao.getAllProvince();
		
		
		for(MsProvince province : list){
			
			item = new Listitem();
			item.setValue(province);
			item.setLabel(province.getVProvinceName());
			item.setParent(propinsiList);
			
		}
		
		propinsiList.setSelectedIndex(0);
	}
	public List getAllProvince() throws VONEAppException{
		// todo Auto-generated method stub
		return this.dao.getAllProvince();
	}
	

}
