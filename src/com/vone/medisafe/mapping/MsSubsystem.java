package com.vone.medisafe.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * MsSubsystem generated by MyEclipse - Hibernate Tools
 */

public class MsSubsystem  implements java.io.Serializable {


    // Fields    

     private Integer NSubsystemId;
     private String VSubsystemCode;
     private String VDesc;
     private String VWhocreate;
     private Date DWhncreate;
     private String VWhochange;
     private Date DWhnchange;
     private Set msScreens = new HashSet(0);


    // Constructors

    /** default constructor */
    public MsSubsystem() {
    }

	/** minimal constructor */
    public MsSubsystem(Integer NSubsystemId) {
        this.NSubsystemId = NSubsystemId;
    }
    
    /** full constructor */
    public MsSubsystem(Integer NSubsystemId, String VSubsystemCode, String VDesc, String VWhocreate, Date DWhncreate, String VWhochange, Date DWhnchange, Set msScreens) {
        this.NSubsystemId = NSubsystemId;
        this.VSubsystemCode = VSubsystemCode;
        this.VDesc = VDesc;
        this.VWhocreate = VWhocreate;
        this.DWhncreate = DWhncreate;
        this.VWhochange = VWhochange;
        this.DWhnchange = DWhnchange;
        this.msScreens = msScreens;
    }

   
    // Property accessors

    public Integer getNSubsystemId() {
        return this.NSubsystemId;
    }
    
    public void setNSubsystemId(Integer NSubsystemId) {
        this.NSubsystemId = NSubsystemId;
    }

    public String getVSubsystemCode() {
        return this.VSubsystemCode;
    }
    
    public void setVSubsystemCode(String VSubsystemCode) {
        this.VSubsystemCode = VSubsystemCode;
    }

    public String getVDesc() {
        return this.VDesc;
    }
    
    public void setVDesc(String VDesc) {
        this.VDesc = VDesc;
    }

    public String getVWhocreate() {
        return this.VWhocreate;
    }
    
    public void setVWhocreate(String VWhocreate) {
        this.VWhocreate = VWhocreate;
    }

    public Date getDWhncreate() {
        return this.DWhncreate;
    }
    
    public void setDWhncreate(Date DWhncreate) {
        this.DWhncreate = DWhncreate;
    }

    public String getVWhochange() {
        return this.VWhochange;
    }
    
    public void setVWhochange(String VWhochange) {
        this.VWhochange = VWhochange;
    }

    public Date getDWhnchange() {
        return this.DWhnchange;
    }
    
    public void setDWhnchange(Date DWhnchange) {
        this.DWhnchange = DWhnchange;
    }

    public Set getMsScreens() {
        return this.msScreens;
    }
    
    public void setMsScreens(Set msScreens) {
        this.msScreens = msScreens;
    }
   








}