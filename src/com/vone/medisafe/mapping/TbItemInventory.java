package com.vone.medisafe.mapping;

import java.util.Date;

import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.mapping.pojo.item.TbBatchItem;


/**
 * TbItemInventory generated by MyEclipse - Hibernate Tools
 */

public class TbItemInventory  implements java.io.Serializable {


    // Fields    

     private Integer NItemInventoryId;
     private MsItem msItem;
     private TbBatchItem tbBatchItem;
     private MsWarehouse msWarehouse;
     private String VItemInvDescId;
     private float NItemInvQty;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;     


    // Constructors

    /** default constructor */
    public TbItemInventory() {
    }

	/** minimal constructor */
    public TbItemInventory(Integer NItemInventoryId, TbBatchItem tbBatchItem, MsWarehouse msWarehouse, String VItemInvDescId, short NItemInvQty) {
        this.NItemInventoryId = NItemInventoryId;
        this.tbBatchItem = tbBatchItem;
        this.msWarehouse = msWarehouse;
        this.VItemInvDescId = VItemInvDescId;
        this.NItemInvQty = NItemInvQty;
    }
    
    /** full constructor */
    public TbItemInventory(Integer NItemInventoryId, MsItem msItem, TbBatchItem tbBatchItem, MsWarehouse msWarehouse, String VItemInvDescId, short NItemInvQty, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange) {
        this.NItemInventoryId = NItemInventoryId;
        this.msItem = msItem;
        this.tbBatchItem = tbBatchItem;
        this.msWarehouse = msWarehouse;
        this.VItemInvDescId = VItemInvDescId;
        this.NItemInvQty = NItemInvQty;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
    }
   
    // Property accessors


	public Integer getNItemInventoryId() {
        return this.NItemInventoryId;
    }
    
    public void setNItemInventoryId(Integer NItemInventoryId) {
        this.NItemInventoryId = NItemInventoryId;
    }

    public MsItem getMsItem() {
        return this.msItem;
    }
    
    public void setMsItem(MsItem msItem) {
        this.msItem = msItem;
    }

    public TbBatchItem getTbBatchItem() {
        return this.tbBatchItem;
    }
    
    public void setTbBatchItem(TbBatchItem tbBatchItem) {
        this.tbBatchItem = tbBatchItem;
    }

    public MsWarehouse getMsWarehouse() {
        return this.msWarehouse;
    }
    
    public void setMsWarehouse(MsWarehouse msWarehouse) {
        this.msWarehouse = msWarehouse;
    }

    public String getVItemInvDescId() {
        return this.VItemInvDescId;
    }
    
    public void setVItemInvDescId(String VItemInvDescId) {
        this.VItemInvDescId = VItemInvDescId;
    }

    public float getNItemInvQty() {
        return this.NItemInvQty;
    }
    
    public void setNItemInvQty(float NItemInvQty) {
        this.NItemInvQty = NItemInvQty;
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
   








}