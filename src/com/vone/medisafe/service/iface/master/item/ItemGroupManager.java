package com.vone.medisafe.service.iface.master.item;

import java.util.List;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.pojo.item.MsItemGroup;


public interface ItemGroupManager {
	public List getAllItemGroup() throws VONEAppException;
	public boolean save(MsItemGroup itemGroup) throws VONEAppException;
	public boolean delete(MsItemGroup itemGroup) throws VONEAppException;
}
