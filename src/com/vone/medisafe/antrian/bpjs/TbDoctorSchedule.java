package com.vone.medisafe.antrian.bpjs;

import java.io.Serializable;
import java.util.Date;

import com.vone.medisafe.mapping.MsStaff;

public class TbDoctorSchedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2156660843299464720L;
	
	private Integer id;
	private MsStaff staff;
	private Date schedule;
	private Date createdAt;
	private String createdBy;
	private String month;
	
	
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public Date getSchedule() {
		return schedule;
	}
	public void setSchedule(Date schedule) {
		this.schedule = schedule;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	
	
}
