package com.vone.medisafe.mapping;



/**
 * TbGroupPrivilegeId generated by MyEclipse - Hibernate Tools
 */

public class TbGroupPrivilegeId  implements java.io.Serializable {


    // Fields    

     private MsGroup msGroup;
     private MsScreen msScreen;


    // Constructors

    /** default constructor */
    public TbGroupPrivilegeId() {
    }

    
    /** full constructor */
    public TbGroupPrivilegeId(MsGroup msGroup, MsScreen msScreen) {
        this.msGroup = msGroup;
        this.msScreen = msScreen;
    }

   
    // Property accessors

    public MsGroup getMsGroup() {
        return this.msGroup;
    }
    
    public void setMsGroup(MsGroup msGroup) {
        this.msGroup = msGroup;
    }

    public MsScreen getMsScreen() {
        return this.msScreen;
    }
    
    public void setMsScreen(MsScreen msScreen) {
        this.msScreen = msScreen;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TbGroupPrivilegeId) ) return false;
		 TbGroupPrivilegeId castOther = ( TbGroupPrivilegeId ) other; 
         
		 return ( (this.getMsGroup()==castOther.getMsGroup()) || ( this.getMsGroup()!=null && castOther.getMsGroup()!=null && this.getMsGroup().equals(castOther.getMsGroup()) ) )
 && ( (this.getMsScreen()==castOther.getMsScreen()) || ( this.getMsScreen()!=null && castOther.getMsScreen()!=null && this.getMsScreen().equals(castOther.getMsScreen()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getMsGroup() == null ? 0 : this.getMsGroup().hashCode() );
         result = 37 * result + ( getMsScreen() == null ? 0 : this.getMsScreen().hashCode() );
         return result;
   }   





}