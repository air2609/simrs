package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * TbDrugIngredients generated by MyEclipse - Hibernate Tools
 */

public class TbDrugIngredients  implements java.io.Serializable {


    // Fields    

     private Integer NDingrId;
     private TbExamination tbExamination;
     private String VItemComposition;
     private String VDingrId;
     private short NDingrQty;
     private short NEr;
     private String NDingrQuantify;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private double NAmountTrx;
     private String VDiscType;
     private double NDiscAmount;
     private double NAmountAfterDisc;
     private String VReceiptNumber;
     private String AturanPakai;
     private Set tbDrugIngredientsDetails = new HashSet(0);
     private Set tbItemTrxes = new HashSet(0);


    // Constructors

    /** default constructor */
    public TbDrugIngredients() {
    }

	/** minimal constructor */
    public TbDrugIngredients(Integer NDingrId, String VDingrId, short NDingrQty, String NDingrQuantify, String VWhoCreate, Date DWhnCreate) {
        this.NDingrId = NDingrId;
        this.VDingrId = VDingrId;
        this.NDingrQty = NDingrQty;
        this.NDingrQuantify = NDingrQuantify;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
    }
    
    /** full constructor */
    public TbDrugIngredients(Integer NDingrId, TbExamination tbExamination, String VDingrId, short NDingrQty, String NDingrQuantify, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, double NAmountTrx, String VDiscType, double NDiscAmount, double NAmountAfterDisc, Set tbDrugIngredientsDetails, Set tbItemTrxes) {
        this.NDingrId = NDingrId;
        this.tbExamination = tbExamination;
        this.VDingrId = VDingrId;
        this.NDingrQty = NDingrQty;
        this.NDingrQuantify = NDingrQuantify;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.NAmountTrx = NAmountTrx;
        this.VDiscType = VDiscType;
        this.NDiscAmount = NDiscAmount;
        this.NAmountAfterDisc = NAmountAfterDisc;
        this.tbDrugIngredientsDetails = tbDrugIngredientsDetails;
        this.tbItemTrxes = tbItemTrxes;
    }

   
    // Property accessors

    public Integer getNDingrId() {
        return this.NDingrId;
    }
    
    public void setNDingrId(Integer NDingrId) {
        this.NDingrId = NDingrId;
    }

    public TbExamination getTbExamination() {
        return this.tbExamination;
    }
    
    public void setTbExamination(TbExamination tbExamination) {
        this.tbExamination = tbExamination;
    }

    public String getVDingrId() {
        return this.VDingrId;
    }
    
    public void setVDingrId(String VDingrId) {
        this.VDingrId = VDingrId;
    }

    public short getNDingrQty() {
        return this.NDingrQty;
    }
    
    public void setNDingrQty(short NDingrQty) {
        this.NDingrQty = NDingrQty;
    }

    public String getNDingrQuantify() {
        return this.NDingrQuantify;
    }
    
    public void setNDingrQuantify(String NDingrQuantify) {
        this.NDingrQuantify = NDingrQuantify;
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

    public double getNAmountTrx() {
        return this.NAmountTrx;
    }
    
    public void setNAmountTrx(double NAmountTrx) {
        this.NAmountTrx = NAmountTrx;
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

    public Set getTbDrugIngredientsDetails() {
        return this.tbDrugIngredientsDetails;
    }
    
    public void setTbDrugIngredientsDetails(Set tbDrugIngredientsDetails) {
        this.tbDrugIngredientsDetails = tbDrugIngredientsDetails;
    }

    public Set getTbItemTrxes() {
        return this.tbItemTrxes;
    }
    
    public void setTbItemTrxes(Set tbItemTrxes) {
        this.tbItemTrxes = tbItemTrxes;
    }

	public String getVReceiptNumber() {
		return VReceiptNumber;
	}

	public void setVReceiptNumber(String receiptNumber) {
		VReceiptNumber = receiptNumber;
	}

	public String getVItemComposition() {
		return VItemComposition;
	}

	public void setVItemComposition(String itemComposition) {
		VItemComposition = itemComposition;
	}

	public short getNEr() {
		return NEr;
	}

	public void setNEr(short er) {
		NEr = er;
	}

	public String getAturanPakai() {
		return AturanPakai;
	}

	public void setAturanPakai(String aturanPakai) {
		AturanPakai = aturanPakai;
	}
   








}