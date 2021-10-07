package com.vone.medisafe.ui.purchasing;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.NumberUtil;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.ui.base.BaseController;

public abstract class PurchaseController extends BaseController{

	private Listbox list = null;

	public Listbox getList() {
		return list;
	}

	public void setList(Listbox list) {
		this.list = list;
	}
	
	public void addItem(MsItem item)  throws VONEAppException {}
	
	public void addItemRequest(Object[] item)  throws VONEAppException{}
	
	/**
	 * 
	 * @param initPrice
	 * @param discount
	 * @param discountType
	 * @param tax
	 * @param taxType
	 * @param qtyLocal
	 * @param qtyGlobal
	 * @return
	 */
	public static double calculatePrice(double initPrice, double discount, String discountType,
			double tax, String taxType, int qtyLocal, int qtyGlobal){		
		
		System.out.println("init price : "+initPrice);
		System.out.println("discount : "+discount);
		System.out.println("discount type : "+discountType);
		System.out.println("tax : "+tax);
		System.out.println("tax type : "+taxType);
		System.out.println("qty local : "+qtyLocal);
		System.out.println("qty global : "+qtyGlobal);
		
		double result = initPrice;
		double disc = 0;
		double ppn = 0;
				
		if (discount > 0){
			if (MedisafeConstants.RP.equals(discountType)){				
				disc = (double)qtyLocal/(double)qtyGlobal * discount;
			}else{
				
				disc = initPrice * discount / 100;
			}
			
			result -= disc;
		}
		
		if (tax > 0){
			if (MedisafeConstants.RP.equals(taxType)){
				
				ppn = tax;
			}else{
				
				ppn = result * tax / 100;
				
			}
			
			result += ppn;			
		}
		System.out.println("discount total : "+disc);
		System.out.println("----------------");
		System.out.println("result : "+result);
		
		return result;
	}
}
