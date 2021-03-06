package com.vone.medisafe.mapping.pojo.item;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsBlood generated by MyEclipse - Hibernate Tools
 */

public class MsBlood  implements java.io.Serializable {


    // Fields    

     private Integer NBloodId;
     private MsItem msItem;
     private String VBloodCode;
     private String VBloodType;
     private String VBloodGroup;
     private String VBloodRhesus;
     private String VWhoCreate;
     private Date DWhenCreate;
     private String VWhoChange;
     private Date DWhenChange;
     private Set tbBloodExpireds = new HashSet(0);
     private Set tbBloodBanks = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsBlood() {
    }

	/** minimal constructor */
    public MsBlood(Integer NBloodId, String VBloodCode, String VBloodType, String VBloodGroup, String VBloodRhesus, String VWhoCreate, Date DWhenCreate) {
        this.NBloodId = NBloodId;
        this.VBloodCode = VBloodCode;
        this.VBloodType = VBloodType;
        this.VBloodGroup = VBloodGroup;
        this.VBloodRhesus = VBloodRhesus;
        this.VWhoCreate = VWhoCreate;
        this.DWhenCreate = DWhenCreate;
    }
    
    /** full constructor */
    public MsBlood(Integer NBloodId, MsItem msItem, String VBloodCode, String VBloodType, String VBloodGroup, String VBloodRhesus, String VWhoCreate, Date DWhenCreate, String VWhoChange, Date DWhenChange, Set tbBloodExpireds, Set tbBloodBanks) {
        this.NBloodId = NBloodId;
        this.msItem = msItem;
        this.VBloodCode = VBloodCode;
        this.VBloodType = VBloodType;
        this.VBloodGroup = VBloodGroup;
        this.VBloodRhesus = VBloodRhesus;
        this.VWhoCreate = VWhoCreate;
        this.DWhenCreate = DWhenCreate;
        this.VWhoChange = VWhoChange;
        this.DWhenChange = DWhenChange;
        this.tbBloodExpireds = tbBloodExpireds;
        this.tbBloodBanks = tbBloodBanks;
    }

   
    // Property accessors

    public Integer getNBloodId() {
        return this.NBloodId;
    }
    
    public void setNBloodId(Integer NBloodId) {
        this.NBloodId = NBloodId;
    }

    public MsItem getMsItem() {
        return this.msItem;
    }
    
    public void setMsItem(MsItem msItem) {
        this.msItem = msItem;
    }

    public String getVBloodCode() {
        return this.VBloodCode;
    }
    
    public void setVBloodCode(String VBloodCode) {
        this.VBloodCode = VBloodCode;
    }

    public String getVBloodType() {
        return this.VBloodType;
    }
    
    public void setVBloodType(String VBloodType) {
        this.VBloodType = VBloodType;
    }

    public String getVBloodGroup() {
        return this.VBloodGroup;
    }
    
    public void setVBloodGroup(String VBloodGroup) {
        this.VBloodGroup = VBloodGroup;
    }

    public String getVBloodRhesus() {
        return this.VBloodRhesus;
    }
    
    public void setVBloodRhesus(String VBloodRhesus) {
        this.VBloodRhesus = VBloodRhesus;
    }

    public String getVWhoCreate() {
        return this.VWhoCreate;
    }
    
    public void setVWhoCreate(String VWhoCreate) {
        this.VWhoCreate = VWhoCreate;
    }

    public Date getDWhenCreate() {
        return this.DWhenCreate;
    }
    
    public void setDWhenCreate(Date DWhenCreate) {
        this.DWhenCreate = DWhenCreate;
    }

    public String getVWhoChange() {
        return this.VWhoChange;
    }
    
    public void setVWhoChange(String VWhoChange) {
        this.VWhoChange = VWhoChange;
    }

    public Date getDWhenChange() {
        return this.DWhenChange;
    }
    
    public void setDWhenChange(Date DWhenChange) {
        this.DWhenChange = DWhenChange;
    }

    public Set getTbBloodExpireds() {
        return this.tbBloodExpireds;
    }
    
    public void setTbBloodExpireds(Set tbBloodExpireds) {
        this.tbBloodExpireds = tbBloodExpireds;
    }

    public Set getTbBloodBanks() {
        return this.tbBloodBanks;
    }
    
    public void setTbBloodBanks(Set tbBloodBanks) {
        this.tbBloodBanks = tbBloodBanks;
    }
   








}