package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsPatientType generated by MyEclipse - Hibernate Tools
 */

public class MsPatientType  implements java.io.Serializable {


    // Fields    

     private Integer NPatientTypeId;
     private String VTpatient;
     private String VTpatientDesc;
     private String VWhoCreate;
     private String VWhoChange;
     private Date DWhnCreate;
     private Date DWhnChange;
     private Set msMiscFees = new HashSet(0);
     private Set msPatients = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsPatientType() {
    }

	/** minimal constructor */
    public MsPatientType(Integer NPatientTypeId, String VTpatient, String VTpatientDesc) {
        this.NPatientTypeId = NPatientTypeId;
        this.VTpatient = VTpatient;
        this.VTpatientDesc = VTpatientDesc;
    }
    
    /** full constructor */
    public MsPatientType(Integer NPatientTypeId, String VTpatient, String VTpatientDesc, String VWhoCreate, String VWhoChange, Date DWhnCreate, Date DWhnChange, Set msMiscFees, Set msPatients) {
        this.NPatientTypeId = NPatientTypeId;
        this.VTpatient = VTpatient;
        this.VTpatientDesc = VTpatientDesc;
        this.VWhoCreate = VWhoCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnCreate = DWhnCreate;
        this.DWhnChange = DWhnChange;
        this.msMiscFees = msMiscFees;
        this.msPatients = msPatients;
    }

   
    // Property accessors

    public Integer getNPatientTypeId() {
        return this.NPatientTypeId;
    }
    
    public void setNPatientTypeId(Integer NPatientTypeId) {
        this.NPatientTypeId = NPatientTypeId;
    }

    public String getVTpatient() {
        return this.VTpatient;
    }
    
    public void setVTpatient(String VTpatient) {
        this.VTpatient = VTpatient;
    }

    public String getVTpatientDesc() {
        return this.VTpatientDesc;
    }
    
    public void setVTpatientDesc(String VTpatientDesc) {
        this.VTpatientDesc = VTpatientDesc;
    }

    public String getVWhoCreate() {
        return this.VWhoCreate;
    }
    
    public void setVWhoCreate(String VWhoCreate) {
        this.VWhoCreate = VWhoCreate;
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

    public Date getDWhnChange() {
        return this.DWhnChange;
    }
    
    public void setDWhnChange(Date DWhnChange) {
        this.DWhnChange = DWhnChange;
    }

    public Set getMsMiscFees() {
        return this.msMiscFees;
    }
    
    public void setMsMiscFees(Set msMiscFees) {
        this.msMiscFees = msMiscFees;
    }

    public Set getMsPatients() {
        return this.msPatients;
    }
    
    public void setMsPatients(Set msPatients) {
        this.msPatients = msPatients;
    }
   








}