package com.vone.medisafe.mapping.pojo;

import java.util.Date;

import com.vone.medisafe.mapping.MsWarehouse;
import com.vone.medisafe.mapping.pojo.item.MsItem;

public class TbItemRequest  implements java.io.Serializable {

	private Integer NIrId;
	private MsWarehouse WarehouseSource; 
	private MsWarehouse WarehouseTarget; 
	private MsItem msItem;
	
	private Integer NQtyReq;
	private Integer NQtySent;
	private Integer NStatus;
	private String VWhoCreate;
	private String VWhoChange;
	private Date DWhnCreate;
	private Date DWhnChange;
	private String VRequestCode;
	
    /** default constructor */
    public TbItemRequest() {
    }

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

	public Integer getNIrId() {
		return NIrId;
	}

	public void setNIrId(Integer irId) {
		NIrId = irId;
	}

	public Integer getNQtyReq() {
		return NQtyReq;
	}

	public void setNQtyReq(Integer qtyReq) {
		NQtyReq = qtyReq;
	}

	public Integer getNQtySent() {
		return NQtySent;
	}

	public void setNQtySent(Integer qtySent) {
		NQtySent = qtySent;
	}

	public Integer getNStatus() {
		return NStatus;
	}

	public void setNStatus(Integer status) {
		NStatus = status;
	}

	public String getVRequestCode() {
		return VRequestCode;
	}

	public void setVRequestCode(String requestCode) {
		VRequestCode = requestCode;
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

	public MsWarehouse getWarehouseSource() {
		return WarehouseSource;
	}

	public void setWarehouseSource(MsWarehouse warehouseSource) {
		WarehouseSource = warehouseSource;
	}

	public MsWarehouse getWarehouseTarget() {
		return WarehouseTarget;
	}

	public void setWarehouseTarget(MsWarehouse warehouseTarget) {
		WarehouseTarget = warehouseTarget;
	}

	public MsItem getMsItem() {
		return msItem;
	}

	public void setMsItem(MsItem msItem) {
		this.msItem = msItem;
	}

}