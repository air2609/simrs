package com.vone.medisafe.warehouse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.printing.PrintClient;
import com.vone.medisafe.report.ReportEngine;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.ui.base.BaseController;

public class HistoryRequestController  extends BaseController {
	public Datebox endDate;
	public Datebox startDate;
	public Treechildren permintaanTreechildren;
	public MsUser user;
	public Session session;
	public WarehouseManager warehouseManager = MasterServiceLocator.getWarehouseManager();
	public Integer whouseid;
	private Treeitem treeItem;
	private Treerow treeRow;
	private Treecell treeCell;
	private Treechildren treeChildren;
	
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		
		permintaanTreechildren = (Treechildren)win.getFellow("permintaanTreechildren");
		endDate = (Datebox)win.getFellow("endDate");
		startDate = (Datebox)win.getFellow("startDate");
		
		startDate.setValue(new Date());
		endDate.setValue(new Date());
		whouseid = 0;
		
		session = win.getDesktop().getSession();
		user = getUserInfoBean().getMsUser();
		
		getRequestItem();
	}
	
	public void getRequestItem() throws VONEAppException{
		warehouseManager.getSentItem(this);
	}
	
	public void createGroupTree(Object value, String nomorPermintaan, String gudangPeminta, Component parent) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(parent);
		treeItem.setAttribute("tipe", "group");

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		
		treeCell = new Treecell(nomorPermintaan);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt;font-weight:bold");
		treeCell.addEventListener("onClick", new GroupItemListener());
		
		treeCell = new Treecell(gudangPeminta);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt;font-weight:bold");
		
		treeChildren = new Treechildren();
		treeChildren.setParent(treeItem);
	}
	public void createItemTree(Object value, String itemCaption, String satuan,
			int jmlOrder, int jmlTerima, int jmlSisa) {
		treeItem = new Treeitem();
		treeItem.setValue(value);
		treeItem.setParent(treeChildren);
		treeItem.setAttribute("tipe", "anak");

		treeRow = new Treerow();
		treeRow.setParent(treeItem);
		treeCell = new Treecell(itemCaption);
		treeCell.setParent(treeRow);
		treeItem.setAttribute("nama", itemCaption);
		treeCell.setStyle("font-size:8pt");

		treeCell = new Treecell("");
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");

		treeCell = new Treecell(satuan);
		treeCell.setParent(treeRow);
		treeItem.setAttribute("satuan", satuan);
		treeCell.setStyle("font-size:8pt");
		
		treeCell = new Treecell("" + jmlOrder);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");

		treeCell = new Treecell("" + jmlTerima);
		treeCell.setParent(treeRow);
		treeItem.setAttribute("jmlSisa", jmlTerima);
		treeCell.setStyle("font-size:8pt");
	}
	
	public void cetak() throws VONEAppException{
		String dari = PrintClient.getDate(startDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL);
		String sampai = PrintClient.getDate(endDate.getValue(), MedisafeConstants.DATE_FORMAT_SQL);
		dari = " '" + dari + " 00:00:00' ";
		sampai = " '" + sampai + " 23:59:59' ";
		
		Integer wh = (Integer) session.getAttribute("whouseid");
		StringBuffer sql = new StringBuffer(); 
		sql.append("select * from report.v_history_request where n_source_whouse_id=");
		sql.append(wh.intValue());
		sql.append(" and d_whn_create between ");
		sql.append(dari);
		sql.append(" and ");
		sql.append(sampai);
		sql.append(" order by v_request_code, d_whn_create");
		System.out.println(sql.toString());
		String lokasi = "";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("title", "HISTORY PERMINTAAN\n"+
				lokasi +
				"\nTGL: " +
				PrintClient.getDate(startDate.getValue(), MedisafeConstants.DATE_FORMAT) +
				" S/D " +
				PrintClient.getDate(endDate.getValue(), MedisafeConstants.DATE_FORMAT)
				);
		
		ReportEngine re = new ReportEngine(sql.toString(),ReportService.getKey("history.request"), parameters);
		if(!re.printOut(session.getClientAddr()))
			re.openPdf();		
	}


}
