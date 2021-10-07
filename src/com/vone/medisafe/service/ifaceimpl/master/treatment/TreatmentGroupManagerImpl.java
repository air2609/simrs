package com.vone.medisafe.service.ifaceimpl.master.treatment;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentGroup;
import com.vone.medisafe.mapping.dao.treatment.MsTreatmentGroupDAO;
import com.vone.medisafe.service.iface.master.treatment.TreatmentGroupManager;

public class TreatmentGroupManagerImpl implements TreatmentGroupManager{
	
	private MsTreatmentGroupDAO dao;
	
	public MsTreatmentGroupDAO getDao() {
		return dao;
	}

	public void setDao(MsTreatmentGroupDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsTreatmentGroup tgroup) throws VONEAppException{
		return dao.delete(tgroup);
	}

	public List getTGroups() throws VONEAppException{
		return dao.getAllTreatmentGroup();
	}

	public boolean save(MsTreatmentGroup tgroup) throws VONEAppException{
		return dao.save(tgroup);
	}

	public void getLabGroup(Listbox categoryList) throws VONEAppException {
		categoryList.getItems().clear();
		List list = dao.getAllTreatmentGroup();
		Iterator iter = list.iterator();
		Listitem item;
		while (iter.hasNext()) {
			MsTreatmentGroup msTreatmentGroup = (MsTreatmentGroup) iter.next();
			if(!(msTreatmentGroup.getVTgroupName().equals(MedisafeConstants.PAKET) ||
					msTreatmentGroup.getVTgroupName().equals(MedisafeConstants.NON_PAKET))){
				item = new Listitem();
				item.setParent(categoryList);
				item.setValue(msTreatmentGroup);
//				item.setLabel(msTreatmentGroup.getVTgroupCode() + " - " + msTreatmentGroup.getVTgroupName());
				item.setLabel(msTreatmentGroup.getVTgroupName());
			}
		}
		categoryList.setSelectedIndex(0);
		
	}

	public void getTestLabDetail(Listbox detailListbox, MsTreatment msTreatment) throws VONEAppException {
		// todo last-touch
		//menampilkan rincian item yg mau dilakukan test lab pada suatu treatmen
		//ke dlm detailListbox
		//reload treatment
		detailListbox.getItems().clear();
		Listitem item;
		Listcell cell;
		
		msTreatment = dao.reloadMsTreatment(msTreatment);
		//problem: gimana caranya spy urut
		Set list = msTreatment.getMsLabTreatmentDetil();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			MsLabTreatmentDetil msLabTreatmentDetil = (MsLabTreatmentDetil) iter.next();
			item = new Listitem();
			item.setValue(msLabTreatmentDetil);
			item.setParent(detailListbox);
			item.setLabel(msLabTreatmentDetil.getVDetailName());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(msLabTreatmentDetil.getVQuantify());
			
			cell = new Listcell();
			cell.setParent(item);
			cell.setLabel(msLabTreatmentDetil.getVNormalRange());
			
			
		}
	}

}
