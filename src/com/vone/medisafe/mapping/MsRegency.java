package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsRegency generated by MyEclipse - Hibernate Tools
 */

public class MsRegency  implements java.io.Serializable {


    // Fields    

     private Integer NRegencyId;
     private String VRegencyId;
     private String VRegencyName;
     private MsProvince province;
     private String VWhoCreate;
     private Date DWhnCreate; 
     private String VWhoChange;
     private Date DWhnChange;
     private Set msPatients = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsRegency() {
    }

	/** minimal constructor */
    public MsRegency(Integer NRegencyId, String VRegencyId, String VRegencyName) {
        this.NRegencyId = NRegencyId;
        this.VRegencyId = VRegencyId;
        this.VRegencyName = VRegencyName;
    }
    
    /** full constructor */
    public MsRegency(Integer NRegencyId, String VRegencyId, String VRegencyName, String VWhoCreate, Date DWhnCreate, String VWhoChange, Date DWhnChange, Set msPatients) {
        this.NRegencyId = NRegencyId;
        this.VRegencyId = VRegencyId;
        this.VRegencyName = VRegencyName;
        this.VWhoCreate = VWhoCreate;
        this.DWhnCreate = DWhnCreate;
        this.VWhoChange = VWhoChange;
        this.DWhnChange = DWhnChange;
        this.msPatients = msPatients;
    }

    
   
    // Property accessors

    public MsProvince getProvince() {
		return province;
	}

	public void setProvince(MsProvince province) {
		this.province = province;
	}

	public Integer getNRegencyId() {
        return this.NRegencyId;
    }
    
    public void setNRegencyId(Integer NRegencyId) {
        this.NRegencyId = NRegencyId;
    }

    public String getVRegencyId() {
        return this.VRegencyId;
    }
    
    public void setVRegencyId(String VRegencyId) {
        this.VRegencyId = VRegencyId;
    }

    public String getVRegencyName() {
        return this.VRegencyName;
    }
    
    public void setVRegencyName(String VRegencyName) {
        this.VRegencyName = VRegencyName;
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

    public Set getMsPatients() {
        return this.msPatients;
    }
    
    public void setMsPatients(Set msPatients) {
        this.msPatients = msPatients;
    }
   








}