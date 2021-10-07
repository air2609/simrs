package com.vone.medisafe.ui.component;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import com.vone.medisafe.general.authorization.UserInfoBean;

/**
 * Project : GHOST
 * Prepared by : JiM
 * Copyright : PT VisiONE System
 * <p/>
 * User: James Pang
 * Date: Jul 13, 2006
 * Time: 7:27:41 AM
 */
public class RootController extends Window {

    UserInfoBean uib = null;
    Session session = Sessions.getCurrent();

    public void onCreate(){

//        if (session.getAttribute(Constant.USER_INFO) != null &&
//                session.getAttribute(Constant.USER_INFO) instanceof UserInfoBean)
//            uib = (UserInfoBean)session.getAttribute(Constant.USER_INFO);
//
//        //creating Accordion
//        ArrayList alSubsystem = uib.getSubsystemName();
//
//        Tabbox tabbox = (Tabbox)Path.getComponent("/root/accordion/mainTab");                
//
//        Tabs tabs = new Tabs();
//        Tabpanels panels = new Tabpanels();
//        for (int i=alSubsystem.size(); --i>=0;){
//            tabs.appendChild(new Tab((String)alSubsystem.get(i)));
//            panels.appendChild(new Tabpanel());
//        }
//
//        tabbox.appendChild(tabs);
//        tabbox.appendChild(panels);
    }
    public void onOK(){
        session.invalidate();
    }
    public void onCancel(){}

}
