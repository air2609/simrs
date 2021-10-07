package com.vone.medisafe.service.iface.polyclinic;

import java.util.List;
import java.util.Set;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbItemTrx;
import com.vone.medisafe.mapping.TbMiscTrx;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.mapping.TbTreatmentTrx;
import com.vone.medisafe.ui.polyclinic.PolyclinicController;

public interface PolyclinicManager {
	
	public boolean save(MsPatient patient, TbExamination nota, Set treatmentTrx, Set itemTrx,
			Set bundleTrx, MsUnit unit, boolean isRanap)throws VONEAppException;
	
		
	public List getItemTrx(TbExamination note) throws VONEAppException;
	
		
		
	public boolean save(MsPatient patient, TbExamination nota, Set<TbTreatmentTrx> 
		treatmentSet, Set<TbItemTrx> itemSet, Set<TbBundledTrx> bundledSet,Set<TbMiscTrx> miscSet,  
		MsUnit unit, boolean isRanap)throws VONEAppException;

	public List getMiscTrx(TbExamination nota)throws VONEAppException;
	

	public void getRegistrationDetil(PolyclinicController controller, int type) 
		throws VONEAppException, InterruptedException;

	public void getNoteDetil(PolyclinicController controller) throws VONEAppException;


	public void updateRegistration(TbRegistration reg) throws VONEAppException;
}
