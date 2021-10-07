package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsStaff generated by MyEclipse - Hibernate Tools
 */

public class MsStaff  implements java.io.Serializable {


    // Fields    

     private Integer NStaffId;
     private MsCoa msCoa;
     private String VStaffName;
     private String VStaffAddr;
     private String VStaffPhNo;
     private Date DStaffHiredDate;
     private Date DStaffFiredDate;
     private double NStaffSalary;
     private short NStaffRole;
     private String VStaffCode;
     private String VWhoChange;
     private Date DWhnCreate;
     private String VWhoCreate;
     private Date DWhnChange;
     private Set tbPurchaseOrdersForNApproverId = new HashSet(0);
     private Set msDoctors = new HashSet(0);
     private Set tbDeliveryOrdersForNApproverId = new HashSet(0);
     private Set msStaffInUnits = new HashSet(0);
     private Set tbDeliveryOrdersForNIssuerId = new HashSet(0);
     private Set tbPurchaseOrdersForNIssuerId = new HashSet(0);
     private Set tbPurchaseRequestsForNIssuerId = new HashSet(0);
     private Set msUsers = new HashSet(0);
     private Set tbPurchaseRequestsForNApproverId = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsStaff() {
    }

	/** minimal constructor */
    public MsStaff(Integer NStaffId, String VStaffName) {
        this.NStaffId = NStaffId;
        this.VStaffName = VStaffName;
    }
    
    /** full constructor */
    public MsStaff(Integer NStaffId, MsCoa msCoa, String VStaffName, String VStaffAddr, String VStaffPhNo, Date DStaffHiredDate, Date DStaffFiredDate, double NStaffSalary, short NStaffRole, String VStaffCode, String VWhoChange, Date DWhnCreate, String VWhoCreate, Date DWhnChange, Set tbPurchaseOrdersForNApproverId, Set msDoctors, Set tbDeliveryOrdersForNApproverId, Set msStaffInUnits, Set tbDeliveryOrdersForNIssuerId, Set tbPurchaseOrdersForNIssuerId, Set tbPurchaseRequestsForNIssuerId, Set msUsers, Set tbPurchaseRequestsForNApproverId) {
        this.NStaffId = NStaffId;
        this.msCoa = msCoa;
        this.VStaffName = VStaffName;
        this.VStaffAddr = VStaffAddr;
        this.VStaffPhNo = VStaffPhNo;
        this.DStaffHiredDate = DStaffHiredDate;
        this.DStaffFiredDate = DStaffFiredDate;
        this.NStaffSalary = NStaffSalary;
        this.NStaffRole = NStaffRole;
        this.VStaffCode = VStaffCode;
        this.VWhoChange = VWhoChange;
        this.DWhnCreate = DWhnCreate;
        this.VWhoCreate = VWhoCreate;
        this.DWhnChange = DWhnChange;
        this.tbPurchaseOrdersForNApproverId = tbPurchaseOrdersForNApproverId;
        this.msDoctors = msDoctors;
        this.tbDeliveryOrdersForNApproverId = tbDeliveryOrdersForNApproverId;
        this.msStaffInUnits = msStaffInUnits;
        this.tbDeliveryOrdersForNIssuerId = tbDeliveryOrdersForNIssuerId;
        this.tbPurchaseOrdersForNIssuerId = tbPurchaseOrdersForNIssuerId;
        this.tbPurchaseRequestsForNIssuerId = tbPurchaseRequestsForNIssuerId;
        this.msUsers = msUsers;
        this.tbPurchaseRequestsForNApproverId = tbPurchaseRequestsForNApproverId;
    }

   
    // Property accessors

    public Integer getNStaffId() {
        return this.NStaffId;
    }
    
    public void setNStaffId(Integer NStaffId) {
        this.NStaffId = NStaffId;
    }

    public MsCoa getMsCoa() {
        return this.msCoa;
    }
    
    public void setMsCoa(MsCoa msCoa) {
        this.msCoa = msCoa;
    }

    public String getVStaffName() {
        return this.VStaffName;
    }
    
    public void setVStaffName(String VStaffName) {
        this.VStaffName = VStaffName;
    }

    public String getVStaffAddr() {
        return this.VStaffAddr;
    }
    
    public void setVStaffAddr(String VStaffAddr) {
        this.VStaffAddr = VStaffAddr;
    }

