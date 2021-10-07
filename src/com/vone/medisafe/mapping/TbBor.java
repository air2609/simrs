package com.vone.medisafe.mapping;

import java.util.Date;

public class TbBor implements java.io.Serializable{
	
	private static final long serialVersionUID = -271662250408997991L;
	
	private Integer id;
	private Integer bedId;
	private Integer tclassId;
	private Integer hallId;
	private Integer bedTrxId;
	private Date bedDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBedId() {
		return bedId;
	}
	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}
	public Integer getTclassId() {
		return tclassId;
	}
	public void setTclassId(Integer tclassId) {
		this.tclassId = tclassId;
	}
	public Integer getHallId() {
		return hallId;
	}
	public void setHallId(Integer hallId) {
		this.hallId = hallId;
	}
	public Integer getBedTrxId() {
		return bedTrxId;
	}
	public void setBedTrxId(Integer bedTrxId) {
		this.bedTrxId = bedTrxId;
	}
	public Date getBedDate() {
		return bedDate;
	}
	public void setBedDate(Date bedDate) {
		this.bedDate = bedDate;
	}
	
	
	

}
