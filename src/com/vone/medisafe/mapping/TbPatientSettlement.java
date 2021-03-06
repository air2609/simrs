package com.vone.medisafe.mapping;

import java.util.Date;


/**
 * TbPatientSettlement generated by MyEclipse - Hibernate Tools
 */

public class TbPatientSettlement  implements java.io.Serializable {


    // Fields    

     private Integer NPsettlementId;
     private TbPatientBill tbPatientBill;
     private MsCreditCardType msCreditCardType;
     private MsBank msBank;
     private MsInsurance msInsurance;
     private Short NPsettlementType;
     private Short NShifId;
     private Double NAmountSettled;
     private String VPatientAccountNo;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;


    // Constructors

    /** default constructor */
    public TbPatientSettlement() {
    }
    // Property accessors

    public Integer getNPsettlementId() {
        return this.NPsettlementId;
    }
    
    public void setNPsettlementId(Integer NPsettlementId) {
        this.NPsettlementId = NPsettlementId;
    }

    public TbPatientBill getTbPatientBill() {
        return this.tbPatientBill;
    }
    
    public void setTbPatientBill(TbPatientBill tbPatientBill) {
        this.tbPatientBill = tbPatientBill;
    }

    public MsCreditCardType getMsCreditCardType() {
        return this.msCreditCardType;
    }
    
    public void setMsCreditCardType(MsCreditCardType msCreditCardType) {
        this.msCreditCardType = msCreditCardType;
    }

    public Double getNAmountSettled() {
		return NAmountSettled;
	}

	public void setNAmountSettled(Double amountSettled) {
		NAmountSettled = amountSettled;
	}

	public String getVPatientAccountNo() {
        return this.VPatientAccountNo;
    }
    
    public void setVPatientAccountNo(String VPatientAccountNo) {
        this.VPatientAccountNo = VPatientAccountNo;
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

	public MsBank getMsBank() {
		return msBank;
	}

	public void setMsBank(MsBank msBank) {
		this.msBank = msBank;
	}

	public MsInsurance getMsInsurance() {
		return msInsurance;
	}

	public void setMsInsurance(MsInsurance msInsurance) {
		this.msInsurance = msInsurance;
	}

	public Short getNPsettlementType() {
		return NPsettlementType;
	}

	public void setNPsettlementType(Short psettlementType) {
		NPsettlementType = psettlementType;
	}

	public Short getNShifId() {
		return NShifId;
	}

	public void setNShifId(Short shifId) {
		NShifId = shifId;
	}
   








}