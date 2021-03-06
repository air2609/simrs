package com.vone.medisafe.mapping.pojo.diagnose;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsCauseOfDeath generated by MyEclipse - Hibernate Tools
 */

public class MsCauseOfDeath  implements java.io.Serializable {


    // Fields    

     private Integer NCodId;
     private String VCodCode;
     private String VCodDesc;
     private String VWhoCreate;
     private Date DWhnCreate;
     private String VWhoChange;
     private Date DWhnChange;
     private Set tbIllnesses = new HashSet(0);
     private Set tbOperations = new HashSet(0);
     private Set tbDiagnoses = new HashSet(0);
     private Set tbViolents = new HashSet(0);
     private Set tbPregnancies = new HashSet(0);
     private Set tbStillbirths = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsCauseOfDeath() {
    }

	/** minimal constructor */
    public MsCauseOfDeath(Integer NCodId, String VCodCode, String VCodDesc, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange) {
        this.NCodId = NCodId;
        this.VCodCode = VCodCode;
        this.VCodDesc = VCodDesc;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
    }
    
    /** full constructor */
    public MsCauseOfDeath(Integer NCodId, String VCodCode, String VCodDesc, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set tbIllnesses, Set tbOperations, Set tbDiagnoses, Set tbViolents, Set tbPregnancies, Set tbStillbirths) {
        this.NCodId = NCodId;
        this.VCodCode = VCodCode;
        this.VCodDesc = VCodDesc;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.tbIllnesses = tbIllnesses;
        this.tbOperations = tbOperations;
        this.tbDiagnoses = tbDiagnoses;
        this.tbViolents = tbViolents;
        this.tbPregnancies = tbPregnancies;
        this.tbStillbirths = tbStillbirths;
    }

   
    // Property accessors

    public Integer getNCodId() {
        return this.NCodId;
    }
    
    public void setNCodId(Integer NCodId) {
        this.NCodId = NCodId;
    }

    public String getVCodCode() {
        return this.VCodCode;
    }
    
    public void setVCodCode(String VCodCode) {
        this.VCodCode = VCodCode;
    }

    public String getVCodDesc() {
        return this.VCodDesc;
    }
    
    public void setVCodDesc(String VCodDesc) {
        this.VCodDesc = VCodDesc;
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

    public Set getTbIllnesses() {
        return this.tbIllnesses;
    }
    
    public void setTbIllnesses(Set tbIllnesses) {
        this.tbIllnesses = tbIllnesses;
    }

    public Set getTbOperations() {
        return this.tbOperations;
    }
    
    public void setTbOperations(Set tbOperations) {
        this.tbOperations = tbOperations;
    }

    public Set getTbDiagnoses() {
        return this.tbDiagnoses;
    }
    
    public void setTbDiagnoses(Set tbDiagnoses) {
        this.tbDiagnoses = tbDiagnoses;
    }

    public Set getTbViolents() {
        return this.tbViolents;
    }
    
    public void setTbViolents(Set tbViolents) {
        this.tbViolents = tbViolents;
    }

    public Set getTbPregnancies() {
        return this.tbPregnancies;
    }
    
    public void setTbPregnancies(Set tbPregnancies) {
        this.tbPregnancies = tbPregnancies;
    }

    public Set getTbStillbirths() {
        return this.tbStillbirths;
    }
    
    public void setTbStillbirths(Set tbStillbirths) {
        this.tbStillbirths = tbStillbirths;
    }
   








}