package com.vone.medisafe.service.iface.master.treatment;

import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.ui.master.treatment.TreatmentController;

public interface TreatmentManager {

	public boolean save(MsTreatment treatment,MsTreatmentFee tfee)throws VONEAppException;
	
	public boolean delete(MsTreatmentFee treatment)throws VONEAppException ;
	
	public List getTreatments()throws VONEAppException ;
	
	public List searchTreatment(MsTreatment treatment)throws VONEAppException ;
	
	public MsTreatment getByTreatmentCode(String code)throws VONEAppException ;
	
	public List getTreatmentByUnitAndClass(MsUnit unit, String tclass, String isPacket)throws VONEAppException ;
	
	public List searchTreatment(Integer unitId, String tcode, String tname, String tclass, String isPacket)
		throws VONEAppException ;
	
	public List getPacketTreatment(MsUnit unit, String tclass,String isPacket)throws VONEAppException ;
	//kun
	public List getTreatmentByTreatmentGroup(String code, String kelasTarif) throws VONEAppException;
	//kun
	public List getTreatmentByUnit(Integer unitId, String tcode, String tname, String tclass)
		throws VONEAppException ;
	
	
	public void searchTreatment(Integer unitId, Textbox code, Textbox nama, String kelasTarif, String non_paket,
			Listbox treatmentList) throws VONEAppException, InterruptedException;
	
	public void getTreatmentPacketForSelect(Listbox treatmentNameList, MsUnit unit, String kelasTarif, 
			String string) throws VONEAppException;

	public void getTreatments(Listbox treatmentList) throws VONEAppException;

	public void searchTreatment(String tcode, String tname, String unitname, String tclass, 
			Listbox treatmentList) throws VONEAppException;

	public void prepareModify(TreatmentController controller) throws VONEAppException;

	public void search(String input, Listbox hasilCari) throws VONEAppException;

	public void getAllTreatment(Listbox treatmentList) throws VONEAppException;

	public void updateTreatmentFee(Listbox treatmentList) throws VONEAppException;

	public void getTreatmentReport(Datebox dateFrom, Datebox dateTo, Listbox tipeList,
			Listbox treatmentList) throws VONEAppException;
	
	
}
