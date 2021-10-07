package com.vone.medisafe.ward;




import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.TbRegistration;


/**
 * WardTransactionManagerImpl.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 09 2006
 * @category business logic
 */  
 

public interface WardTransactionManager {
	
			
	public void save(WardTransactionController controller) throws VONEAppException, InterruptedException;

	public void getRegistrationDetil(WardTransactionController controller, int type) throws VONEAppException,
		InterruptedException;

	public void getNoteDetil(WardTransactionController controller) throws VONEAppException, InterruptedException;

	public void updateRegistration(TbRegistration reg) throws VONEAppException;
	
	
	public void getRegistration(int type, Window win) throws VONEAppException, InterruptedException;
}
