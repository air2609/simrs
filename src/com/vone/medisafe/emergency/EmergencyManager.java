package com.vone.medisafe.emergency;



import com.vone.medisafe.common.exception.VONEAppException;


public interface EmergencyManager {
	
	
	public void getPatientDetil(EmergencyController controller, int type) 
		throws VONEAppException,InterruptedException;

	public void getNoteDetil(EmergencyController controller) throws VONEAppException;

	public void save(EmergencyController controller) throws VONEAppException, InterruptedException;
	
}
