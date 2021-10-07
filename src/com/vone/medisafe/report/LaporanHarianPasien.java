package com.vone.medisafe.report;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class LaporanHarianPasien extends BaseController{
	
	Listbox locationList;
	Datebox startDate;
	Datebox endDate;
	Bandbox noMR;
	Textbox crNoMR;
	Textbox crNama;
	Textbox crNoAlamat;
	Listbox patientSearchList;
	Textbox namaPasien;
	Textbox bed;
	
	Window win;
	
	UserManager userService = ServiceLocator.getUserManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		
		super.init(cmp);
		
		locationList = (Listbox)cmp.getFellow("locationList");
		startDate = (Datebox)cmp.getFellow("startDate");
		endDate = (Datebox)cmp.getFellow("endDate");
		noMR = (Bandbox)cmp.getFellow("noMR");
		crNoMR = (Textbox)cmp.getFellow("crNoMR");
		crNama = (Textbox)cmp.getFellow("crNama");
		crNoAlamat = (Textbox)cmp.getFellow("crNoAlamat");
		patientSearchList = (Listbox)cmp.getFellow("patientSearchList");
		namaPasien = (Textbox)cmp.getFellow("namaPasien");
		bed = (Textbox)cmp.getFellow("bed");
		
		win = (Window)cmp;
		Date tanggal = new Date();
		
		userService.getUnitUser(getUserInfoBean().getMsUser(), locationList);
		startDate.setValue(tanggal);
		endDate.setValue(tanggal);
		
	}
	
	public void createRepport() throws VONEAppException{
		
		
		Map<String, String> parameters = new HashMap<String, String>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		
		MsUnit unit = (MsUnit)locationList.getSelectedItem().getValue();
		
		String mulai = sdf.format(startDate.getValue());
		mulai = mulai + " 00:00:00";
		
		String sampai = sdf.format(endDate.getValue());
		sampai = sampai + " 23:59:59";
		
		Integer regId = (Integer)noMR.getAttribute("regId");
		String regNo = noMR.getAttribute("regNo").toString();
		String ruangan = bed.getAttribute("ruangan").toString();
		String tclass = bed.getAttribute("kelas").toString();
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * ");
		sql.append(" from ");
		sql.append(" report.fungsi_rekap_pasien_bangsal");
		sql.append("('"+regId.intValue()+"','"+mulai+"','"+sampai+"','%"+unit.getVUnitCode()+"%') order by nomor");
		
		
		
		parameters.put("startDate", sdf2.format(startDate.getValue()));
		parameters.put("endDate", sdf2.format(endDate.getValue()));
		parameters.put("name", namaPasien.getText());
		parameters.put("mr_no", noMR.getText());
		parameters.put("reg_no", regNo);
		parameters.put("tclass", tclass);
		parameters.put("hall", ruangan);
		parameters.put("bed", bed.getText());
//		parameters.put("namaBangsal", unit.getVUnitName());
//		parameters.put("userName", getUserInfoBean().getMsUser().getVUserName());
		
		
		
		ReportEngine re = new ReportEngine(sql.toString(), 
				ReportService.getKey("laporan.harian.pasien.bangsal"), parameters);
		if(!re.printOut(win.getDesktop().getSession().getClientAddr()))
			re.openPdf();
			
	}

}
