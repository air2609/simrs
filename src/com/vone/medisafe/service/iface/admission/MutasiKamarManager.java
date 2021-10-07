package com.vone.medisafe.service.iface.admission;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.ui.admission.MutasiKamarController;

public interface MutasiKamarManager {

	public void getPatientRanapDetil(MutasiKamarController controller, int type)
		throws VONEAppException, InterruptedException;

	public void save(MutasiKamarController controller) throws VONEAppException,InterruptedException;

	public void modify(MutasiKamarController controller) throws VONEAppException, InterruptedException;

}
