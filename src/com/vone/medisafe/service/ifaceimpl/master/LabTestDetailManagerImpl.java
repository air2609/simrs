package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.dao.MsLabTestDetailDAO;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;
import com.vone.medisafe.service.iface.master.LabTestDetailManager;

public class LabTestDetailManagerImpl implements LabTestDetailManager{
	
	private MsLabTestDetailDAO dao;

	public MsLabTestDetailDAO getDao() {
		return dao;
	}

	public void setDao(MsLabTestDetailDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsLabTestDetail lab) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(lab);
	}

	public List getLabtTestDeatils() throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getLabTestDetails();
	}

	public boolean save(MsLabTestDetail lab) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(lab);
	}

	public void getLabtTestDeatils(Listbox labTestDetailList) throws VONEAppException {
		labTestDetailList.getItems().clear();
		
		List list = dao.getLabTestDetails();
		Iterator it = list.iterator();
		while(it.hasNext()){
			MsLabTestDetail labdetil = (MsLabTestDetail)it.next();
			
			Listitem item = new Listitem();
			item.setParent(labTestDetailList);
			item.setValue(labdetil);
			
			Listcell cell = new Listcell(labdetil.getMsTreatment().getVTreatmentCode());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getMsTreatment().getVTreatmentName());
			cell.setParent(item);
			
			cell = new Listcell("PRIA : "+labdetil.getVNrmlRangeGroup()+"; WANITA : "+labdetil.getVNrmlRange());
			cell.setParent(item);
			
			cell = new Listcell(labdetil.getVLabTestQuantify());
			cell.setParent(item);
		}
	}

	public boolean save(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException {
		return dao.save(msLabTreatmentDetil);
		
	}

	public boolean delete(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException {
		// todo Auto-generated method stub
		return dao.delete(msLabTreatmentDetil);
	}

}
