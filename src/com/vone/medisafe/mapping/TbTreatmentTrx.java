package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbTreatmentTrx generated by MyEclipse - Hibernate Tools
 */

public class TbTreatmentTrx  implements java.io.Serializable {


    // Fields    

     private Integer NTreatmentId;
     private TbExamination tbExamination;
     private MsStaff msDoctor;
     private MsStaff anastesiStaff;
     private MsTreatmentFee msTreatmentFee;
     private double NAmountTrx;
     private short NQty;
     private String VWhoCreate;
     private String VWhoChange;
     private Date DWhnCreate;
     private Date DWhnChange;
     private String VDiscType;
     private double NDiscAmount;
     private double NAmountAfterDisc;
     private Set tbTreatmentItemUsedTrxes = new HashSet(0);


    // Constructors

    /** default constructor */
    public TbTreatmentTrx() {
    }

	/** minimal constructor */
    public TbTreatmentTrx(Integer NTreatmentId, TbExamination tbExamination, MsTreatmentFee msTreatmentFee) {
        this.NTreatmentId = NTreatmentId;
        this.tbExamination = tbExamination;
        this.msTreatmentFee = msTreatmentFee;
    }
    
    // Property accessors

    public Integer getNTreatmentId() {
        return this.NTreatmentId;
    }
    
    public void setNTreatmentId(Integer NTreatmentId) {
        this.NTreatmentId = NTreatmentId;
    }

    public TbExamination getTbExamination() {
        return this.tbExamination;
    }
    
    public void setTbExamination(TbExamination tbExamination) {
        this.tbExamination = tbExamination;
    }
    
    public MsStaff getMsDoctor() {
        return this.msDoctor;
    }
    
    public void setMsDoctor(MsStaff msDoctor) {
        this.msDoctor = msDoctor;
    }

    public MsTreatmentFee getMsTreatmentFee() {
        return this.msTreatmentFee;
    }
    
    public void setMsTreatmentFee(MsTreatmentFee msTreatmentFee) {
        this.msTreatmentFee = msTreatmentFee;
    }

    public double getNAmountTrx() {
        return this.NAmountTrx;
    }
    
    public void setNAmountTrx(double NAmountTrx) {
        this.NAmountTrx = NAmountTrx;
    }

    public short getNQty() {
        return this.NQty;
    }
    
    public void setNQty(short NQty) {
        this.NQty = NQty;
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

    public String getVDiscType() {
        return this.VDiscType;
    }
    
    public void setVDiscType(String VDiscType) {
        this.VDiscType = VDiscType;
    }

    public double getNDiscAmount() {
        return this.NDiscAmount;
    }
    
    public void setNDiscAmount(double NDiscAmount) {
        this.NDiscAmount = NDiscAmount;
    }

    public double getNAmountAfterDisc() {
        return this.NAmountAfterDisc;
    }
    
    public void setNAmountAfterDisc(double NAmountAfterDisc) {
        this.NAmountAfterDisc = NAmountAfterDisc;
    }

    public Set getTbTreatmentItemUsedTrxes() {
        return this.tbTreatmentItemUsedTrxes;
    }
    
    public void setTbTreatmentItemUsedTrxes(Set tbTreatmentItemUsedTrxes) {
        this.tbTreatmentItemUsedTrxes = tbTreatmentItemUsedTrxes;
    }


	public MsStaff getAnastesiStaff() {
		return anastesiStaff;
	}

	public void setAnastesiStaff(MsStaff anastesiStaff) {
		this.anastesiStaff = anastesiStaff;
	}


	

	








}