    public String getVStaffPhNo() {
        return this.VStaffPhNo;
    }
    
    public void setVStaffPhNo(String VStaffPhNo) {
        this.VStaffPhNo = VStaffPhNo;
    }

    public Date getDStaffHiredDate() {
        return this.DStaffHiredDate;
    }
    
    public void setDStaffHiredDate(Date DStaffHiredDate) {
        this.DStaffHiredDate = DStaffHiredDate;
    }

    public Date getDStaffFiredDate() {
        return this.DStaffFiredDate;
    }
    
    public void setDStaffFiredDate(Date DStaffFiredDate) {
        this.DStaffFiredDate = DStaffFiredDate;
    }

    public double getNStaffSalary() {
        return this.NStaffSalary;
    }
    
    public void setNStaffSalary(double NStaffSalary) {
        this.NStaffSalary = NStaffSalary;
    }

    public short getNStaffRole() {
        return this.NStaffRole;
    }
    
    public void setNStaffRole(short NStaffRole) {
        this.NStaffRole = NStaffRole;
    }

    public String getVStaffCode() {
        return this.VStaffCode;
    }
    
    public void setVStaffCode(String VStaffCode) {
        this.VStaffCode = VStaffCode;
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

    public String getVWhoCreate() {
        return this.VWhoCreate;
    }
    
    public void setVWhoCreate(String VWhoCreate) {
        this.VWhoCreate = VWhoCreate;
    }

    public Date getDWhnChange() {
        return this.DWhnChange;
    }
    
    public void setDWhnChange(Date DWhnChange) {
        this.DWhnChange = DWhnChange;
    }

    public Set getTbPurchaseOrdersForNApproverId() {
        return this.tbPurchaseOrdersForNApproverId;
    }
    
    public void setTbPurchaseOrdersForNApproverId(Set tbPurchaseOrdersForNApproverId) {
        this.tbPurchaseOrdersForNApproverId = tbPurchaseOrdersForNApproverId;
    }

    public Set getMsDoctors() {
        return this.msDoctors;
    }
    
    public void setMsDoctors(Set msDoctors) {
        this.msDoctors = msDoctors;
    }

    public Set getTbDeliveryOrdersForNApproverId() {
        return this.tbDeliveryOrdersForNApproverId;
    }
    
    public void setTbDeliveryOrdersForNApproverId(Set tbDeliveryOrdersForNApproverId) {
        this.tbDeliveryOrdersForNApproverId = tbDeliveryOrdersForNApproverId;
    }

    public Set getMsStaffInUnits() {
        return this.msStaffInUnits;
    }
    
    public void setMsStaffInUnits(Set msStaffInUnits) {
        this.msStaffInUnits = msStaffInUnits;
    }

    public Set getTbDeliveryOrdersForNIssuerId() {
        return this.tbDeliveryOrdersForNIssuerId;
    }
    
    public void setTbDeliveryOrdersForNIssuerId(Set tbDeliveryOrdersForNIssuerId) {
        this.tbDeliveryOrdersForNIssuerId = tbDeliveryOrdersForNIssuerId;
    }

    public Set getTbPurchaseOrdersForNIssuerId() {
        return this.tbPurchaseOrdersForNIssuerId;
    }
    
    public void setTbPurchaseOrdersForNIssuerId(Set tbPurchaseOrdersForNIssuerId) {
        this.tbPurchaseOrdersForNIssuerId = tbPurchaseOrdersForNIssuerId;
    }

    public Set getTbPurchaseRequestsForNIssuerId() {
        return this.tbPurchaseRequestsForNIssuerId;
    }
    
    public void setTbPurchaseRequestsForNIssuerId(Set tbPurchaseRequestsForNIssuerId) {
        this.tbPurchaseRequestsForNIssuerId = tbPurchaseRequestsForNIssuerId;
    }

    public Set getMsUsers() {
        return this.msUsers;
    }
    
    public void setMsUsers(Set msUsers) {
        this.msUsers = msUsers;
    }

    public Set getTbPurchaseRequestsForNApproverId() {
        return this.tbPurchaseRequestsForNApproverId;
    }
    
    public void setTbPurchaseRequestsForNApproverId(Set tbPurchaseRequestsForNApproverId) {
        this.tbPurchaseRequestsForNApproverId = tbPurchaseRequestsForNApproverId;
    }
   








}