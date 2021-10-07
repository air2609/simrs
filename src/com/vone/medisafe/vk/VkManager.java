package com.vone.medisafe.vk;

import com.vone.medisafe.common.exception.VONEAppException;

public interface VkManager {

	
	public void getRegistration(VkController controller, int type) 
		throws VONEAppException, InterruptedException;

	public void getNoteDetil(VkController controller) throws VONEAppException;
	

}
