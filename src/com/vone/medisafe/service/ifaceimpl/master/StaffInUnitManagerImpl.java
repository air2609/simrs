package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsStaffInUnitDAO;
import com.vone.medisafe.mapping.MsVillage;
import com.vone.medisafe.service.iface.master.StaffInUnitManager;

public class StaffInUnitManagerImpl implements StaffInUnitManager{

	private MsStaffInUnitDAO dao;
	/**
	 * @return Returns the dao.
	 */
	public MsStaffInUnitDAO getDao() {
		return dao;
	}
	/**
	 * @param dao The dao to set.
	 */
	public void setDao(MsStaffInUnitDAO dao) {
		this.dao = dao;
	}
	public void save(MsStaffInUnit siu) {
		// TODO Auto-generated method stub
		this.dao.save(siu);
	}
	public List getStaffInUnitByCriteria(MsStaffInUnit siu) {
		// TODO Auto-generated method stub
		return this.dao.findByExample(siu);
	}
	
	public List getStaffByUnitId(Integer param)throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.getStaffByUnitId(param);
	}
	public List getAllStafInUnit() {
		// TODO Auto-generated method stub
		return this.dao.getAllStaffInUnit(MsStaffInUnit.class);
	}
	public MsStaffInUnit getStaffInUnitByStaffId(Integer id)
	{
		return this.dao.getMsStaffInUnitByStaffId(id);
	}
	public void saveTest(MsVillage village) {
		// TODO Auto-generated method stub
//		this.dao.saveVillage(village);
	}
	public void deleteByStaffId(Integer id) {
		// TODO Auto-generated method stub
		this.dao.delteStaffInUnitByStaffId(id);
	}
	
	public List getMsStaffInUnitListByStaffId(Integer staffId){
		return this.dao.getMsStaffInUnitListByStaffId(staffId);
	}
	public void getAllStafInUnit(Listbox allDoctor) throws VONEAppException {
		List list = getAllStafInUnit();
		Iterator it = list.iterator();
		int i = 1;
		while (it.hasNext()) {
			MsStaffInUnit siu = (MsStaffInUnit) it.next();
			Listitem item = new Listitem();

			if (siu.getId().getMsStaff().getNStaffRole() == MedisafeConstants.DOKTER) {
				item.setValue(siu.getId().getMsStaff());
				item.setLabel(i + "." + siu.getId().getMsStaff().getVStaffName());
				item.setParent(allDoctor);
				i++;
			}
		}
		return;
	}
	public void getAllDokterInUnit(Listbox dokterList) throws VONEAppException {
		dokterList.getItems().clear();

		Listitem item;
		MsStaffInUnit staffInUnit;
		List list = getAllStafInUnit();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			staffInUnit = (MsStaffInUnit) it.next();
			if (staffInUnit.getId().getMsStaff().getNStaffRole() == MedisafeConstants.DOKTER) {
				item = new Listitem();
				item.setValue(staffInUnit.getId().getMsStaff());
				item.setParent(dokterList);

				Listcell code = new Listcell(staffInUnit.getId().getMsStaff()
						.getVStaffCode());
				code.setParent(item);

				Listcell name = new Listcell(staffInUnit.getId().getMsStaff()
						.getVStaffName());
				name.setParent(item);

				Listcell unit = new Listcell(staffInUnit.getId().getMsUnit().getVUnitName());
				unit.setValue(staffInUnit.getId().getMsUnit());
				unit.setParent(item);
			}
		}
	}
	public void getStaffByUnitId(Integer unitId, Listbox docterListbox) throws VONEAppException {
		docterListbox.getItems().clear();
		Listitem item = new Listitem();
		item.setLabel(MedisafeConstants.LABELKOSONG);
		item.setValue(MedisafeConstants.LISTKOSONG);
		item.setParent(docterListbox);

		MsStaffInUnit siu;
		int i = 1;

		List list = null;
		
		list = getStaffByUnitId(unitId);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			siu = (MsStaffInUnit) it.next();
			item = new Listitem();

			if (siu.getId().getMsStaff().getNStaffRole() == MedisafeConstants.DOKTER) {
				item.setValue(siu.getId().getMsStaff().getNStaffId());
				item.setLabel(i + "." + siu.getId().getMsStaff().getVStaffName());
				item.setParent(docterListbox);
				i++;
			}
		}
		docterListbox.setSelectedItem(docterListbox.getItemAtIndex(0));
	}
}
