package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsTreatmentRoomFee generated by MyEclipse - Hibernate Tools
 */

public class MsTreatmentRoomFee  implements java.io.Serializable {


    // Fields    

     private Integer NTroomFeeId;
     private MsTreatmentClass msTreatmentClass;
     private MsTreatmentRoom msTreatmentRoom;
     private MsCoa msCoa;
     private double NTroomFee;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set tbOperationTrxes = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsTreatmentRoomFee() {
    }

	/** minimal constructor */
    public MsTreatmentRoomFee(Integer NTroomFeeId, MsTreatmentClass msTreatmentClass, MsTreatmentRoom msTreatmentRoom, double NTroomFee, String VWhoCreate, Date DWhnCreate) {
        this.NTroomFeeId = NTroomFeeId;
        this.msTreatmentClass = msTreatmentClass;
        this.msTreatmentRoom = msTreatmentRoom;
        this.NTroomFee = NTroomFee;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
    }
    
    /** full constructor */
    public MsTreatmentRoomFee(Integer NTroomFeeId, MsTreatmentClass msTreatmentClass, MsTreatmentRoom msTreatmentRoom, MsCoa msCoa, double NTroomFee, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbOperationTrxes) {
        this.NTroomFeeId = NTroomFeeId;
        this.msTreatmentClass = msTreatmentClass;
        this.msTreatmentRoom = msTreatmentRoom;
        this.msCoa = msCoa;
        this.NTroomFee = NTroomFee;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbOperationTrxes = tbOperationTrxes;
    }

   
    // Property accessors

    public Integer getNTroomFeeId() {
        return this.NTroomFeeId;
    }
    
    public void setNTroomFeeId(Integer NTroomFeeId) {
        this.NTroomFeeId = NTroomFeeId;
    }

    public MsTreatmentClass getMsTreatmentClass() {
        return this.msTreatmentClass;
    }
    
    public void setMsTreatmentClass(MsTreatmentClass msTreatmentClass) {
        this.msTreatmentClass = msTreatmentClass;
    }

    public MsTreatmentRoom getMsTreatmentRoom() {
        return this.msTreatmentRoom;
    }
    
    public void setMsTreatmentRoom(MsTreatmentRoom msTreatmentRoom) {
        this.msTreatmentRoom = msTreatmentRoom;
    }

    public MsCoa getMsCoa() {
        return this.msCoa;
    }
    
    public void setMsCoa(MsCoa msCoa) {
        this.msCoa = msCoa;
    }

    public double getNTroomFee() {
        return this.NTroomFee;
    }
    
    public void setNTroomFee(double NTroomFee) {
        this.NTroomFee = NTroomFee;
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

    public Set getTbOperationTrxes() {
        return this.tbOperationTrxes;
    }
    
    public void setTbOperationTrxes(Set tbOperationTrxes) {
        this.tbOperationTrxes = tbOperationTrxes;
    }
   








}