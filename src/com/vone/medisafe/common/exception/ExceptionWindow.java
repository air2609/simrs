package com.vone.medisafe.common.exception;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.constant.Constant;

public class ExceptionWindow extends Window implements AfterCompose{

	public void afterCompose() {
		// TODO Auto-generated method stub		
		Object obj = Sessions.getCurrent().getAttribute(Constant.EXCEPTION);
		if (obj == null){
			System.out.println("NULL!!");
			
			return;
			
		}				
		
		VONEAppException exc = (VONEAppException)obj;
		
		Label lbl = (Label)this.getFellow("lbl");
		lbl.setValue(exc.getMsgCode());
		
		Textbox detail = (Textbox)this.getFellow("detail");
		detail.setValue(exc.getMessage() + " - "+exc.getFullMsgError()+" - "+detail.getValue());
				
		//clear up exception
		Sessions.getCurrent().removeAttribute(Constant.EXCEPTION);
	}

}
