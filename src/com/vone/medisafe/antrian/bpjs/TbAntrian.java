package com.vone.medisafe.antrian.bpjs;

import java.util.Date;

import com.vone.medisafe.mapping.MsStaff;

public class TbAntrian {
	
	Integer id;
	MsStaff staff;
	String queueDate;
	Integer queueNo;
	Integer counterNo;
	String bpjsNo;
	String phoneNo;
	String referralNo;
	Integer status;
	Date createdAt;
	Date doneAt;
	String doneBy;
	String poliklinik;
	
	
	public String getPoliklinik() {
		return poliklinik;
	}
	public void setPoliklinik(String poliklinik) {
		this.poliklinik = poliklinik;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MsStaff getStaff() {
		return staff;
	}
	public void setStaff(MsStaff staff) {
		this.staff = staff;
	}
	public String getQueueDate() {
		return queueDate;
	}
	public void setQueueDate(String queueDate) {
		this.queueDate = queueDate;
	}
	public Integer getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(Integer queueNo) {
		this.queueNo = queueNo;
	}
	public Integer getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(Integer counterNo) {
		this.counterNo = counterNo;
	}
	public String getBpjsNo() {
		return bpjsNo;
	}
	public void setBpjsNo(String bpjsNo) {
		this.bpjsNo = bpjsNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getReferralNo() {
		return referralNo;
	}
	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getDoneAt() {
		return doneAt;
	}
	public void setDoneAt(Date doneAt) {
		this.doneAt = doneAt;
	}
	public String getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}
	
	
}
