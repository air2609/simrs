package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.mapping.MsMedicStaffGroup;

public interface MedicStaffGroupManager {
	public void save(MsMedicStaffGroup msg);
	public void delete(MsMedicStaffGroup msg);
	public MsMedicStaffGroup getById(Integer id);
	public List getAllMedicStaffGroup();
	public MsMedicStaffGroup getByCode(String code);
	public boolean deleteById(Integer id);
}
