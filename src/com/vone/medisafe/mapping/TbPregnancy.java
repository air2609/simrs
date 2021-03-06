package com.vone.medisafe.mapping;

import java.util.Date;


/**
 * TbPregnancy generated by MyEclipse - Hibernate Tools
 */

public class TbPregnancy  implements java.io.Serializable {


    // Fields    

     private Integer NPregnancyId;
     private TbDiagnose tbDiagnose;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private short NType;


    // Constructors

    /** default constructor */
    public TbPregnancy() {
    }

    
    /** full constructor */
    public TbPregnancy(TbDiagnose tbDiagnose, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, short NType) {
        this.tbDiagnose = tbDiagnose;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.NType = NType;
    }

   
    // Property accessors

    public Integer getNPregnancyId() {
        return this.NPregnancyId;
    }
    
    public void setNPregnancyId(Integer NPregnancyId) {
        this.NPregnancyId = NPregnancyId;
    }

    public TbDiagnose getTbDiagnose() {
        return this.tbDiagnose;
    }
    
    public void setTbDiagnose(TbDiagnose tbDiagnose) {
        this.tbDiagnose = tbDiagnose;
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

    public short getNType() {
        return this.NType;
    }
    
    public void setNType(short NType) {
        this.NType = NType;
    }
   








}