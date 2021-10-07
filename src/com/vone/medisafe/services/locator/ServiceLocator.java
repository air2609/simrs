package com.vone.medisafe.services.locator;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;


import com.vone.medisafe.service.iface.admin.BranchManager;
import com.vone.medisafe.service.iface.admin.GroupManager;
import com.vone.medisafe.service.iface.admin.GroupPrivilegeManager;
import com.vone.medisafe.service.iface.admin.ScreenInUnitManager;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.service.iface.admin.SubsystemManager;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.service.iface.admin.UserPrivilegeManager;

public class ServiceLocator {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public static SessionFactory getSessionFactory()
	{
			return (SessionFactory)ctx.getBean("SessionFactory",SessionFactory.class);
	}

	public static PlatformTransactionManager getTrxManager(){
		return (PlatformTransactionManager)ctx.getBean("txManager", PlatformTransactionManager.class);
	}

	
	public static UserManager getUserManager()
	{
		return (UserManager)ctx.getBean("UserManager",UserManager.class);
	}
	
	public static GroupManager getGroupManager()
	{
		return (GroupManager) ctx.getBean("GroupManager", GroupManager.class);
	}
	
	public static ScreenManager getScreenManager()
	{
		return (ScreenManager) ctx.getBean("ScreenManager", ScreenManager.class);
	}	
	
	public static SubsystemManager getSubsystemManager()
	{
		return (SubsystemManager) ctx.getBean("SubsystemManager", SubsystemManager.class);
	}		
	
	public static UserPrivilegeManager getUserPrivilegeManager()
	{
		return (UserPrivilegeManager) ctx.getBean("UserPrivilegeManager", UserPrivilegeManager.class);
	}
	
	public static GroupPrivilegeManager getGroupPrivilegeManager()
	{
		return (GroupPrivilegeManager) ctx.getBean("GroupPrivilegeManager", GroupPrivilegeManager.class);
	}
	
	public static BranchManager getBranchManager()
	{
		return (BranchManager) ctx.getBean("BranchManager", BranchManager.class);
	}
	
	public static ScreenInUnitManager getScreenInUnitManager(){
		return (ScreenInUnitManager) ctx.getBean("ScreenInUnitManager",ScreenInUnitManager.class);
	}
}
