package com.vone.medisafe.report;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.cashier.CashierManager;
import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanPenerimaanKasir extends BaseController{
	
	Datebox transDate;
	Listbox shiftList;
	Listbox kwitansiList;
	Decimalbox tunai;
	Decimalbox nonTunai;
	
	CashierManager manager = Service.getCashierManager();
	ZulConstraint constraints = new ZulConstraint();
	
	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		
		super.init(cmp);
		
		this.transDate = (Datebox) cmp.getFellow("transDate");
		this.shiftList = (Listbox)cmp.getFellow("shiftList");
		this.kwitansiList = (Listbox)cmp.getFellow("kwitansiList"); 
		this.tunai = (Decimalbox)cmp.getFellow("tunai");
		this.nonTunai = (Decimalbox)cmp.getFellow("nontunai");
		this.tunai.setFormat("#,###.##");
		this.nonTunai.setFormat("#,###.##");
		
		constraints.registerComponent(transDate, ZulConstraint.NO_EMPTY);
	}
	
	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}
	
	public void createReport() throws WrongValueException, InterruptedException{
		if(transDate.getValue() != null)
			manager.getPatientSettlement(transDate, shiftList, kwitansiList, tunai, nonTunai);
		else{
			Messagebox.show("Isi tanggal terlebih dahulu");
		}
		
	}
}
