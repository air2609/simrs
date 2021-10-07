package com.vone.medisafe.common.util;

import com.vone.medisafe.common.exception.VONEAppException;

public interface IdsManager {
	public Integer getSequence(String sequenceName)throws VONEAppException;
}
