package com.vone.medisafe.mapping.pojo;

import java.util.Date;

import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.pojo.item.MsItem;

public class TbGl  implements java.io.Serializable {

	private Integer id;
	private Integer status;
	private Date from;
	private Date to;
	private String fileLocation;
	
    /** default constructor */
    public TbGl() {
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	
}