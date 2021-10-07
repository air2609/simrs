package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsVillage;

public interface StaffInUnitManager {
	public void save(MsStaffInUnit siu) throws VONEAppException;
	public List getStaffInUnitByCriteria(MsStaffInUnit siu) throws VONEAppException;
	public List getStaffByUnitId(Integer param) throws VONEAppException;
	public List getAllStafInUnit() throws VONEAppException;
	public MsStaffInUnit getStaffInUnitByStaffId(Integer id) throws VONEAppException;
	public void saveTest(MsVillage village) throws VONEAppException;
	public void deleteByStaffId(Integer id) throws VONEAppException;
	public List getMsStaffInUnitListByStaffId(Integer staffId) throws VONEAppException;
	public void getAllStafInUnit(Listbox allDoctor) throws VONEAppException;
	public void getAllDokterInUnit(Listbox dokterList) throws VONEAppException;
	public void getStaffByUnitId(Integer unitId, Listbox docterListbox) throws VONEAppException;
}
