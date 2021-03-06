package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbPatientBill generated by MyEclipse - Hibernate Tools
 */

public class TbPatientBill  implements java.io.Serializable {


    // Fields    

     private Integer NPbillId;
     private TbRegistration tbRegistration;
     private MsInsurance msInsurance;
     private String VPbillCode;
     private String VNameOnBill;
     private String VAddrOnBill;
     private String VPbillDesc;
     private float NPbillDisc;
     private float NPbillTax;
     private double NPbillSubTtl;
     private double NPbillTtlPaid;
     private Date DSettlementDate;
     private String VCancelationStatus;
     private String VCancelationDesc;
     private Short NPbillStatus;
     private Short NPaymentStatus;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Short idShift;
     private Set tbReturnTrxes = new HashSet(0);
     private Set tbGakinNotes = new HashSet(0);
     private Set tbPatientSettlements = new HashSet(0);
     private Set tbPatientBillDetails = new HashSet(0);
     private Set tbExamination = new HashSet(0);


    // Constructors

    /** default constructor */
    public TbPatientBill() {
    }

	/** minimal constructor */
    public TbPatientBill(Integer NPbillId, String VPbillCode) {
        this.NPbillId = NPbillId;
        this.VPbillCode = VPbillCode;
    }
    
    /** full constructor */
    public TbPatientBill(Integer NPbillId, TbRegistration tbRegistration, MsInsurance msInsurance, TbExamination tbExamination, String VPbillCode, String VNameOnBill, String VAddrOnBill, String VPbillDesc, float NPbillDisc, float NPbillTax, double NPbillSubTtl, double NPbillTtlPaid, Date DSettlementDate, String VCancelationStatus, String VCancelationDesc, String VCloseTrxStatus, String VValidatedStatus, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbReturnTrxes, Set tbGakinNotes, Set tbPatientSettlements, Set tbPatientBillDetails) {
        this.NPbillId = NPbillId;
        this.tbRegistration = tbRegistration;
        this.msInsurance = msInsurance;
        this.VPbillCode = VPbillCode;
        this.VNameOnBill = VNameOnBill;
        this.VAddrOnBill = VAddrOnBill;
        this.VPbillDesc = VPbillDesc;
        this.NPbillDisc = NPbillDisc;
        this.NPbillTax = NPbillTax;
        this.NPbillSubTtl = NPbillSubTtl;
        this.NPbillTtlPaid = NPbillTtlPaid;
        this.DSettlementDate = DSettlementDate;
        this.VCancelationStatus = VCancelationStatus;
        this.VCancelationDesc = VCancelationDesc;
//        this.VCloseTrxStatus = VCloseTrxStatus;
//        this.VValidatedStatus = VValidatedStatus;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbReturnTrxes = tbReturnTrxes;
        this.tbGakinNotes = tbGakinNotes;
        this.tbPatientSettlements = tbPatientSettlements;
        this.tbPatientBillDetails = tbPatientBillDetails;
    }

   
    // Property accessors

    public Integer getNPbillId() {
        return this.NPbillId;
    }
    
    public void setNPbillId(Integer NPbillId) {
        this.NPbillId = NPbillId;
    }

    public TbRegistration getTbRegistration() {
        return this.tbRegistration;
    }
    
    public void setTbRegistration(TbRegistration tbRegistration) {
        this.tbRegistration = tbRegistration;
    }

    public MsInsurance getMsInsurance() {
        return this.msInsurance;
    }
    
    public void setMsInsurance(MsInsurance msInsurance) {
        this.msInsurance = msInsurance;
    }

    public String getVPbillCode() {
        return this.VPbillCode;
    }
    
    public void setVPbillCode(String VPbillCode) {
        this.VPbillCode = VPbillCode;
    }

    public String getVNameOnBill() {
        return this.VNameOnBill;
    }
    
    public void setVNameOnBill(String VNameOnBill) {
        this.VNameOnBill = VNameOnBill;
    }

    public String getVAddrOnBill() {
        return this.VAddrOnBill;
    }
    
    public void setVAddrOnBill(String VAddrOnBill) {
        this.VAddrOnBill = VAddrOnBill;
    }

    public String getVPbillDesc() {
        return this.VPbillDesc;
    }
    
    public void setVPbillDesc(String VPbillDesc) {
        this.VPbillDesc = VPbillDesc;
    }
    
	public float getNPbillDisc() {
		return NPbillDisc;
	}

	public void setNPbillDisc(float pbillDisc) {
		NPbillDisc = pbillDisc;
	}

	public float getNPbillTax() {
		return NPbillTax;
	}

	public void setNPbillTax(float pbillTax) {
		NPbillTax = pbillTax;
	}

	public double getNPbillSubTtl() {
        return this.NPbillSubTtl;
    }
    
    public void setNPbillSubTtl(double NPbillSubTtl) {
        this.NPbillSubTtl = NPbillSubTtl;
    }

    public double getNPbillTtlPaid() {
        return this.NPbillTtlPaid;
    }
    
    public void setNPbillTtlPaid(double NPbillTtlPaid) {
        this.NPbillTtlPaid = NPbillTtlPaid;
    }

    public Date getDSettlementDate() {
        return this.DSettlementDate;
    }
    
    public void setDSettlementDate(Date DSettlementDate) {
        this.DSettlementDate = DSettlementDate;
    }

    public String getVCancelationStatus() {
        return this.VCancelationStatus;
    }
    
    public void setVCancelationStatus(String VCancelationStatus) {
        this.VCancelationStatus = VCancelationStatus;
    }

    public String getVCancelationDesc() {
        return this.VCancelationDesc;
    }
    
    public void setVCancelationDesc(String VCancelationDesc) {
        this.VCancelationDesc = VCancelationDesc;
    }

   
   public Short getNPbillStatus() {
		return NPbillStatus;
	}

	public void setNPbillStatus(Short pbillStatus) {
		NPbillStatus = pbillStatus;
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

    public Set getTbReturnTrxes() {
        return this.tbReturnTrxes;
    }
    
    public void setTbReturnTrxes(Set tbReturnTrxes) {
        this.tbReturnTrxes = tbReturnTrxes;
    }

    public Set getTbGakinNotes() {
        return this.tbGakinNotes;
    }
    
    public void setTbGakinNotes(Set tbGakinNotes) {
        this.tbGakinNotes = tbGakinNotes;
    }

    public Set getTbPatientSettlements() {
        return this.tbPatientSettlements;
    }
    
    public void setTbPatientSettlements(Set tbPatientSettlements) {
        this.tbPatientSettlements = tbPatientSettlements;
    }

    public Set getTbPatientBillDetails() {
        return this.tbPatientBillDetails;
    }
    
    public void setTbPatientBillDetails(Set tbPatientBillDetails) {
        this.tbPatientBillDetails = tbPatientBillDetails;
    }

	public Set getTbExamination() {
		return tbExamination;
	}

	public void setTbExamination(Set tbExamination) {
		this.tbExamination = tbExamination;
	}

	public Short getNPaymentStatus() {
		return NPaymentStatus;
	}

	public void setNPaymentStatus(Short paymentStatus) {
		NPaymentStatus = paymentStatus;
	}
	

	public Short getIdShift() {
		return idShift;
	}

	public void setIdShift(Short idShift) {
		this.idShift = idShift;
	}
   








}