package com.vone.medisafe.service.ifaceimpl.master;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.service.iface.master.InsuranceManager;
import com.vone.medisafe.ui.master.InsuranceController;
import com.vone.medisafe.mapping.MsInsurance;
import com.vone.medisafe.mapping.MsInsuranceDAO;

public class InsuranceManagerImpl implements InsuranceManager{

	private MsInsuranceDAO dao;
	
	public MsInsuranceDAO getDao() {
		return dao;
	}

	public void setDao(MsInsuranceDAO dao) {
		this.dao = dao;
	}

	public List findAll () throws VONEAppException {
		return this.dao.findAll();
	}
	
	public void save (MsInsurance pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update (MsInsurance pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete (MsInsurance pojo) throws VONEAppException {
		this.dao.delete(pojo);
	}

	public void redrawList(Listbox list) throws VONEAppException {
		list.getItems().clear();
		
		List listInsurance = findAll();
		
		Iterator it = listInsurance.iterator();
		
		while (it.hasNext()){
			Listitem item = new Listitem();
			
			MsInsurance insurancePojo = (MsInsurance)it.next();
						
			item.appendChild(new Listcell((insurancePojo.getNActiveStatus() == MedisafeConstants.INSURANCE_ACTIVE)?"ACTIVE":""));
			item.appendChild(new Listcell(insurancePojo.getVInsuranceName()));			
			item.appendChild(new Listcell(insurancePojo.getVInsuranceAddr()));			
			
			//set COA cell
			Listcell cellCoa = new Listcell(insurancePojo.getMsCoa().getVAcctNo());
			cellCoa.setValue(insurancePojo.getMsCoa());			
			item.appendChild(cellCoa);
			
			item.appendChild(new Listcell(insurancePojo.getVInsurancePhNo()));
			
			if (insurancePojo.getDDateEndOfPartnership() != null)
				item.appendChild(new Listcell(MedisafeUtil.convertDateToString(insurancePojo.getDDateEndOfPartnership())));					
			
			item.setValue(insurancePojo);
			
			item.setParent(list);
		}
	}

	public void prepareModify(InsuranceController controller) throws VONEAppException {
		
		MsInsurance insurancePojo = (MsInsurance)controller.list.getSelectedItem().getValue();
		
		controller.insuranceName.setText(insurancePojo.getVInsuranceName());
		controller.desc.setText(insurancePojo.getVInsuranceDesc());
		controller.insuranceAddress.setText(insurancePojo.getVInsuranceAddr());
		controller.telpNo.setText(insurancePojo.getVInsurancePhNo());
		controller.activeStatus.setChecked((insurancePojo.getNActiveStatus() == MedisafeConstants.INSURANCE_ACTIVE)?true:false);
		controller.endOfContract.setValue(insurancePojo.getDDateEndOfPartnership());
		
		if(insurancePojo.getMsCoa() != null){
			controller.coa.setValue(insurancePojo.getMsCoa().getVAcctNo()+" - "+insurancePojo.getMsCoa().getVAcctName());
			controller.coa.setAttribute("coa", insurancePojo.getMsCoa());
		}
		
		
		controller.list.setDisabled(true);
		
		controller.insuranceAddress.select();
		
	}
}
