package com.vone.medisafe.accounting;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.report.ReportEngine;

public class AccountingReport {
	private static ReportEngine re;
	private static Session session;

	public static void openCurrentLabarugi(Component win) throws VONEAppException{
		session = win.getDesktop().getSession(); 
		String plQuery = "select * from report.v_profit_loss";
		Map<String, String> parameters = new HashMap<String, String>();
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		re = new ReportEngine(plQuery,ReportService.getKey("profitLoss"), parameters);
		if(!re.printOut(session.getClientAddr()))
			re.openPdf();

	}	

	public static void openCurrentNeraca(Component win) throws VONEAppException{
		session = win.getDesktop().getSession(); 
		
		//code berikut utk membua current neraca
		String bsQuery = "select * from report.balance_sheet()";
		Map<String, String> parameters = new HashMap<String, String>();
		String dateParam = "DATE RANGE: ";
		parameters.put("dateParam", dateParam );
		re = new ReportEngine(bsQuery,ReportService.getKey("balanceSheet"), parameters);
		//String ipTarget = postList.getParent().getc;
		if(!re.printOut(session.getClientAddr()))
			re.openPdf();
	}
	
}
