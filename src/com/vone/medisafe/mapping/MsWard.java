package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsWard generated by MyEclipse - Hibernate Tools
 */

public class MsWard  implements java.io.Serializable {


    // Fields    

     private Integer NWardId;
     private String VWardCode;
     private String VWardName;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set msRooms = new HashSet(0);
     private Set msHalls = new HashSet(0);
     private Set tbRoomReservations = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsWard() {
    }

	/** minimal constructor */
    public MsWard(Integer NWardId, String VWardCode, String VWardName) {
        this.NWardId = NWardId;
        this.VWardCode = VWardCode;
        this.VWardName = VWardName;
    }
    
    /** full constructor */
    public MsWard(Integer NWardId, String VWardCode, String VWardName, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set msRooms, Set msHalls, Set tbRoomReservations) {
        this.NWardId = NWardId;
        this.VWardCode = VWardCode;
        this.VWardName = VWardName;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.msRooms = msRooms;
        this.msHalls = msHalls;
        this.tbRoomReservations = tbRoomReservations;
    }

   
    // Property accessors

    public Integer getNWardId() {
        return this.NWardId;
    }
    
    public void setNWardId(Integer NWardId) {
        this.NWardId = NWardId;
    }

    public String getVWardCode() {
        return this.VWardCode;
    }
    
    public void setVWardCode(String VWardCode) {
        this.VWardCode = VWardCode;
    }

    public String getVWardName() {
        return this.VWardName;
    }
    
    public void setVWardName(String VWardName) {
        this.VWardName = VWardName;
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

    public Set getMsHalls() {
        return this.msHalls;
    }
    
    public void setMsHalls(Set msHalls) {
        this.msHalls = msHalls;
    }

    public Set getTbRoomReservations() {
        return this.tbRoomReservations;
    }
    
    public void setTbRoomReservations(Set tbRoomReservations) {
        this.tbRoomReservations = tbRoomReservations;
    }
   








}