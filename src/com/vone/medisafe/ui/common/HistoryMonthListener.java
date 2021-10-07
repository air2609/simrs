package com.vone.medisafe.ui.common;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.NoteManager;

public class HistoryMonthListener implements EventListener{

	private Listbox historyList;
	private List dataList;
	private String bulan;
	private String tahun;
	private Decimalbox grandTotal;
	
	NoteManager service = Service.getNotaManager();
	
	
	public HistoryMonthListener(Listbox historyList, List dataList, String bulan,
			String tahun, Decimalbox decimalbox){
		this.historyList = historyList;
		this.dataList = dataList;
		this.bulan = bulan;
		this.tahun = tahun;
		this.grandTotal = decimalbox;
	}

	public boolean isAsap() {
		// TODO Auto-generated method stub
		return true;
	}

	public void onEvent(Event arg0) {
		// TODO Auto-generated method stub
		historyList.getItems().clear();
		
		
		Listitem item = null;
		Listcell cell = null;
		
		String bln ="";
		String thn = "";
		double total=0;
		Decimalbox db = new Decimalbox();
		db.setFormat("#,###.##");
		
		MsUnit unit = null;
		
		Iterator it = dataList.iterator();
		while(it.hasNext()){
			TbExamination exam = (TbExamination)it.next();
			bln = MedisafeUtil.getYearMonthDay(exam.getDWhnCreate(), MedisafeConstants.MONTH);
			thn = MedisafeUtil.getYearMonthDay(exam.getDWhnCreate(), MedisafeConstants.YEAR);
			
			if(tahun.equals(thn) && bulan.equals(bln)){
				item = new Listitem();
				item.setValue(exam);
				item.setParent(historyList);
				
				cell = new Listcell();
				cell.setParent(item);
				cell.setLabel(exam.getVNoteNo());
				
				cell = new Listcell();
				cell.setParent(item);
				
				try {
					unit = service.getUnitNote(exam);
				} catch (VONEAppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(unit != null)
					cell.setLabel(unit.getVUnitName());
				else cell.setLabel("ADMISI");
				
				
				if(exam.getTotal() == null)db.setValue(new BigDecimal(0));
				else db.setValue(new BigDecimal(exam.getTotal().doubleValue()));
				
				cell = new Listcell();
				cell.setParent(item);
				cell.setLabel(db.getText());
				
				total=total+db.getValue().doubleValue();
			}
		}
		
		grandTotal.setValue(new BigDecimal(total));
		
	/*	item = new Listitem();
		item.setParent(historyList);
		
		cell = new Listcell();
		cell.setParent(item);
		cell.setLabel("");
		
		cell = new Listcell();
		cell.setParent(item);
		cell.setLabel("");
	
		cell = new Listcell();
		cell.setParent(item);
	    cell.setLabel("");
		
	    item = new Listitem();
		item.setParent(historyList);
		
		cell = new Listcell();
		cell.setParent(item);
		cell.setLabel("");
		
		cell = new Listcell();
		cell.setParent(item);
		cell.setLabel("TOTAL  : ");
		
		db.setValue(new BigDecimal(total));
		cell = new Listcell();
		cell.setParent(item);
		cell.setLabel(db.getText());*/
		MiscTrxController.setFont(historyList);
	}


}
