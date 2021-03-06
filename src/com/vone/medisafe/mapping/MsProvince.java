package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsProvince generated by MyEclipse - Hibernate Tools
 */

public class MsProvince  implements java.io.Serializable {


    // Fields    

     private Integer NProvinceId;
     private String VProvinceId;
     private String VProvinceName;
     private String VWhoChange;
     private Date DWhnCreate;
     private String VWhoCreate;
     private Date DWhnChange;
     private Set msPatients = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsProvince() {
    }

	/** minimal constructor */
    public MsProvince(Integer NProvinceId, String VProvinceId, String VProvinceName) {
        this.NProvinceId = NProvinceId;
        this.VProvinceId = VProvinceId;
        this.VProvinceName = VProvinceName;
    }
    
    /** full constructor */
    public MsProvince(Integer NProvinceId, String VProvinceId, String VProvinceName, String VWhoChange, Date DWhnCreate, String VWhoCreate, Date DWhnChange, Set msPatients) {
        this.NProvinceId = NProvinceId;
        this.VProvinceId = VProvinceId;
        this.VProvinceName = VProvinceName;
        this.VWhoChange = VWhoChange;
        this.DWhnCreate = DWhnCreate;
        this.VWhoCreate = VWhoCreate;
        this.DWhnChange = DWhnChange;
        this.msPatients = msPatients;
    }

   
    // Property accessors

    public Integer getNProvinceId() {
        return this.NProvinceId;
    }
    
    public void setNProvinceId(Integer NProvinceId) {
        this.NProvinceId = NProvinceId;
    }

    public String getVProvinceId() {
        return this.VProvinceId;
    }
    
    public void setVProvinceId(String VProvinceId) {
        this.VProvinceId = VProvinceId;
    }

    public String getVProvinceName() {
        return this.VProvinceName;
    }
    
    public void setVProvinceName(String VProvinceName) {
        this.VProvinceName = VProvinceName;
    }

    public String getVWhoChange() {
        return this.VWhoChange;
    }
    
    public void setVWhoChange(String VWhoChange) {
        this.VWhoChange = VWhoChange;
    }

    public Date getDWhnCreate() {
        return this.DWhnCreate;
    }
    
    public void setDWhnCreate(Date DWhnCreate) {
        this.DWhnCreate = DWhnCreate;
    }

    public String getVWhoCreate() {
        return this.VWhoCreate;
    }
    
    public void setVWhoCreate(String VWhoCreate) {
        this.VWhoCreate = VWhoCreate;
    }

    public Date getDWhnChange() {
        return this.DWhnChange;
    }
    
    public void setDWhnChange(Date DWhnChange) {
        this.DWhnChange = DWhnChange;
    }

    public Set getMsPatients() {
        return this.msPatients;
    }
    
    public void setMsPatients(Set msPatients) {
        this.msPatients = msPatients;
    }
   








}