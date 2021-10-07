package com.vone.medisafe.service.iface.master.treatment;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentGroup;

public interface TreatmentGroupManager {
	public boolean save(MsTreatmentGroup tgroup) throws VONEAppException;
	public boolean delete(MsTreatmentGroup tgroup) throws VONEAppException;
	public List getTGroups() throws VONEAppException;
	public void getLabGroup(Listbox categoryList) throws VONEAppException;
	public void getTestLabDetail(Listbox detailListbox, MsTreatment msTreatment) throws VONEAppException;
}
