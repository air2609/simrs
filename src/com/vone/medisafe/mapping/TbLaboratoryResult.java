package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbLaboratoryResult generated by MyEclipse - Hibernate Tools
 */

public class TbLaboratoryResult  implements java.io.Serializable {


    // Fields    

     private Integer NLabRsltId;
     private TbExamination tbExamination;
     private String VLabRsltCode;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set tbLaboratoryResultDetails = new HashSet(0);
     private String VJam;
     private String VDrPengirim;
     private String VResep;


    // Constructors

    /** default constructor */
    public TbLaboratoryResult() {
    }

	/** minimal constructor */
    public TbLaboratoryResult(Integer NLabRsltId, TbExamination tbExamination) {
        this.NLabRsltId = NLabRsltId;
        this.tbExamination = tbExamination;
    }
    
    /** full constructor */
    public TbLaboratoryResult(Integer NLabRsltId, TbExamination tbExamination, String VLabRsltCode, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbLaboratoryResultDetails) {
        this.NLabRsltId = NLabRsltId;
        this.tbExamination = tbExamination;
        this.VLabRsltCode = VLabRsltCode;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbLaboratoryResultDetails = tbLaboratoryResultDetails;
    }

   
    // Property accessors

    public Integer getNLabRsltId() {
        return this.NLabRsltId;
    }
    
    public void setNLabRsltId(Integer NLabRsltId) {
        this.NLabRsltId = NLabRsltId;
    }

    public TbExamination getTbExamination() {
        return this.tbExamination;
    }
    
    public void setTbExamination(TbExamination tbExamination) {
        this.tbExamination = tbExamination;
    }

    public String getVLabRsltCode() {
        return this.VLabRsltCode;
    }
    
    public void setVLabRsltCode(String VLabRsltCode) {
        this.VLabRsltCode = VLabRsltCode;
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

    public Set getTbLaboratoryResultDetails() {
        return this.tbLaboratoryResultDetails;
    }
    
    public void setTbLaboratoryResultDetails(Set tbLaboratoryResultDetails) {
        this.tbLaboratoryResultDetails = tbLaboratoryResultDetails;
    }

	public String getVJam() {
		return VJam;
	}

	public void setVJam(String jam) {
		VJam = jam;
	}

	public String getVDrPengirim() {
		return VDrPengirim;
	}

	public void setVDrPengirim(String drPengirim) {
		VDrPengirim = drPengirim;
	}

	public String getVResep() {
		return VResep;
	}

	public void setVResep(String resep) {
		VResep = resep;
	}
   








}