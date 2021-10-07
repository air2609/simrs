package com.vone.medisafe.service.iface.master;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.mapping.MsLabTreatmentDetil;
import com.vone.medisafe.mapping.pojo.MsLabTestDetail;

public interface LabTestDetailManager {
	public boolean save(MsLabTestDetail lab) throws VONEAppException;
	public boolean delete(MsLabTestDetail lab) throws VONEAppException;
	public List getLabtTestDeatils() throws VONEAppException;
	public void getLabtTestDeatils(Listbox labTestDetailList) throws VONEAppException;
	public boolean save(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException;
	public boolean delete(MsLabTreatmentDetil msLabTreatmentDetil) throws VONEAppException;
}
