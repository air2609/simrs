package com.vone.medisafe.service.ifaceimpl.admission;


import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsBed;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedOccupancyDAO;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.service.iface.admission.BedOccupancyManager;

public class BedOccupanyManagerImpl implements BedOccupancyManager{
	
	private TbBedOccupancyDAO dao;
	
	public TbBedOccupancy getBedOccupanyByRegId(Integer id) throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getBedOccupanyByRegId(id);
	}

	public TbBedOccupancyDAO getDao() throws VONEAppException  {
		return dao;
	}

	public void setDao(TbBedOccupancyDAO dao)  throws VONEAppException {
		this.dao = dao;
	}



	

	public List getHistoryMoveByRegistration(TbRegistration reg)  throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getHistoryMove(reg);
	}

	public TbBedOccupancy getByWhenCreateDate(String date)  throws VONEAppException {
		// TODO Auto-generated method stub
		return dao.getByCreateDate(date);
	}

	
	public List getBedOccupancyByBed(MsBed bed) throws VONEAppException {
		// TODO Auto-generated method stub
		return null;
	}
}
