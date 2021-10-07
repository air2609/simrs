package com.vone.medisafe.service.ifaceimpl.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBranch;
import com.vone.medisafe.mapping.MsBranchDAO;
import com.vone.medisafe.service.iface.admin.BranchManager;

public class BranchManagerImpl implements BranchManager{

	MsBranchDAO dao;		
	
	public MsBranchDAO getDao() {
		return dao;
	}

	public void setDao(MsBranchDAO dao) {
		this.dao = dao;
	}

	public void deleteBranch(MsBranch branch) {
		// TODO Auto-generated method stub
		
		this.dao.delete(branch);
	}

	public List getAllBranch(MsBranch branch) throws VONEAppException {
		// TODO Auto-generated method stub
		return this.dao.findByExample(branch);
	}

	public MsBranch getMsBranch(Integer iBranchId) {
		// TODO Auto-generated method stub
		return this.dao.findById(iBranchId);
	}

	public void saveBranch(MsBranch branch) {
		// TODO Auto-generated method stub		
		
		this.dao.save(branch);
	}

	public void updateBranch (int iBranchId, String stBranchCode) {
		// TODO Auto-generated method stub
		this.dao.update(iBranchId, stBranchCode);
	}

}
