package com.vone.medisafe.ui.component;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.util.ZKPathReader;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.service.iface.admin.ScreenManager;
import com.vone.medisafe.service.iface.admin.SubsystemManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.component.util.ScreenImageURL;

public class AccordionController{

    static UserInfoBean uib = null;
    static Session session = null;	    
    static SubsystemManager subsystemManager = (SubsystemManager)ServiceLocator.getSubsystemManager();
    static ScreenManager screenManager = (ScreenManager)ServiceLocator.getScreenManager();
    
    
    
	public static void generateAccordion(Window win) throws Exception{
		
		session = Sessions.getCurrent();				
		
        if (session.getAttribute(Constant.USER_INFO) != null &&
                session.getAttribute(Constant.USER_INFO) instanceof UserInfoBean){
            uib = (UserInfoBean)session.getAttribute(Constant.USER_INFO);
        }
        else
        	session.invalidate();   
        
        Tabbox tabbox = (Tabbox)win.getFellow("mainTab");
        
        if (uib == null || uib.getListModule() == null) return; 
        
        List alSubsystem = uib.getListModule();

        Tabs tabs = new Tabs();
        Tabpanels panels = new Tabpanels();           
        
        if (alSubsystem != null)
	        for (int i=0; i<alSubsystem.size(); i++){	        	
	        	
	        	String[] stSbsNameDesc = subsystemManager.getSubsystemNameDesc((Integer)alSubsystem.get(i));
	        	
	        	
	        	
	        	Tab tab = new Tab(stSbsNameDesc[1], ScreenImageURL.getInstance().getURL(stSbsNameDesc[0]));
	        	tab.setWidth("150px");
	        	tabs.appendChild(tab);
	        	        	
	        	Tabpanel panel = new Tabpanel();	        		        	
	        	
	        	panel.appendChild(createAccordionItems(uib.getListScreenPerModule(((Integer)alSubsystem.get(i)).intValue()),
	        			win));	        	
	        	
//	        	panel.setHeight("150px");
	        	panel.setStyle("overflow:auto");
	        	panels.appendChild(panel);
	        }
        
        Tab manTab = new Tab("UTILITIES","../image/lock.gif");
        manTab.setWidth("150px");
//        manTab.setHeight("10px");
    	tabs.appendChild(manTab);
    	
    	Tabpanel manPanel = new Tabpanel();
    	manPanel.appendChild(getAutomaticMenu(win));
    	manPanel.setStyle("overflow:auto");
    	panels.appendChild(manPanel);

        tabbox.appendChild(tabs);
        tabbox.appendChild(panels);
        
	}           
    
    private static Vbox getAutomaticMenu(final Window win) {
    	Vbox vbox = new Vbox();
    	Vbox vboxInner = new Vbox();
    	Image img = new Image();
    	img.setSrc("/image/dojo/icon_users.png");
    	img.setHeight("40px");
		img.setWidth("40px");
		img.setStyle("cursor:hand");
		
		Label label = new Label("Change Password");
		label.setSclass("labelAccordion");
		label.setStyle("cursor:hand");
		
		vboxInner.appendChild(img);
		vboxInner.appendChild(label);
		img.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event arg0) throws Exception {
				Include inc = (Include)win.getDesktop().getPage("main").getFellow("root").getFellow("contents").getFellow("xcontent");						
				inc.setSrc("/zkpages/admin/user/changePassword.zul");
			}
		});
		
		label.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event arg0) throws Exception {
				Include inc = (Include)win.getDesktop().getPage("main").getFellow("root").getFellow("contents").getFellow("xcontent");						
				inc.setSrc("/zkpages/admin/user/changePassword.zul");
			}
		});
		
		vbox.appendChild(vboxInner);
		
		
		vboxInner = new Vbox();
    	img = new Image();
    	img.setSrc("/image/dojo/icon_logout.png");
    	img.setHeight("40px");
		img.setWidth("40px");
		img.setStyle("cursor:hand");
		
		label = new Label("Logout");
		label.setSclass("labelAccordion");
		label.setStyle("cursor:hand");
		
		vboxInner.appendChild(img);
		vboxInner.appendChild(label);
		img.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event arg0) throws Exception {
				Session session = Sessions.getCurrent();
				session.invalidate();
				
				Executions.sendRedirect("login.zul");
			}
		});
		
		label.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event arg0) throws Exception {
				Session session = Sessions.getCurrent();
				session.invalidate();
				
				Executions.sendRedirect("login.zul");
			}
		});
		
		vbox.appendChild(vboxInner);
		
		return vbox;
	}

	public static Vbox createAccordionItems(List listScreen, final Window win) throws Exception{    	
    	
    	Vbox vbox = new Vbox();
//    	vbox.setSpacing("2");
    	
    	if (listScreen != null)
    	for (int i=0; i<listScreen.size(); i++){
    		
    		final String stScreenCodeName[] = screenManager.getScreenCodeName( ((Integer)listScreen.get(i)).intValue() );    		
    		
    		Vbox vboxInner = new Vbox();
    		
    		Image img = new Image();
    		
    		img.setSrc(ScreenImageURL.getInstance().getURL(stScreenCodeName[0]));
    		
    		img.setHeight("40px");
    		img.setWidth("40px");
    		img.setStyle("cursor:hand");
    		
    		Label label = new Label(stScreenCodeName[1]);
    		label.setSclass("labelAccordion");
    		label.setStyle("cursor:hand");
    		
//    		img.setAction("onmouseover:alert('test')");
//    		label.setAction("onmouseover:alert('test')");
    		
    		vboxInner.appendChild(img);
    		vboxInner.appendChild(label);
    			    		
	    	img.addEventListener("onClick", new EventListener(){
	    		
				public boolean isAsap() {
					// TODO Auto-generated method stub
					return true;
				}
	
				public void onEvent(Event arg0) {
					// TODO Auto-generated method stub
					
					try {

				    	Include inc = (Include)win.getDesktop().getPage("main").getFellow("root").getFellow("contents").getFellow("xcontent");						

						inc.setSrc(ZKPathReader.getInstance().getUrl(stScreenCodeName[0]));
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	    		
	    	});	    	
	    	
	    	label.addEventListener("onClick", new EventListener(){
	    		
				public boolean isAsap() {
					// TODO Auto-generated method stub
					return true;
				}
	
				public void onEvent(Event arg0) {
					// TODO Auto-generated method stub
					
					try {

				    	Include inc = (Include)win.getDesktop().getPage("main").getFellow("root").getFellow("contents").getFellow("xcontent");						
				    	
						inc.setSrc(ZKPathReader.getInstance().getUrl(stScreenCodeName[0]));
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	    		
	    	});	    	
	    	
	    	vbox.appendChild(vboxInner);
    	}
    	
    	
		
    	
    	return vbox;
    }    
}
