package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vone.medisafe.mapping.pojo.item.MsItemMeasurement;


/**
 * TbDeliveryOrder generated by MyEclipse - Hibernate Tools
 */

public class TbDeliveryOrder  implements java.io.Serializable {


    // Fields    

     private Integer NDoId;
     private MsStaff msStaffByNApproverId;
     private TbPurchaseOrder tbPurchaseOrder;
     private MsWarehouse msWarehouse;
     private MsStaff msStaffByNIssuerId;
     private String VDoCode;
     private String VTaxType;
     private Double NDoTax;
     private Double NTotal;
     private Double NTotalAfterDisc;
     private Double NTotalAfterPpn;     
     private String VWhoCreate;
     private String VDoStatus;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Date DRecDate;
     private Set tbDeliveryOrderDetails = new HashSet(0);


    // Constructors

    /** default constructor */
    public TbDeliveryOrder() {
    }

	/** minimal constructor */
    public TbDeliveryOrder(Integer NDoId, MsStaff msStaffByNApproverId, TbPurchaseOrder tbPurchaseOrder, MsStaff msStaffByNIssuerId, String VDoCode, double NDoTax, String VWhoCreate, Date DWhnCreate) {
        this.NDoId = NDoId;
        this.msStaffByNApproverId = msStaffByNApproverId;
        this.tbPurchaseOrder = tbPurchaseOrder;
        this.msStaffByNIssuerId = msStaffByNIssuerId;
        this.VDoCode = VDoCode;
        this.NDoTax = NDoTax;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
    }
    
    /** full constructor */
    public TbDeliveryOrder(Integer NDoId, MsStaff msStaffByNApproverId, TbPurchaseOrder tbPurchaseOrder, MsStaff msStaffByNIssuerId, String VDoCode, double NDoTax, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbDeliveryOrderDetails) {
        this.NDoId = NDoId;
        this.msStaffByNApproverId = msStaffByNApproverId;
        this.tbPurchaseOrder = tbPurchaseOrder;
        this.msStaffByNIssuerId = msStaffByNIssuerId;
        this.VDoCode = VDoCode;
        this.NDoTax = NDoTax;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbDeliveryOrderDetails = tbDeliveryOrderDetails;
    }

   
    // Property accessors

    public String getVTaxType() {
		return VTaxType;
	}

	public void setVTaxType(String taxType) {
		VTaxType = taxType;
	}

	public Integer getNDoId() {
        return this.NDoId;
    }
    
    public void setNDoId(Integer NDoId) {
        this.NDoId = NDoId;
    }

    public MsStaff getMsStaffByNApproverId() {
        return this.msStaffByNApproverId;
    }
    
    public void setMsStaffByNApproverId(MsStaff msStaffByNApproverId) {
        this.msStaffByNApproverId = msStaffByNApproverId;
    }

    public TbPurchaseOrder getTbPurchaseOrder() {
        return this.tbPurchaseOrder;
    }
    
    public void setTbPurchaseOrder(TbPurchaseOrder tbPurchaseOrder) {
        this.tbPurchaseOrder = tbPurchaseOrder;
    }

    public MsStaff getMsStaffByNIssuerId() {
        return this.msStaffByNIssuerId;
    }
    
    public MsWarehouse getMsWarehouse() {
		return msWarehouse;
	}

	public void setMsWarehouse(MsWarehouse msWarehouse) {
		this.msWarehouse = msWarehouse;
	}

	public void setMsStaffByNIssuerId(MsStaff msStaffByNIssuerId) {
        this.msStaffByNIssuerId = msStaffByNIssuerId;
    }

    public String getVDoCode() {
        return this.VDoCode;
    }
    
    public void setVDoCode(String VDoCode) {
        this.VDoCode = VDoCode;
    }

    public String getVWhoCreate() {
        return this.VWhoCreate;
    }
    
    public void setVWhoCreate(String VWhoCreate) {
        this.VWhoCreate = VWhoCreate;
    }

    public Date getDWhnCreate() {
        return this.DWhnCreate;
    }
    
    public void setDWhnCreate(Date DWhnCreate) {
        this.DWhnCreate = DWhnCreate;
    }

    public String getVWhoChange() {
        return this.VWhoChange;
    }
    
    public void setVWhoChange(String VWhoChange) {
        this.VWhoChange = VWhoChange;
    }

    public Date getDWhnChange() {
        return this.DWhnChange;
    }
    
    public void setDWhnChange(Date DWhnChange) {
        this.DWhnChange = DWhnChange;
    }

    public Set getTbDeliveryOrderDetails() {
        return this.tbDeliveryOrderDetails;
    }
    
    public void setTbDeliveryOrderDetails(Set tbDeliveryOrderDetails) {
        this.tbDeliveryOrderDetails = tbDeliveryOrderDetails;
    }

	public String getVDoStatus() {
		return VDoStatus;
	}

	public void setVDoStatus(String doStatus) {
		VDoStatus = doStatus;
	}

	public Double getNDoTax() {
		return NDoTax;
	}

	public void setNDoTax(Double doTax) {
		NDoTax = doTax;
	}

	public Date getDRecDate() {
		return DRecDate;
	}

	public void setDRecDate(Date recDate) {
		DRecDate = recDate;
	}

	public Double getNTotal() {
		return NTotal;
	}

	public void setNTotal(Double total) {
		NTotal = total;
	}

	public Double getNTotalAfterDisc() {
		return NTotalAfterDisc;
	}

	public void setNTotalAfterDisc(Double totalAfterDisc) {
		NTotalAfterDisc = totalAfterDisc;
	}

	public Double getNTotalAfterPpn() {
		return NTotalAfterPpn;
	}

	public void setNTotalAfterPpn(Double totalAfterPpn) {
		NTotalAfterPpn = totalAfterPpn;
	}

}