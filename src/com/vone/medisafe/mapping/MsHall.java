package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsHall generated by MyEclipse - Hibernate Tools
 */

public class MsHall  implements java.io.Serializable {


    // Fields    

     private Integer NHallId;
     private MsWard msWard;
     private MsTreatmentClass msTreatmentClass;
     private String VHallCode;
     private String VHallName;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set msRooms = new HashSet(0);
     private Set tbRoomReservations = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsHall() {
    }

	/** minimal constructor */
    public MsHall(Integer NHallId, MsWard msWard, MsTreatmentClass msTreatmentClass, String VHallCode, String VHallName) {
        this.NHallId = NHallId;
        this.msWard = msWard;
        this.msTreatmentClass = msTreatmentClass;
        this.VHallCode = VHallCode;
        this.VHallName = VHallName;
    }
    
    /** full constructor */
    public MsHall(Integer NHallId, MsWard msWard, MsTreatmentClass msTreatmentClass, String VHallCode, String VHallName, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set msRooms, Set tbRoomReservations) {
        this.NHallId = NHallId;
        this.msWard = msWard;
        this.msTreatmentClass = msTreatmentClass;
        this.VHallCode = VHallCode;
        this.VHallName = VHallName;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.msRooms = msRooms;
        this.tbRoomReservations = tbRoomReservations;
    }

   
    // Property accessors

    public Integer getNHallId() {
        return this.NHallId;
    }
    
    public void setNHallId(Integer NHallId) {
        this.NHallId = NHallId;
    }

    public MsWard getMsWard() {
        return this.msWard;
    }
    
    public void setMsWard(MsWard msWard) {
        this.msWard = msWard;
    }

    public MsTreatmentClass getMsTreatmentClass() {
        return this.msTreatmentClass;
    }
    
    public void setMsTreatmentClass(MsTreatmentClass msTreatmentClass) {
        this.msTreatmentClass = msTreatmentClass;
    }

    public String getVHallCode() {
        return this.VHallCode;
    }
    
    public void setVHallCode(String VHallCode) {
        this.VHallCode = VHallCode;
    }

    public String getVHallName() {
        return this.VHallName;
    }
    
    public void setVHallName(String VHallName) {
        this.VHallName = VHallName;
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

    public Set getMsRooms() {
        return this.msRooms;
    }
    
    public void setMsRooms(Set msRooms) {
        this.msRooms = msRooms;
    }

    public Set getTbRoomReservations() {
        return this.tbRoomReservations;
    }
    
    public void setTbRoomReservations(Set tbRoomReservations) {
        this.tbRoomReservations = tbRoomReservations;
    }
   








}