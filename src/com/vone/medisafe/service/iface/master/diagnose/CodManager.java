package com.vone.medisafe.service.iface.master.diagnose;

import java.util.List;

import com.vone.medisafe.mapping.pojo.diagnose.MsCauseOfDeath;

public interface CodManager {
	public boolean save(MsCauseOfDeath cod);
	public boolean delete(MsCauseOfDeath cod);
	public List getCods();
}
