package com.vone.medisafe.ward;

import java.util.Date;
import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbRegistration;

/**
 * BedTransactionManager.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP : +6281314282206)
 * @since Nov, 13 2006
 * @category model (business logic)
 */

public interface BedTransactionManager {
	
	
	public boolean save(TbExamination nota, TbBedTrx bedTrx, 
			boolean isClosed, TbBedOccupancy boc, List<Date> bors) throws VONEAppException;
	
	public TbBedTrx getBTrxByNota(TbExamination nota) throws VONEAppException;
	
	public List<TbBedTrx> getBedTrxBaseOnPatient(MsPatient patient, MsBed bed) throws VONEAppException;

	public void getPatitentRegistration(BedTransactionController controller, int type) 
		throws VONEAppException, InterruptedException;
}
