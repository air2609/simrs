package com.vone.medisafe.mapping;

import java.util.Date;


/**
 * TbItemInventory generated by MyEclipse - Hibernate Tools
 */

public class TbJournalTrx  implements java.io.Serializable {


    // Fields    

     private Integer NJournalId;
     private String VJournalBatchId;
     private String VVoucherNo;
     private String VDesc;
     private double NDebit;
     private double NCredit;
     private Date DAplDate;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private MsCoa msCoa;


    // Constructors

    /** default constructor */
    public TbJournalTrx() {
    }

    public Date getDAplDate() {
		return DAplDate;
	}

	public double getNCredit() {
		return NCredit;
	}

	public MsCoa getMsCoa() {
		return msCoa;
	}

	public void setMsCoa(MsCoa msCoa) {
		this.msCoa = msCoa;
	}

	public void setNCredit(double credit) {
		NCredit = credit;
	}

	public double getNDebit() {
		return NDebit;
	}

	public void setNDebit(double debit) {
		NDebit = debit;
	}

	public void setDAplDate(Date aplDate) {
		DAplDate = aplDate;
	}



	public Integer getNJournalId() {
		return NJournalId;
	}

	public void setNJournalId(Integer journalId) {
		NJournalId = journalId;
	}

	public String getVDesc() {
		return VDesc;
	}

	public void setVDesc(String desc) {
		VDesc = desc;
	}

	public String getVJournalBatchId() {
		return VJournalBatchId;
	}

	public void setVJournalBatchId(String journalBatchId) {
		VJournalBatchId = journalBatchId;
	}

	public String getVVoucherNo() {
		return VVoucherNo;
	}

	public void setVVoucherNo(String voucherNo) {
		VVoucherNo = voucherNo;
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
   








}