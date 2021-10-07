package com.vone.medisafe.service.iface.master;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsDivision;

public interface DivisionManager {

	public void save(MsDivision division) throws VONEAppException;
	public List getAllDivision() throws VONEAppException;
	public MsDivision getById(Integer id) throws VONEAppException;
	public void delete(MsDivision division) throws VONEAppException;
	public MsDivision getDivisionByCode(String code) throws VONEAppException;
	public boolean deleteById(Integer id) throws VONEAppException;
}
