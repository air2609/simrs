package com.vone.medisafe.report;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanRekapBulanan extends BaseController{
	
	Listbox locationList;
	Listbox monthList;
	Intbox year;
	
	Window win;
	
	UserManager userService = ServiceLocator.getUserManager();
	
	public UserInfoBean getUserInfoBean(){
		
		return super.getUserInfoBean();
	}
	
	public void init (Component cmp) throws InterruptedException, VONEAppException{
		super.init(cmp);
	
		locationList = (Listbox)cmp.getFellow("locationList");
		monthList = (Listbox)cmp.getFellow("monthList");
		year = (Intbox)cmp.getFellow("year");
		
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
		monthList.focus();
		this.win = (Window)cmp;
		
	}
	
	public void createReport() throws VONEAppException{
		Map<String, String> parameters = new HashMap<String, String>(); 
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		String input = year.getValue()+"-"+monthList.getSelectedItem().getValue()+"%";
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from report.fungsi_rekap_bulanan ");
		sql.append("('"+input+"','"+unit.getVUnitCode()+"')");
		
		parameters.put("bulan", monthList.getSelectedItem().getLabel());
		parameters.put("tahun", year.getText());
		parameters.put("unitName", unit.getVUnitName());
		parameters.put("userName", getUserInfoBean().getStUserId());
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("rekap.transaksi.bulanan"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
		
	}

}
