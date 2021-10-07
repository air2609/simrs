package com.vone.medisafe.mapping.pojo.item;

import java.util.Date;


/**
 * MsItemSupplied generated by MyEclipse - Hibernate Tools
 */

public class MsItemSupplied  implements java.io.Serializable {


    // Fields    

     private Integer NItemSuppliedId;
     private MsItem msItem;
     private MsVendor msVendor;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;


    // Constructors

    /** default constructor */
    public MsItemSupplied() {
    }

	/** minimal constructor */
    public MsItemSupplied(Integer NItemSuppliedId, MsItem msItem, MsVendor msVendor, String VWhoCreate, Date DWhnCreate) {
        this.NItemSuppliedId = NItemSuppliedId;
        this.msItem = msItem;
        this.msVendor = msVendor;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
    }
    
    /** full constructor */
    public MsItemSupplied(Integer NItemSuppliedId, MsItem msItem, MsVendor msVendor, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange) {
        this.NItemSuppliedId = NItemSuppliedId;
        this.msItem = msItem;
        this.msVendor = msVendor;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
    }

   
    // Property accessors

    public Integer getNItemSuppliedId() {
        return this.NItemSuppliedId;
    }
    
    public void setNItemSuppliedId(Integer NItemSuppliedId) {
        this.NItemSuppliedId = NItemSuppliedId;
    }

    public MsItem getMsItem() {
        return this.msItem;
    }
    
    public void setMsItem(MsItem msItem) {
        this.msItem = msItem;
    }

    public MsVendor getMsVendor() {
        return this.msVendor;
    }
    
    public void setMsVendor(MsVendor msVendor) {
        this.msVendor = msVendor;
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