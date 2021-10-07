package com.vone.medisafe.mapping;

import java.util.Date;

import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;


/**
 * TbItemMutation generated by MyEclipse - Hibernate Tools
 */

public class TbItemMutation  implements java.io.Serializable {


    // Fields    

     private Integer NMitemId;
     private TbBatchItem tbBatchItem;
     private TbItemRequest tbItemRequest;
     private Integer NMitemQty;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Integer NStatus;


    // Constructors

    /** default constructor */
    public TbItemMutation() {
    }

	/** minimal constructor */
    public TbItemMutation(Integer NMitemId, Integer NMitemQty, String VWhoCreate, Date DWhnCreate) {
        this.NMitemId = NMitemId;
        this.NMitemQty = NMitemQty;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
    }
    
    /** full constructor */
    public TbItemMutation(Integer NMitemId, Integer NMitemQty, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange) {
        this.NMitemId = NMitemId;
        this.NMitemQty = NMitemQty;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
    }

   
    // Property accessors

    public Integer getNMitemId() {
        return this.NMitemId;
    }
    
    public void setNMitemId(Integer NMitemId) {
        this.NMitemId = NMitemId;
    }

    public Integer getNMitemQty() {
        return this.NMitemQty;
    }
    
    public void setNMitemQty(Integer NMitemQty) {
        this.NMitemQty = NMitemQty;
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

	public TbItemRequest getTbItemRequest() {
		return tbItemRequest;
	}

	public void setTbItemRequest(TbItemRequest tbItemRequest) {
		this.tbItemRequest = tbItemRequest;
	}

	public TbBatchItem getTbBatchItem() {
		return tbBatchItem;
	}

	public void setTbBatchItem(TbBatchItem tbBatchItem) {
		this.tbBatchItem = tbBatchItem;
	}

	public Integer getNStatus() {
		return NStatus;
	}

	public void setNStatus(Integer status) {
		NStatus = status;
	}

}