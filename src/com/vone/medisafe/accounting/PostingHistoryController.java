package com.vone.medisafe.accounting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbHistoryPosting;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.ui.base.BaseController;

public class PostingHistoryController extends BaseController{
	
	private static final String REPORT_PL = "PL-";

	private static final String REPORT_GL = "GL-";

	private static final String REPORT_BS = "BS-";

	public Session session;
	
	public JournalManager journalManager = Service.getJournalManager();
	
	public Listbox postList;

	public MsUser user; 
	
	ReportEngine re;
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		postList = (Listbox)win.getFellow("postList"); 
		session = win.getDesktop().getSession(); 

		user = getUserInfoBean().getMsUser();
		
		//load semua history posting ke postList
		journalManager.getAllPostingHistory(postList);
		
		
	}
	
	public void openLabarugi() throws VONEAppException{
		Listitem pilih = postList.getSelectedItem();
		if(pilih == null){
			return;
		}
		
		TbHistoryPosting tbHistoryPosting = (TbHistoryPosting) pilih.getValue();
		String reportName = tbHistoryPosting.getVReportName();
		re = new ReportEngine();
		re.openPdf(REPORT_PL + reportName);
		

	}
	

	public void openGl() throws VONEAppException{
		Listitem pilih = postList.getSelectedItem();
		if(pilih == null){
			return;
		}
		TbHistoryPosting tbHistoryPosting = (TbHistoryPosting) pilih.getValue();
		String reportName = tbHistoryPosting.getVReportName();
		re = new ReportEngine();
		re.openPdf(REPORT_GL + reportName);
	}
	
	public void openNeraca() throws VONEAppException{
		// todo Auto-generated method stub
		
		//code ini utk membuka pdf (history neraca)
		Listitem pilih = postList.getSelectedItem();
		if(pilih == null){
			return;
		}
		TbHistoryPosting tbHistoryPosting = (TbHistoryPosting) pilih.getValue();
		String reportName = tbHistoryPosting.getVReportName();
		re = new ReportEngine();
		re.openPdf(REPORT_BS + reportName);
		
	}
	public void doPosting() throws VONEAppException, InterruptedException{
		final Window win = (Window) Executions.createComponents(
				"/zkpages/report/posting.zul", null, null);
		Textbox usernameTextbox = (Textbox) win.getFellow("username"); 
		Textbox passwordTextbox = (Textbox) win.getFellow("password"); 
		Textbox descTextbox = (Textbox) win.getFellow("desc"); 
		
		ZulConstraint constraints = new ZulConstraint();
		constraints.registerComponent(usernameTextbox, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(passwordTextbox, ZulConstraint.UPPER_CASE);
		constraints.registerComponent(descTextbox, ZulConstraint.UPPER_CASE);
		constraints.validateComponent(false);
		win.doModal();
		if(win.getAttribute("aksi") == MedisafeConstants.OK_BUTTON ){
			//todo cek validasi password dulu sbl proses save
			if (!constraints.validateComponent(true))
				return;
			
			//check if user valid
			MD5 md5 = new MD5(passwordTextbox.getValue());
			String username = usernameTextbox.getValue();
			String password = md5.toString();
			
			if(username.equals(user.getVUserName()) && password.equals(user.getVPassword())){
				//todo SAVE HISTORY LAPORAN KE FILE
				// - GL
				// - BS
				// - PL
				String bsQuery = "select * from report.balance_sheet()";
				String glQuery = "select * from report.func_gl_all()";
				String plQuery = "select * from report.v_profit_loss"; //ambil dari view 
				String reportName = PrintClient.getDate(new Date(), "yyyyMMdd-hhmmss");
				
				
				Map<String, String> parameters = new HashMap<String, String>();
				String dateParam = "DATE RANGE: ";
				parameters.put("dateParam", dateParam );
				
				re = new ReportEngine(glQuery,ReportService.getKey("generalLedger"), parameters);
				re.savePdf(REPORT_GL + reportName);
				
				re = new ReportEngine(bsQuery,ReportService.getKey("balanceSheet"), parameters);
				re.savePdf(REPORT_BS + reportName);
				
				re = new ReportEngine(plQuery,ReportService.getKey("profitLoss"), parameters);
				re.savePdf(REPORT_PL + reportName);
				
				journalManager.postingJournal(user.getVUserName(), descTextbox.getText(), reportName);
				journalManager.getAllPostingHistory(postList);
				MiscTrxController.setFont(postList);
			}
			else{
				Messagebox.show(MessagesService.getKey("common.password.not.match"));
				return;
			}
		}
	}
	public void doPostingBack() throws VONEAppException, InterruptedException{
		Listitem pilih = postList.getSelectedItem();
		if(pilih == null){
			return;
		}
		TbHistoryPosting tbHistoryPosting = (TbHistoryPosting) pilih.getValue();
		if(tbHistoryPosting.getNStatus() == 0){
			final Window win = (Window) Executions.createComponents(
					"/zkpages/report/postingBack.zul", null, null);
			
			Textbox usernameTextbox = (Textbox) win.getFellow("username"); 
			Textbox passwordTextbox = (Textbox) win.getFellow("password"); 
			Textbox descTextbox = (Textbox) win.getFellow("desc"); 
			
			ZulConstraint constraints = new ZulConstraint();
			constraints.registerComponent(usernameTextbox, ZulConstraint.UPPER_CASE);
			constraints.registerComponent(passwordTextbox, ZulConstraint.UPPER_CASE);
			constraints.registerComponent(descTextbox, ZulConstraint.UPPER_CASE);
			constraints.validateComponent(false);
			
			win.setAttribute("aksi", MedisafeConstants.CANCEL_BUTTON);
			win.doModal();
			if(win.getAttribute("aksi") == MedisafeConstants.OK_BUTTON){
				//todo cek validasi password dulu sbl proses save
				if (!constraints.validateComponent(true))
					return;
				
				//check if user valid
				MD5 md5 = new MD5(passwordTextbox.getValue());
				String username = usernameTextbox.getValue();
				String password = md5.toString();
				
				if(username.equals(user.getVUserName()) && password.equals(user.getVPassword())){
					journalManager.postingJournalBack(tbHistoryPosting.getNFlag(),user.getVUserName(), descTextbox.getText());
					journalManager.getAllPostingHistory(postList);
				}else{
					Messagebox.show(MessagesService.getKey("common.password.not.match"));
					return;
				}
			}
		}else{
			Messagebox.show("GAGAL MENGEMBALIKAN POSTING");
		}
	}

}
