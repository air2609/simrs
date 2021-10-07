package com.vone.medisafe.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.vone.medisafe.common.services.MessagesService;

/**
 * 
 * @author James Pang
 * @version 1.0
 */
public class ZulConstraint {
	
	private static final int RESERVE = 0; 
	public static final int NO_EMPTY = 1;
	public static final int UPPER_CASE = 2;		
	public static final int TIME_CONSTRAINT = 3;
	
	private static final String YELLOW = "#FFFFFF";
	private static final String WHITE = "#FFFFFF";
	
	private String stMessage = "";
	
	List listCmpNoEmpty = new ArrayList();
	List listCmpUpperCase = new ArrayList();
	List listCmpTimeCst = new ArrayList();
	
	boolean isRaiseError = true;
	
	public ZulConstraint(){}
	
	public ZulConstraint(boolean raiseError){
		isRaiseError = raiseError;
	}
	
	/**
	 * register component without additional paramenter, will be set to "NO_EMPTY" by default
	 * @param cmp
	 * @deprecated
	 */
	public void registerComponent(Component cmp){			
		listCmpNoEmpty.add(cmp);
	}	
	
	public void registerComponent(Component cmp, int constraintType){
		
		switch (constraintType){
			case NO_EMPTY : 
				listCmpNoEmpty.add(cmp);
				break;
			case UPPER_CASE :
				listCmpUpperCase.add(cmp);
				executeToUpperValidation();
				break;
			case TIME_CONSTRAINT :
				listCmpTimeCst.add(cmp);
				break;				
			default :
				break;
		}
	}
	
	public boolean isRaiseError() {
		return isRaiseError;
	}

	public void setRaiseError(boolean isRaiseError) {
		this.isRaiseError = isRaiseError;
	}

	public boolean validateComponent(boolean isRaiseError) throws InterruptedException {
		this.isRaiseError = isRaiseError;
		
		return validateComponent();
	}
	
	/**	 
	 * @return List of Constraint-error Component
	 */
	public boolean validateComponent() throws InterruptedException {
		
		boolean result;
		
		if (isRaiseError){
			
			result = executeValidation(false, null);
			
			if (!result)
				Messagebox.show(MessagesService.getKey(stMessage));
		}
		
		return executeValidation(true, null);
		
	}	
	
