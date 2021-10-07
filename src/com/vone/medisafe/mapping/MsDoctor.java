package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsDoctor generated by MyEclipse - Hibernate Tools
 */

public class MsDoctor  implements java.io.Serializable {


    // Fields    

     private Integer NDoctorId;
     private MsStaff msStaff;
//     private MsMedicStaffGroup msMedicStaffGroup;
     private Short NStaffGroup;
     private String VDocLvlOfExpertise;
     private String VDocStatus;
     private Integer NOutPatientEarnings;
     private String VDocBankAccNo;
     private long NAssistenOf;
     private float NPercentageInPatientWage;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set tbExamDetailsForMsNStaffId = new HashSet(0);
     private Set tbExamDetailsForNStaffId = new HashSet(0);
     private Integer NDocType;
     private Integer flagAntrian;


    // Constructors

    public Integer getFlagAntrian() {
		return flagAntrian;
	}

	public void setFlagAntrian(Integer flagAntrian) {
		this.flagAntrian = flagAntrian;
	}

	/** default constructor */
    public MsDoctor() {
    }

	/** minimal constructor */
    public MsDoctor(Integer NDoctorId, MsStaff msStaff) {
        this.NDoctorId = NDoctorId;
        this.msStaff = msStaff;
    }
    
    /** full constructor */
    public MsDoctor(Integer NDoctorId, MsStaff msStaff, MsMedicStaffGroup msMedicStaffGroup, String VDocLvlOfExpertise, String VDocStatus, Integer NOutPatientEarnings, String VDocBankAccNo, long NAssistenOf, float NPercentageInPatientWage, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbExamDetailsForMsNStaffId, Set tbExamDetailsForNStaffId) {
        this.NDoctorId = NDoctorId;
        this.msStaff = msStaff;
//        this.msMedicStaffGroup = msMedicStaffGroup;
        this.VDocLvlOfExpertise = VDocLvlOfExpertise;
        this.VDocStatus = VDocStatus;
        this.NOutPatientEarnings = NOutPatientEarnings;
        this.VDocBankAccNo = VDocBankAccNo;
        this.NAssistenOf = NAssistenOf;
        this.NPercentageInPatientWage = NPercentageInPatientWage;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbExamDetailsForMsNStaffId = tbExamDetailsForMsNStaffId;
        this.tbExamDetailsForNStaffId = tbExamDetailsForNStaffId;
    }

   
    // Property accessors

    public Integer getNDoctorId() {
        return this.NDoctorId;
    }
    
    public void setNDoctorId(Integer NDoctorId) {
        this.NDoctorId = NDoctorId;
    }

    public MsStaff getMsStaff() {
        return this.msStaff;
    }
    
    public void setMsStaff(MsStaff msStaff) {
        this.msStaff = msStaff;
    }

//    public MsMedicStaffGroup getMsMedicStaffGroup() {
//        return this.msMedicStaffGroup;
//    }
//    
//    public void setMsMedicStaffGroup(MsMedicStaffGroup msMedicStaffGroup) {
//        this.msMedicStaffGroup = msMedicStaffGroup;
//    }

    public String getVDocLvlOfExpertise() {
        return this.VDocLvlOfExpertise;
    }
    
    public void setVDocLvlOfExpertise(String VDocLvlOfExpertise) {
        this.VDocLvlOfExpertise = VDocLvlOfExpertise;
    }

    public String getVDocStatus() {
        return this.VDocStatus;
    }
    
    public void setVDocStatus(String VDocStatus) {
        this.VDocStatus = VDocStatus;
    }

    public Integer getNOutPatientEarnings() {
        return this.NOutPatientEarnings;
    }
    
    public void setNOutPatientEarnings(Integer NOutPatientEarnings) {
        this.NOutPatientEarnings = NOutPatientEarnings;
    }

    public String getVDocBankAccNo() {
        return this.VDocBankAccNo;
    }
    
    public void setVDocBankAccNo(String VDocBankAccNo) {
        this.VDocBankAccNo = VDocBankAccNo;
    }

    public long getNAssistenOf() {
        return this.NAssistenOf;
    }
    
    public void setNAssistenOf(long NAssistenOf) {
        this.NAssistenOf = NAssistenOf;
    }

    public float getNPercentageInPatientWage() {
        return this.NPercentageInPatientWage;
    }
    
    public void setNPercentageInPatientWage(float NPercentageInPatientWage) {
        this.NPercentageInPatientWage = NPercentageInPatientWage;
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

    public Set getTbExamDetailsForMsNStaffId() {
        return this.tbExamDetailsForMsNStaffId;
    }
    
    public void setTbExamDetailsForMsNStaffId(Set tbExamDetailsForMsNStaffId) {
        this.tbExamDetailsForMsNStaffId = tbExamDetailsForMsNStaffId;
    }

    public Set getTbExamDetailsForNStaffId() {
        return this.tbExamDetailsForNStaffId;
    }
    
    public void setTbExamDetailsForNStaffId(Set tbExamDetailsForNStaffId) {
        this.tbExamDetailsForNStaffId = tbExamDetailsForNStaffId;
    }

	public Short getNStaffGroup() {
		return NStaffGroup;
	}

	public void setNStaffGroup(Short staffGroup) {
		NStaffGroup = staffGroup;
	}

	public Integer getNDocType() {
		return NDocType;
	}

	public void setNDocType(Integer docType) {
		NDocType = docType;
	}

}