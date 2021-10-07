package com.vone.medisafe.antrian.bpjs;

import java.io.Serializable;
import java.util.Date;

import com.vone.medisafe.mapping.MsStaff;

public class MsPolyDoctor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 230198211866832335L;
	
	private Integer id;
	private MsStaff staff;
	private String photo;
	private String polyStatus;
	private String bookingFlag;
	private Integer maxPatient;
	private Date scheduleFrom;
	private Date scheduleTo;
	private String unit;
	private String doctorDescription;
	private String monSchedule;
	private String tueSchedule;
	private String wedSchedule;
	private String thuSchedule;
	private String friSchedule;
	private String satSchedule;
	private String sunSchedule;
	
	
	
	public String getBookingFlag() {
		return bookingFlag;
	}
	public void setBookingFlag(String bookingFlag) {
		this.bookingFlag = bookingFlag;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPolyStatus() {
		return polyStatus;
	}
	public void setPolyStatus(String polyStatus) {
		this.polyStatus = polyStatus;
	}
	public Integer getMaxPatient() {
		return maxPatient;
	}
	public void setMaxPatient(Integer maxPatient) {
		this.maxPatient = maxPatient;
	}
	public Date getScheduleFrom() {
		return scheduleFrom;
	}
	public void setScheduleFrom(Date scheduleFrom) {
		this.scheduleFrom = scheduleFrom;
	}
	public Date getScheduleTo() {
		return scheduleTo;
	}
	public void setScheduleTo(Date scheduleTo) {
		this.scheduleTo = scheduleTo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDoctorDescription() {
		return doctorDescription;
	}
	public void setDoctorDescription(String doctorDescription) {
		this.doctorDescription = doctorDescription;
	}
	public String getMonSchedule() {
		return monSchedule;
	}
	public void setMonSchedule(String monSchedule) {
		this.monSchedule = monSchedule;
	}
	public String getTueSchedule() {
		return tueSchedule;
	}
	public void setTueSchedule(String tueSchedule) {
		this.tueSchedule = tueSchedule;
	}
	public String getWedSchedule() {
		return wedSchedule;
	}
	public void setWedSchedule(String wedSchedule) {
		this.wedSchedule = wedSchedule;
	}
	public String getThuSchedule() {
		return thuSchedule;
	}
	public void setThuSchedule(String thuSchedule) {
		this.thuSchedule = thuSchedule;
	}
	public String getFriSchedule() {
		return friSchedule;
	}
	public void setFriSchedule(String friSchedule) {
		this.friSchedule = friSchedule;
	}
	public String getSatSchedule() {
		return satSchedule;
	}
	public void setSatSchedule(String satSchedule) {
		this.satSchedule = satSchedule;
	}
	public String getSunSchedule() {
		return sunSchedule;
	}
	public void setSunSchedule(String sunSchedule) {
		this.sunSchedule = sunSchedule;
	}
	
	
	
}
