package com.vone.medisafe.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanPoliUgd extends BaseController{
	
	Listbox locationList;
	Listbox shiftList;
	Datebox startDate;
	Datebox endDate;
	
	Window win;
	
	UserManager userService = ServiceLocator.getUserManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.init(cmp);
		locationList = (Listbox)cmp.getFellow("locationList");
		shiftList = (Listbox)cmp.getFellow("shiftList");
		startDate = (Datebox)cmp.getFellow("startDate");
		endDate = (Datebox)cmp.getFellow("endDate");
		
		startDate.setValue(new Date());
		endDate.setValue(new Date());
		
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
		
		this.win = (Window)cmp;
	}
	
	
	public void createRepport() throws VONEAppException{
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		String kodeRajal = "%"+unit.getVUnitCode()+"%";
		String shift = shiftList.getSelectedItem().getValue().toString();
		
		String shiftName = shiftList.getSelectedItem().getLabel();
		
		
		
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from ");
		sql.append(" report.laporan_harian_poly_ugd");
		sql.append("('"+mulai+"','"+sampai+"','"+kodeRajal+"','"+shift+"')");
		
		
		
		parameters.put("startDate", sdf2.format(startDate.getValue()));
		parameters.put("endDate", sdf2.format(endDate.getValue()));
		parameters.put("unitName", unit.getVUnitName());
		parameters.put("shiftName", shiftName);
		parameters.put("userName", getUserInfoBean().getStUserId());
		
		
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("laporan.poli.ugd"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
			
	}

}
