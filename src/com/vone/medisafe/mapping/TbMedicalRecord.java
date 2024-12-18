package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbMedicalRecord generated by MyEclipse - Hibernate Tools
 */

public class TbMedicalRecord  implements java.io.Serializable {


    // Fields    

     private Integer NMrId;
     private MsPatient msPatient;
     private MsUnit msUnit;
     private String VMrCode;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private String VMrStatus;
     private Set tbTreatmentRoomReservations = new HashSet(0);
     private Set tbRegistrations = new HashSet(0);

     private String ihsNumber;

    public String getIhsNumber() {
        return ihsNumber;
    }

    public void setIhsNumber(String ihsNumber) {
        this.ihsNumber = ihsNumber;
    }
// Constructors

    /** default constructor */
    public TbMedicalRecord() {
    }

	/** minimal constructor */
    public TbMedicalRecord(Integer NMrId, MsPatient msPatient) {
        this.NMrId = NMrId;
        this.msPatient = msPatient;
    }
    
    /** full constructor */
    public TbMedicalRecord(Integer NMrId, MsPatient msPatient, MsUnit msUnit, String VMrCode, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, String VMrStatus, Set tbTreatmentRoomReservations, Set tbRegistrations) {
        this.NMrId = NMrId;
        this.msPatient = msPatient;
        this.msUnit = msUnit;
        this.VMrCode = VMrCode;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.VMrStatus = VMrStatus;
        this.tbTreatmentRoomReservations = tbTreatmentRoomReservations;
        this.tbRegistrations = tbRegistrations;
    }

   
    // Property accessors

    public Integer getNMrId() {
        return this.NMrId;
    }
    
    public void setNMrId(Integer NMrId) {
        this.NMrId = NMrId;
    }

    public MsPatient getMsPatient() {
        return this.msPatient;
    }
    
    public void setMsPatient(MsPatient msPatient) {
        this.msPatient = msPatient;
    }

    public MsUnit getMsUnit() {
        return this.msUnit;
    }
    
    public void setMsUnit(MsUnit msUnit) {
        this.msUnit = msUnit;
    }

    public String getVMrCode() {
        return this.VMrCode;
    }
    
    public void setVMrCode(String VMrCode) {
        this.VMrCode = VMrCode;
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

    public String getVMrStatus() {
        return this.VMrStatus;
    }
    
    public void setVMrStatus(String VMrStatus) {
        this.VMrStatus = VMrStatus;
    }

    public Set getTbTreatmentRoomReservations() {
        return this.tbTreatmentRoomReservations;
    }
    
    public void setTbTreatmentRoomReservations(Set tbTreatmentRoomReservations) {
        this.tbTreatmentRoomReservations = tbTreatmentRoomReservations;
    }

    public Set getTbRegistrations() {
        return this.tbRegistrations;
    }
    
    public void setTbRegistrations(Set tbRegistrations) {
        this.tbRegistrations = tbRegistrations;
    }
   








}