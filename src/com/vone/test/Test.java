package com.vone.test;

import java.util.List;

import org.zkforge.dojo.Fisheyeitem;
import org.zkforge.dojo.Fisheyelist;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.exception.ScreenImageURLException;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.ZKPathReader;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.service.iface.admin.SubsystemManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.component.util.ScreenImageURL;

public class Test {
	
	Fisheyelist listMenu;
	Fisheyeitem itemMenu;
	Include inc;
	String pathMenu;
	List listScreen;
	
	static UserInfoBean uib = null;
    static Session session = null;	    
    static SubsystemManager subsystemManager = (SubsystemManager)ServiceLocator.getSubsystemManager();
    static ScreenManager screenManager = (ScreenManager)ServiceLocator.getScreenManager();
	
	public void initTest(Window win) throws VONEAppException, ScreenImageURLException, InterruptedException{
		
		listMenu =(Fisheyelist)win.getFellow("fi");
		listMenu.getChildren().clear();
		
		Textbox text = new Textbox();
		
		
		inc = (Include)win.getDesktop().getPage("main").getFellow("root").getFellow("contents").getFellow("xcontent");
		
		session = Sessions.getCurrent();				
		
        if (session.getAttribute(Constant.USER_INFO) != null &&
                session.getAttribute(Constant.USER_INFO) instanceof UserInfoBean){
            uib = (UserInfoBean)session.getAttribute(Constant.USER_INFO);
        }
        else
        	session.invalidate();
        
        if (uib == null || uib.getListModule() == null) return; 
        
        List alSubsystem = uib.getListModule();
        
        if (alSubsystem != null)
	        for (int i=0; i<alSubsystem.size(); i++){	        	
	        	
//	        	String[] stSbsNameDesc = subsystemManager.getSubsystemNameDesc((Integer)alSubsystem.get(i));
	        	listScreen = uib.getListScreenPerModule(((Integer)alSubsystem.get(i)).intValue());
	        	
	        	if (listScreen != null)
	        	for (int j=0; j<listScreen.size(); j++){
	        		final String stScreenCodeName[] = screenManager.getScreenCodeName( ((Integer)listScreen.get(j)).intValue() );    		
	        		
	        		itemMenu = new Fisheyeitem();
	        		itemMenu.setImage(ScreenImageURL.getInstance().getURL(stScreenCodeName[0]));
	        		itemMenu.setLabel(stScreenCodeName[1]);
	        		pathMenu =ZKPathReader.getInstance().getUrl(stScreenCodeName[0]);
	        		itemMenu.addEventListener("onClick", new MenuListener(pathMenu,inc));
	        		itemMenu.setParent(listMenu);
	        		
//	        		Separator sp = new Separator();
//	        		sp.set
	        		
	        	}	
	        }
        
        itemMenu = new Fisheyeitem();
		itemMenu.setImage("/image/changepassword.png");
		itemMenu.setLabel("Change Password");
//		pathMenu ="/zkpages/admin/user/changePassword.zul";
		itemMenu.addEventListener("onClick", new CPListener());
		itemMenu.setParent(listMenu);
        
        itemMenu = new Fisheyeitem();
		itemMenu.setImage("/image/dojo/icon_logout.png");
		itemMenu.setLabel("Log Out");
		itemMenu.addEventListener("onClick", new LogoutListner());
		itemMenu.setParent(listMenu);
	}
}
