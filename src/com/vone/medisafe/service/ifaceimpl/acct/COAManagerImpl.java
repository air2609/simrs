package com.vone.medisafe.service.ifaceimpl.acct;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsStaff;
import com.vone.medisafe.mapping.dao.acct.CoaDAO;
import com.vone.medisafe.service.iface.acct.COAManager;

public class COAManagerImpl implements COAManager{
	
	private CoaDAO dao;

	public CoaDAO getDao() {
		return dao;
	}

	public void setDao(CoaDAO dao) {
		this.dao = dao;
	}

	public List getCoaHeader() throws VONEAppException{
		return this.dao.getCoaHeader();
	}
	
	public List getCoaHeader(int status) throws VONEAppException{
		return this.dao.getCoaHeader(status);
	}
	
	public List getCoaChild(MsCoa headPojo) throws VONEAppException {
		return this.dao.getCoaChild(headPojo);
	}
	
	public List getCoaChild(MsCoa headPojo, int status) throws VONEAppException {
		return this.dao.getCoaChild(headPojo, status);
	}
	
	public List getCoaBaseOnType(int type) throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.getCoaBaseOnType(type);
	}

	public void save(MsCoa pojo) throws VONEAppException {
		this.dao.save(pojo);
	}
	
	public void update(MsCoa pojo) throws VONEAppException {
		this.dao.update(pojo);
	}
	
	public void delete(MsCoa persistentInstance) throws VONEAppException {
		this.dao.delete(persistentInstance);
	}
	
	public MsCoa findByExample(MsCoa instance) throws VONEAppException{
		return this.dao.findByExample(instance);
	}
	
	public List getCoaBaseOnTypeNoChild(int type) throws VONEAppException{
		return this.dao.getCoaBaseOnTypeNoChild(type);
	}
	
	public MsCoa getCoaByCode(String code) throws VONEAppException{
		return this.dao.getCoaByCode(code);
	}
	
	public List getCoaType() throws VONEAppException{
		return this.dao.getCoaType();
	}
	
	public void redrawCoaController(Tree tree, Listbox statusOnScreen) throws VONEAppException {
		
		tree.getTreechildren().getChildren().clear();
		Treechildren treeChild = tree.getTreechildren();
		Iterator itHead = null;
		Iterator itChild = null;
		
		int statusSearch = new Integer((String)statusOnScreen.getSelectedItem().getValue()).intValue();
		
		if (statusSearch == 
			MedisafeConstants.COA_ALL)
			
		itHead = this.dao.getCoaHeader().iterator();				
		else 
			itHead = this.dao.getCoaHeader(statusSearch).iterator();
			
		
		while (itHead.hasNext()){
			
			MsCoa pojoHead = (MsCoa)itHead.next();
			
			Treeitem item = new Treeitem();
			item.setParent(treeChild);
			item.setValue(pojoHead);
			
			Treerow row = new Treerow();
			row.setParent(item);
			
			//set header
			row.appendChild(new Treecell(pojoHead.getVAcctNo()));
			//status
			String status = null;
			if (pojoHead.getNStatus() == MedisafeConstants.COA_ACTIVE)
				status = MedisafeConstants.COA_ACTIVE_STR;
			else 
				if (pojoHead.getNStatus() == MedisafeConstants.COA_INACTIVE)
					status = MedisafeConstants.COA_INACTIVE_STR;
			row.appendChild(new Treecell(status));
			
			row.appendChild(new Treecell(pojoHead.getVAcctName()));
			
			//TYPE
			row.appendChild(new Treecell(pojoHead.getMsCoaType().getVCtName()));
			
			//balance
			double balance = 0;
			if (pojoHead.getNBalance() != null)
				balance = pojoHead.getNBalance().doubleValue();
			
			row.appendChild(new Treecell(NumberUtil.format(balance)));
			
			//set child
			if (statusSearch == 
				MedisafeConstants.COA_ALL)
				itChild = this.dao.getCoaChild(pojoHead).iterator();
			else
				itChild = this.dao.getCoaChild(pojoHead, statusSearch).iterator();
				
			Treechildren tchild = new Treechildren();
			tchild.setParent(item);

			while (itChild.hasNext()){
				MsCoa pojoChild = (MsCoa)itChild.next();
								
				Treeitem itemChild = new Treeitem();
				itemChild.setParent(tchild);
				itemChild.setValue(pojoChild);
				
				Treerow trow = new Treerow();
				trow.setParent(itemChild);
				
				trow.appendChild(new Treecell(pojoChild.getVAcctNo()));
				//status
				if (pojoChild.getNStatus() == MedisafeConstants.COA_ACTIVE)
					status = MedisafeConstants.COA_ACTIVE_STR;
				else 
					if (pojoChild.getNStatus() == MedisafeConstants.COA_INACTIVE)
						status = MedisafeConstants.COA_INACTIVE_STR;
				trow.appendChild(new Treecell(status));
				trow.appendChild(new Treecell(pojoChild.getVAcctName()));
				trow.appendChild(new Treecell(pojoChild.getMsCoaType().getVCtName()));
				
				balance = 0;
				if (pojoChild.getNBalance() != null)
					balance = pojoChild.getNBalance().doubleValue();
				trow.appendChild(new Treecell(NumberUtil.format(balance)));	
			}
		}
	}

	public MsCoa getCoaByStaff(MsStaff staff) throws VONEAppException {
		MsStaff msStaff = dao.getMsStaff(staff);
		return msStaff.getMsCoa();
	}

	
	
	public void searchCoa(String coaCode, String coaName, Listbox coaList) throws VONEAppException {
		
		coaList.getItems().clear();
		Listitem item;
		Listcell cell;
		
		List<MsCoa> list = dao.getCoaByCodeAndName(coaCode, coaName);
		
		for(MsCoa coa : list){
			
			item = new Listitem();
			item.setValue(coa);
			item.setParent(coaList);
			
			cell = new Listcell(coa.getVAcctNo());
			cell.setParent(item);
			
			cell = new Listcell(coa.getVAcctName());
			cell.setParent(item);
			
		}
		
	}

	public MsCoa checkCoa(String nomorKecoa) throws VONEAppException {
		
		return dao.getCoaByCode(nomorKecoa);
	}
	
	
	
}
