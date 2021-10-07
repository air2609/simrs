package com.vone.medisafe.service.iface.admin;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBranch;

public interface BranchManager {

	public List getAllBranch (MsBranch branch) throws VONEAppException;
	
	public void saveBranch (MsBranch branch);
	
	public void deleteBranch (MsBranch branch);
	
	public MsBranch getMsBranch (Integer iBranchId);
	
	public void updateBranch (int iBranchId, String stBranchCode);
}
