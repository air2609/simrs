package com.vone.medisafe.service.iface.common;

import com.vone.medisafe.common.exception.VONEAppException;

public interface NavigationManager {
	
	public static final int HQL = 1;
	public static final int SQL = 2;
	
	public int getMaxResult(String stQuery, int queryType) throws VONEAppException;
}
