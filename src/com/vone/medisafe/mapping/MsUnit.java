package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsUnit generated by MyEclipse - Hibernate Tools
 */

public class MsUnit  implements java.io.Serializable {


    // Fields    

     private Integer NUnitId;
     private Integer unitType;
     private MsDivision msDivision;
     private MsWarehouse msWarehouse;
     private MsCoa coa;
     private String VUnitCode;
     private String VUnitName;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set tbItemMutationsForNItemFromUnitId = new HashSet(0);
     private Set msScreenInUnits = new HashSet(0);
     private Set msStaffInUnits = new HashSet(0);
     private Set tbMedicalRecords = new HashSet(0);
     private Set tbItemMutationsForNItemToUnitId = new HashSet(0);
     private Set msTreatments = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsUnit() {
    }

	/** minimal constructor */
    public MsUnit(Integer NUnitId, MsDivision msDivision, String VUnitCode, String VUnitName) {
        this.NUnitId = NUnitId;
        this.msDivision = msDivision;
        this.VUnitCode = VUnitCode;
        this.VUnitName = VUnitName;
    }
    
    /** full constructor */
    public MsUnit(Integer NUnitId, MsDivision msDivision, String VUnitCode, String VUnitName, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbItemMutationsForNItemFromUnitId, Set msScreenInUnits, Set msStaffInUnits, Set tbMedicalRecords, Set tbItemMutationsForNItemToUnitId, Set msTreatments) {
        this.NUnitId = NUnitId;
        this.msDivision = msDivision;
        this.VUnitCode = VUnitCode;
        this.VUnitName = VUnitName;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbItemMutationsForNItemFromUnitId = tbItemMutationsForNItemFromUnitId;
        this.msScreenInUnits = msScreenInUnits;
        this.msStaffInUnits = msStaffInUnits;
        this.tbMedicalRecords = tbMedicalRecords;
        this.tbItemMutationsForNItemToUnitId = tbItemMutationsForNItemToUnitId;
        this.msTreatments = msTreatments;
    }

   
    // Property accessors

    public Integer getNUnitId() {
        return this.NUnitId;
    }
    
    public void setNUnitId(Integer NUnitId) {
        this.NUnitId = NUnitId;
    }

    public MsWarehouse getMsWarehouse() {
		return msWarehouse;
	}

	public void setMsWarehouse(MsWarehouse msWarehouse) {
		this.msWarehouse = msWarehouse;
	}

	public MsDivision getMsDivision() {
        return this.msDivision;
    }
    
    public void setMsDivision(MsDivision msDivision) {
        this.msDivision = msDivision;
    }

    public String getVUnitCode() {
        return this.VUnitCode;
    }
    
    public void setVUnitCode(String VUnitCode) {
        this.VUnitCode = VUnitCode;
    }

    public String getVUnitName() {
        return this.VUnitName;
    }
    
    public void setVUnitName(String VUnitName) {
        this.VUnitName = VUnitName;
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

    public Set getTbItemMutationsForNItemFromUnitId() {
        return this.tbItemMutationsForNItemFromUnitId;
    }
    
    public void setTbItemMutationsForNItemFromUnitId(Set tbItemMutationsForNItemFromUnitId) {
        this.tbItemMutationsForNItemFromUnitId = tbItemMutationsForNItemFromUnitId;
    }

    public Set getMsScreenInUnits() {
        return this.msScreenInUnits;
    }
    
    public void setMsScreenInUnits(Set msScreenInUnits) {
        this.msScreenInUnits = msScreenInUnits;
    }

    public Set getMsStaffInUnits() {
        return this.msStaffInUnits;
    }
    
    public void setMsStaffInUnits(Set msStaffInUnits) {
        this.msStaffInUnits = msStaffInUnits;
    }

    public Set getTbMedicalRecords() {
        return this.tbMedicalRecords;
    }
    
    public void setTbMedicalRecords(Set tbMedicalRecords) {
        this.tbMedicalRecords = tbMedicalRecords;
    }

    public Set getTbItemMutationsForNItemToUnitId() {
        return this.tbItemMutationsForNItemToUnitId;
    }
    
    public void setTbItemMutationsForNItemToUnitId(Set tbItemMutationsForNItemToUnitId) {
        this.tbItemMutationsForNItemToUnitId = tbItemMutationsForNItemToUnitId;
    }

    public Set getMsTreatments() {
        return this.msTreatments;
    }
    
    public void setMsTreatments(Set msTreatments) {
        this.msTreatments = msTreatments;
    }

	public MsCoa getCoa() {
		return coa;
	}

	public void setCoa(MsCoa coa) {
		this.coa = coa;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}
   








}