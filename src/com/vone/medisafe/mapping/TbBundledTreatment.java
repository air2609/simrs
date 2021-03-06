package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbBundledTreatment generated by MyEclipse - Hibernate Tools
 */

public class TbBundledTreatment  implements java.io.Serializable {


    // Fields    

     private Integer NTbundledId;
     private MsCoa msCoa;
     private MsTreatmentFee msTreatmentFee;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     
     private Set tbBundledTreatUsedTrxes = new HashSet(0);
     private Set tbBundledTrxes = new HashSet(0);
     private Set tbBundledItemUsedTrxes = new HashSet(0);


    // Constructors

    /** default constructor */
    public TbBundledTreatment() {
    }

	/** minimal constructor */
    public TbBundledTreatment(Integer NTbundledId) {
        this.NTbundledId = NTbundledId;
    }
    
    /** full constructor */
    public TbBundledTreatment(Integer NTbundledId, MsCoa msCoa, MsTreatmentFee msTreatmentFee, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbBundledTreatUsedTrxes, Set tbBundledTrxes, Set tbBundledItemUsedTrxes) {
        this.NTbundledId = NTbundledId;
        this.msCoa = msCoa;
        this.msTreatmentFee = msTreatmentFee;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbBundledTreatUsedTrxes = tbBundledTreatUsedTrxes;
        this.tbBundledTrxes = tbBundledTrxes;
        this.tbBundledItemUsedTrxes = tbBundledItemUsedTrxes;
    }

   
    // Property accessors

    public Integer getNTbundledId() {
        return this.NTbundledId;
    }
    
    public void setNTbundledId(Integer NTbundledId) {
        this.NTbundledId = NTbundledId;
    }

    public MsCoa getMsCoa() {
        return this.msCoa;
    }
    
    public void setMsCoa(MsCoa msCoa) {
        this.msCoa = msCoa;
    }

    public MsTreatmentFee getMsTreatmentFee() {
        return this.msTreatmentFee;
    }
    
    public void setMsTreatmentFee(MsTreatmentFee msTreatmentFee) {
        this.msTreatmentFee = msTreatmentFee;
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

    public Set getTbBundledTreatUsedTrxes() {
        return this.tbBundledTreatUsedTrxes;
    }
    
    public void setTbBundledTreatUsedTrxes(Set tbBundledTreatUsedTrxes) {
        this.tbBundledTreatUsedTrxes = tbBundledTreatUsedTrxes;
    }

    public Set getTbBundledTrxes() {
        return this.tbBundledTrxes;
    }
    
    public void setTbBundledTrxes(Set tbBundledTrxes) {
        this.tbBundledTrxes = tbBundledTrxes;
    }

    public Set getTbBundledItemUsedTrxes() {
        return this.tbBundledItemUsedTrxes;
    }
    
    public void setTbBundledItemUsedTrxes(Set tbBundledItemUsedTrxes) {
        this.tbBundledItemUsedTrxes = tbBundledItemUsedTrxes;
    }
   








}