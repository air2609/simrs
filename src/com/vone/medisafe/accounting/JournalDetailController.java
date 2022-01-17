package com.vone.medisafe.accounting;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.acct.JournalManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

public class JournalDetailController  extends BaseController{
	
	Listbox detailList;
	Button btnEdit;
	Button btnSave;
	Button btnDelete;
	
	JournalManager manager = Service.getJournalManager();

	@Override
	public void init(Component cmp) throws InterruptedException, VONEAppException {
		super.init(cmp);
		
		btnEdit = (Button)cmp.getFellow("btnEdit");
		btnSave = (Button)cmp.getFellow("btnSave");
		btnDelete = (Button)cmp.getFellow("btnDelete");
		detailList = (Listbox)cmp.getFellow("detailList");
		btnSave.setDisabled(true);
	}
	
	public void delete() throws VONEAppException {
		Listitem item = (Listitem) detailList.getItems().get(0);
		String batchNo = ((Listcell)item.getChildren().get(0)).getLabel();
		
		String username = this.getUserInfoBean().getStUserName();
		
		int confirm;
		try {
			confirm = Messagebox.show("Anda akan menghapus data journal, apakah Anda yakin?", "KONFIRMASI", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION);
			if(confirm == Messagebox.NO)return;
			manager.deleteJournal(batchNo, username, detailList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void edit() {
		List<Listitem> items = detailList.getItems();
		Decimalbox db;
		String formatDb="#,###,###";
		int counter = 0;
		for(Listitem item : items) {
			
			//voucher no
			Listcell cell = (Listcell)item.getChildren().get(1);
			String cellLabel = cell.getLabel();
			cell.setLabel(null);
			Textbox tbox = new Textbox(cellLabel);
			tbox.setParent(cell);
			
			//account 
			cell = (Listcell)item.getChildren().get(2);
			Bandbox bbox = getBandboxAccount(counter);
			bbox.setText(cell.getLabel());
			cell.setLabel(null);
			bbox.setParent(cell);
			
			//keterangan
			cell = (Listcell)item.getChildren().get(3);
			cellLabel = cell.getLabel();
			cell.setLabel(null);
			tbox = new Textbox(cellLabel);
			tbox.setParent(cell);
			
			
			//debet
			cell = (Listcell)item.getChildren().get(4);
			cellLabel = cell.getLabel();
			cell.setLabel(null);

			db = new Decimalbox();
			db.setFormat(formatDb);
			db.setText(cellLabel);
			db.setParent(cell);
			
			//credit
			cell = (Listcell)item.getChildren().get(5);
			cellLabel = cell.getLabel();
			cell.setLabel(null);

			db = new Decimalbox();
			db.setFormat(formatDb);
			db.setText(cellLabel);
			db.setParent(cell);

			
			//apl date
			cell = (Listcell)item.getChildren().get(6);
			cellLabel = cell.getLabel();
			cell.setLabel(null);

			Datebox datebox = new Datebox();
			datebox.setFormat("dd-MM-yyyy");
			datebox.setText(cellLabel);
			datebox.setParent(cell);

			counter = counter + 1;
		}
		
		btnEdit.setDisabled(true);
		btnSave.setDisabled(false);
	}
	 
	public Bandbox getBandboxAccount(int counter) {
		Bandbox bbox = new Bandbox();
		Bandpopup bp = new Bandpopup();
		Vbox vbox = new Vbox();
		vbox.setParent(bp);
		Hbox hbox = new Hbox();
		Textbox tbox = new Textbox();
		tbox.setParent(hbox);
		Button btnSearch = new Button();
		btnSearch.setLabel("CARI");
		btnSearch.setParent(hbox);
		
		hbox.setParent(vbox);
		Listbox listbox = new Listbox();
		Listhead head = new Listhead();
		Listheader header = new Listheader();
		header.setLabel("Account No");
		header.setParent(head);
		
		header.setLabel("Account Name");
		header.setParent(head);
		
		head.setParent(listbox);
		listbox.setParent(vbox);
		listbox.setMold("paging");
		listbox.setPageSize(10);
		bp.setParent(bbox);
		
		bbox.setId("bbox"+counter);
		bp.setId("bp"+counter);
		vbox.setId("vbox"+counter);
		hbox.setId("hbox"+counter);
		tbox.setId("tbox"+counter);
		btnSearch.setId("btnSearch"+counter);
		listbox.setId("listbox"+counter);

		
		btnSearch.addEventListener("onClick", new EventListener() {
			
			JournalManager journalManager = Service.getJournalManager();
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				journalManager.searchCoa(tbox.getText(), listbox);
				
			}
		});
		
		return bbox;
	}
}
