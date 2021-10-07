package com.vone.medisafe.mapping;

import java.util.Date;

import com.vone.medisafe.mapping.pojo.item.MsVendor;

/**
 * MsBank generated by MyEclipse - Hibernate Tools
 */

public class TbAccountPayable  implements java.io.Serializable {


    // Fields    

     private Integer NApId;
     private TbJournalTrx tbJournalTrx;
     private MsVendor msVendor;
     private TbDeliveryOrder tbDO;
     private Double NTotalRemaining;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Date DDueDate;
     
	public Date getDWhnChange() {
		return DWhnChange;
	}
	public void setDWhnChange(Date whnChange) {
		DWhnChange = whnChange;
	}
	public Date getDWhnCreate() {
		return DWhnCreate;
	}
	public void setDWhnCreate(Date whnCreate) {
		DWhnCreate = whnCreate;
	}
	public MsVendor getMsVendor() {
		return msVendor;
	}
	public void setMsVendor(MsVendor msVendor) {
		this.msVendor = msVendor;
	}
	public Integer getNApId() {
		return NApId;
	}
	public void setNApId(Integer apId) {
		NApId = apId;
	}
	public Double getNTotalRemaining() {
		return NTotalRemaining;
	}
	public void setNTotalRemaining(Double totalRemaining) {
		NTotalRemaining = totalRemaining;
	}
	public TbJournalTrx getTbJournalTrx() {
		return tbJournalTrx;
	}
	public void setTbJournalTrx(TbJournalTrx tbJournalTrx) {
		this.tbJournalTrx = tbJournalTrx;
	}
	public String getVWhoChange() {
		return VWhoChange;
	}
	public void setVWhoChange(String whoChange) {
		VWhoChange = whoChange;
	}
	public String getVWhoCreate() {
		return VWhoCreate;
	}
	public void setVWhoCreate(String whoCreate) {
		VWhoCreate = whoCreate;
	}
	public Date getDDueDate() {
		return DDueDate;
	}
	public void setDDueDate(Date dueDate) {
		DDueDate = dueDate;
	}
	public TbDeliveryOrder getTbDO() {
		return tbDO;
	}
	public void setTbDO(TbDeliveryOrder tbDO) {
		this.tbDO = tbDO;
	}
     
     
}