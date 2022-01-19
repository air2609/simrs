package com.vone.medisafe.mapping;

import java.util.Date;

public class TbJournalTrxHistory implements java.io.Serializable{
	
	private Integer id;
	private Integer journalId;
    private String journalBatchId;
    private String voucherNo;
    private String descrpiton;
    private double debit;
    private double credit;
    private Date aplDate;
    private String whoCreate;
    private Date whnCreate;
    private String whoChange;
    private Date DWhnChange;
    private Integer coaId;
    private String actionBy;
    private String actionType;
    private Date actionDate;
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public Integer getJournalId() {
		return journalId;
	}
	public void setJournalId(Integer journalId) {
		this.journalId = journalId;
	}
	public String getJournalBatchId() {
		return journalBatchId;
	}
	public void setJournalBatchId(String journalBatchId) {
		this.journalBatchId = journalBatchId;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getDescrpiton() {
		return descrpiton;
	}
	public void setDescrpiton(String descrpiton) {
		this.descrpiton = descrpiton;
	}
	public double getDebit() {
		return debit;
	}
	public void setDebit(double debit) {
		this.debit = debit;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public Date getAplDate() {
		return aplDate;
	}
	public void setAplDate(Date aplDate) {
		this.aplDate = aplDate;
	}
	public String getWhoCreate() {
		return whoCreate;
	}
	public void setWhoCreate(String whoCreate) {
		this.whoCreate = whoCreate;
	}
	public Date getWhnCreate() {
		return whnCreate;
	}
	public void setWhnCreate(Date whnCreate) {
		this.whnCreate = whnCreate;
	}
	public String getWhoChange() {
		return whoChange;
	}
	public void setWhoChange(String whoChange) {
		this.whoChange = whoChange;
	}
	public Date getDWhnChange() {
		return DWhnChange;
	}
	public void setDWhnChange(Date dWhnChange) {
		DWhnChange = dWhnChange;
	}
	public Integer getCoaId() {
		return coaId;
	}
	public void setCoaId(Integer codId) {
		this.coaId = codId;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
    
    

}