	/**
	 * hybrid method to return constraint result or execute skin modification of component 
	 * executeModification is true if user want to change the skin
	 * executeModification is false if user just want to know the result (constraint error or not) 
	 * @param executeModification
	 * @return
	 */
	private boolean executeValidation(boolean executeModification, String modbackgroundColor){
		
		boolean result = true;
		boolean firstItem = true;
		Iterator it = null;
		
		String backgroundColor = "";
		
		if (!StringUtils.hasValue(modbackgroundColor))
			backgroundColor = this.YELLOW;
		else
			backgroundColor = modbackgroundColor;
		
		//executing NO_EMPTY Validation						
		it = listCmpNoEmpty.iterator();		
		stMessage = "common.constraint.noempty";		
		
		while(it.hasNext()){					
			
			Component curComp = (Component)it.next();			

			if (curComp instanceof Textbox){
				Textbox textBox = (Textbox)curComp;				
				
				if (!StringUtils.hasValue(textBox.getText())){
					
					if (executeModification){
						
						textBox.setStyle(textBox.getStyle()+";background-color: "+backgroundColor);
						
						if (firstItem) {textBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;					
				}
			}else
			if (curComp instanceof Bandbox){
				Bandbox bandBox = (Bandbox)curComp;

				if (!StringUtils.hasValue(bandBox.getText())){
					
					if (executeModification){

						bandBox.setStyle(bandBox.getStyle()+";background-color: "+backgroundColor);
						
						if (firstItem) {bandBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;
				}
			}else
			if (curComp instanceof Datebox){
				Datebox dateBox = (Datebox)curComp;

				if (!StringUtils.hasValue(dateBox.getText())){
					if (executeModification){
						dateBox.setStyle(dateBox.getStyle()+";background-color: "+backgroundColor);			
						if (firstItem) {dateBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;
				}
			}else
			if (curComp instanceof Listbox){
				Listbox listBox = (Listbox)curComp;				
				
				if (listBox.getSelectedItem() == null ||
						MedisafeConstants.LISTKOSONG.equals(((Listitem)listBox.getSelectedItem()).getValue()) ){
					if (executeModification){
						
						listBox.setStyle(listBox.getStyle()+";background-color: "+backgroundColor);	
						if (firstItem) {listBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;
				}
				
				
			}else
			if (curComp instanceof Decimalbox){
				Decimalbox decimalBox = (Decimalbox)curComp;
				
				if (decimalBox.getValue() == null){
					if (executeModification){
						decimalBox.setStyle(decimalBox.getStyle()+";background-color: "+backgroundColor);				
						if (firstItem) {decimalBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;
				}
			}else
			if (curComp instanceof Intbox){
				Intbox intBox = (Intbox)curComp;
				
				if (intBox.getValue() == null){
					if (executeModification){
						intBox.setStyle(intBox.getStyle()+";background-color: "+backgroundColor);				
						if (firstItem) {intBox.focus(); firstItem = false;}
						
						result = false;
					}else
						return false;
				}
			}else		
				if (curComp instanceof Radiogroup){
					Radiogroup rGroup = (Radiogroup)curComp;
					
					if (rGroup.getSelectedItem() == null){
						if (executeModification){
							rGroup.setStyle(rGroup.getStyle()+";background-color: "+backgroundColor);				
							if (firstItem) {rGroup.focus(); firstItem = false;}
							
							result = false;
						}else
							return false;
					}
				}
		}				
		
		//executing TIME CONSTRAINT Validation						
		it = listCmpTimeCst.iterator();		
		
		stMessage = "common.constraint.time";
		
		while(it.hasNext()){					
			
			Component curComp = (Component)it.next();			

			if (curComp instanceof Textbox){
				Textbox textBox = (Textbox)curComp;				
				
				if (!timeValidate(textBox)){
					
					if (executeModification){
						
						textBox.setStyle(textBox.getStyle()+";background-color: "+backgroundColor);
						
						if (firstItem) {textBox.select(); firstItem = false;}
						
						result = false;
					}else
						return false;					
				}
			}
		}
		
		return result;
	}
	
	/**
	 * registering toUpper Validation
	 *
	 */
	private void executeToUpperValidation (){
		//executing UPPER_CASE Validation
		Iterator it = listCmpUpperCase.iterator();
		
		while (it.hasNext()){
			Component cmp = (Component)it.next();
			
			if (cmp instanceof Textbox){
				final Textbox textBox = (Textbox)cmp;
				
				textBox.addEventListener("onChange", new EventListener(){

					public boolean isAsap() {
						// TODO Auto-generated method stub
						return true;
					}

					public void onEvent(Event evt) {
						// TODO Auto-generated method stub						
						textBox.setText(textBox.getText().toUpperCase());
						
					}
					
				});				
			}
		}		
	}	
	
	/**
	 * time constraint validation logic
	 * @param timebox
	 * @return
	 */
	private boolean timeValidate(Textbox timebox){
		if (timebox == null || "".equals(timebox.getText()))
			return false;
		
		Integer iHour = null;
		Integer iMinute = null;
		String hour = null;
		String minute = null;
		
		try{
			String txt = timebox.getText();
			
			if (txt.length() > 5)
				return false;		
			
			if (txt.indexOf(':') == -1)
				return false;
			
			if (txt.indexOf(':') == 2){
				hour = txt.substring(0, 2);
				minute = txt.substring(3, 5);
			}else{
				hour = txt.substring(0, 1);
				minute = txt.substring(2, 4);				
			}
				
			iHour = new Integer(hour);
			iMinute = new Integer(minute);				
		}catch(Exception e){
			return false;
		}
		
		if (iHour.intValue() > 23 || iHour.intValue() < 0)
			return false;
		if (iMinute.intValue() > 59 || iMinute.intValue() < 0)
			return false;		
		
		return true;
	}	
	
	/**
	 * unreg component
	 * execute validateComponent(false) after execute this method to refresh the screen.
	 * @param cmp
	 * @return
	 */
	public boolean unregisterComponent(Component cmp, int cstType){
		
		if (cstType == this.NO_EMPTY){
			if (!listCmpNoEmpty.contains(cmp))
				return false;
			
			executeValidation(true, this.WHITE);
			listCmpNoEmpty.remove(cmp);			
		}else
		if (cstType == this.UPPER_CASE){
			if (!listCmpUpperCase.contains(cmp))
				return false;
			
			executeValidation(true, this.WHITE);
			listCmpUpperCase.remove(cmp);
		}
		
		return true;
	}	
}
