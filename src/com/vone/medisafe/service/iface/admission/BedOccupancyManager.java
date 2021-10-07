package com.vone.medisafe.service.iface.admission;




import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbRegistration;

public interface BedOccupancyManager {

	
	public TbBedOccupancy getBedOccupanyByRegId(Integer id) throws VONEAppException;
	

	public List getBedOccupancyByBed(MsBed bed) throws VONEAppException ;
	
	
	
	public List getHistoryMoveByRegistration(TbRegistration reg) throws VONEAppException ;
	
	public TbBedOccupancy getByWhenCreateDate(String date) throws VONEAppException ;
	
	

}
