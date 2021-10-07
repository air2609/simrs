package com.vone.medisafe.common.exception;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.vone.medisafe.common.constant.Constant;
import com.vone.medisafe.common.util.ErrorUtil;
import com.vone.medisafe.common.util.StringUtils;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: May 29, 2005
 * Time: 11:51:43 AM
 */
public class VONEAppException extends Exception{

	private String msgCode;
    private String fullMsgError;
    private String initMsg;
    private Session session;

    public VONEAppException() {
    	
    	super();    	
    	
    	saveToSession();
    }

    public VONEAppException(String msg){  	    	
    	super(msg);

    	this.msgCode = msg;
    	saveToSession();
    }
    
    public VONEAppException(String msg, String fullMsg){
    	super(msg);
    	
    	this.msgCode = msg;
    	this.fullMsgError = fullMsg;
    	saveToSession();
    }
    
    public VONEAppException(String msg, Exception e){
    	super(msg);
    	
    	this.msgCode = msg;
    	
    	
    	StackTraceElement[] trc = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<trc.length; i++)
			sb.append(trc[i]);
		
		this.fullMsgError = sb.toString();
		
		saveToSession();
    }
    
    private void saveToSession(){
    	Session session = Sessions.getCurrent();
    	
    	VONEAppException excOld = (VONEAppException)session.getAttribute(Constant.EXCEPTION);
    	
    	if (excOld == null){    		
    		
    		session.setAttribute(Constant.EXCEPTION, this);
    	}else{
    		
    		if (!StringUtils.hasValue(excOld.getMsgCode()))
    			excOld.setMsgCode(this.msgCode);
    		if (!StringUtils.hasValue(excOld.getFullMsgError()))
    			excOld.setFullMsgError(this.fullMsgError);
    		    		
    		session.setAttribute(Constant.EXCEPTION, excOld);
    	}
    }

	public String getFullMsgError() {
		return fullMsgError;
	}

	public void setFullMsgError(String fullMsgError) {
		this.fullMsgError = fullMsgError;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}    
}
