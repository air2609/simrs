package com.vone.medisafe.mapping;

import java.util.Date;


/**
 * TbViolent generated by MyEclipse - Hibernate Tools
 */

public class TbViolent  implements java.io.Serializable {


    // Fields    

     private Integer NViolentId;
     private TbDiagnose tbDiagnose;
     private String VViolentDesc;
     private String VViolentDamageDesc;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private short NViolentType;


    // Constructors

    /** default constructor */
    public TbViolent() {
    }

	/** minimal constructor */
    public TbViolent(TbDiagnose tbDiagnose, String VViolentDesc, String VViolentDamageDesc) {
        this.tbDiagnose = tbDiagnose;
        this.VViolentDesc = VViolentDesc;
        this.VViolentDamageDesc = VViolentDamageDesc;
    }
    
    /** full constructor */
    public TbViolent(TbDiagnose tbDiagnose, String VViolentDesc, String VViolentDamageDesc, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, short NViolentType) {
        this.tbDiagnose = tbDiagnose;
        this.VViolentDesc = VViolentDesc;
        this.VViolentDamageDesc = VViolentDamageDesc;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.NViolentType = NViolentType;
    }

   
    // Property accessors

    public Integer getNViolentId() {
        return this.NViolentId;
    }
    
    public void setNViolentId(Integer NViolentId) {
        this.NViolentId = NViolentId;
    }

    public TbDiagnose getTbDiagnose() {
        return this.tbDiagnose;
    }
    
    public void setTbDiagnose(TbDiagnose tbDiagnose) {
        this.tbDiagnose = tbDiagnose;
    }

    public String getVViolentDesc() {
        return this.VViolentDesc;
    }
    
    public void setVViolentDesc(String VViolentDesc) {
        this.VViolentDesc = VViolentDesc;
    }

    public String getVViolentDamageDesc() {
        return this.VViolentDamageDesc;
    }
    
    public void setVViolentDamageDesc(String VViolentDamageDesc) {
        this.VViolentDamageDesc = VViolentDamageDesc;
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

    public short getNViolentType() {
        return this.NViolentType;
    }
    
    public void setNViolentType(short NViolentType) {
        this.NViolentType = NViolentType;
    }
   








}