package com.vone.medisafe.service.iface.purchasing;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.ui.purchasing.ReturController;
import com.vone.medisafe.ui.purchasing.ReturDetailController;

public interface ReturManager {

	public void doSearch(ReturController ctr) throws VONEAppException;
	
	public void redraw(ReturController ctr) throws VONEAppException;
	
	public void save(ReturDetailController ctr) throws VONEAppException, InterruptedException;
	
	public void generateAPList(ReturDetailController ctr) throws VONEAppException;
}
