package com.vone.medisafe.service.ifaceimpl.master.item;

import java.util.List;


import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.dao.item.ItemGroupDAO;
import com.vone.medisafe.mapping.pojo.item.MsItemGroup;
import com.vone.medisafe.service.iface.master.item.ItemGroupManager;

public class ItemGroupManagerImpl implements ItemGroupManager{

	private ItemGroupDAO dao;
	
	public ItemGroupDAO getDao() {
		return dao;
	}

	public void setDao(ItemGroupDAO dao) {
		this.dao = dao;
	}

	public boolean delete(MsItemGroup itemGroup) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.delete(itemGroup);
	}

	public List getAllItemGroup() throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.getAllItemGroup();
	}

	public boolean save (MsItemGroup itemGroup) throws VONEAppException{
		// TODO Auto-generated method stub
		return dao.save(itemGroup);
	}

}
