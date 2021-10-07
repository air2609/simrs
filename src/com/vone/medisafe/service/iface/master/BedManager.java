package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsRoom;

public interface BedManager {

	public void save(MsBed bed) throws VONEAppException;
	
	public void delete(MsBed bed) throws VONEAppException;
	
	public MsBed getBedById(Integer id) throws VONEAppException;
	
	public void getBedByClass(Listbox listbed) throws VONEAppException;
		
	public MsBed getBedByCode(String code) throws VONEAppException;
	
	public List getBedBaseByRoom(MsRoom room) throws VONEAppException;
	
	public boolean deleteById(Integer id) throws VONEAppException;
	
	public boolean isBedExist(MsBed bed) throws VONEAppException;

	public void getAllBed(Listbox bedList) throws VONEAppException;

	public void doModify(Component win) throws VONEAppException;

	public void searchBed(String textCari, Listbox bedList) throws VONEAppException;

	public void getBedByClass(Listhead listhead);
	
	public void getActiveBed(Listbox bedList, Checkbox cb) throws VONEAppException;
	
	public void saveBulk(List<MsBed> beds) throws VONEAppException;

	public List getDuplicateBed();

	public void getBedInfo(Listbox bedInfoList) throws VONEAppException;
	
	public void getBorReport(Datebox startDate, Datebox endDate, Listbox borList) throws VONEAppException;
	
	
}
