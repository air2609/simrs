package com.vone.medisafe.service.iface.master.item;

import java.util.List;

import com.vone.medisafe.mapping.pojo.item.MsBlood;

public interface BloodManager {
	public List getBloods();
	public boolean save(MsBlood blood);
	public boolean delete(MsBlood blood);
}
