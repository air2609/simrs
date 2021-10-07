package com.vone.medisafe.warehouse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbItemMutation;
import com.vone.medisafe.mapping.pojo.TbItemRequest;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.WarehouseManager;
import com.vone.medisafe.ui.base.BaseController;

public class ItemRequestApproveController extends BaseController {
	//detailRequest.zul (part of itemRequest.zul)
	public WarehouseManager warehouseManager = MasterServiceLocator.getWarehouseManager();
	
	Tree setujuTree;
	Tree requestTree;;
	public Treechildren setujuTreechildren;
	public Treechildren requestTreechildren;
	
	Treeitem treeItem;
	Treerow treeRow;
	Treecell treeCell;
	Treechildren treeChildren;
	
	Logger log = Logger.getLogger(ItemRequestApproveController.class);
	
	public Session session;
	MsUser user;
	MsStaffInUnit siu;

	public int whouseid;

	public ItemRequestApproveController() {
		whouseid = 0;
	}
	public void init(Component win) throws InterruptedException, VONEAppException {
		super.init(win);
		log.debug("INIT");
		
		setujuTreechildren = (Treechildren)win.getFellow("setujuTreechildren");
		requestTreechildren = (Treechildren)win.getFellow("requestTreechildren");
		setujuTree = (Tree)win.getFellow("setujuTree");
		requestTree = (Tree)win.getFellow("requestTree");
		Button btnCancel = (Button)win.getFellow("btnCancel");
		btnCancel.setDisabled(false);
		
		session = win.getDesktop().getSession();
		user = getUserInfoBean().getMsUser();
		
		//todo urgen
		getSentItem();
		getRequestItem();
	}
	private void getRequestItem() throws VONEAppException {
		//menampilkan daftar permintaan yg telah dilakukan 
		//dan blm disetujui
		warehouseManager.getRequestItem(this);
	}
	public void refreshClick() throws VONEAppException{
		getSentItem();
		getRequestItem();
	}
	public void setujuClick() throws VONEAppException{
		log.debug("SETUJU");
		Set<TbItemMutation> itemMutationSet = new HashSet<TbItemMutation>();
		Set list = setujuTree.getSelectedItems();
		Iterator iter = list.iterator();
		TbItemMutation tbItemMutation;
		while (iter.hasNext()) {
			Treeitem elemItem = (Treeitem) iter.next();
			if (elemItem.getAttribute("tipe").equals("anak")){
				tbItemMutation = (TbItemMutation) elemItem.getValue();
				itemMutationSet.add(tbItemMutation);
			}
		}
		
		warehouseManager.saveItemApprove(itemMutationSet);
		log.debug(itemMutationSet.size());
		getSentItem();
	}
	
	public void btnCancelClick() throws VONEAppException{
		
		if (requestTree.getSelectedItem() == null) {
			try {
				Messagebox.show(MessagesService
						.getKey("common.modify.list.notselected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

		int confirm;
		try {
			confirm = Messagebox.show(MessagesService
					.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
					| Messagebox.NO, Messagebox.NONE);
			if (confirm == Messagebox.NO){
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Set<TbItemRequest> itemRequestSet = new HashSet<TbItemRequest>();
		Set list = requestTree.getSelectedItems();
		Iterator iter = list.iterator();
		TbItemRequest tbItemRequest;
		while (iter.hasNext()) {
			
			Treeitem elemItem = (Treeitem) iter.next();
			if (elemItem.getAttribute("tipe").equals("anak")){
				tbItemRequest = (TbItemRequest) elemItem.getValue();
				itemRequestSet.add(tbItemRequest);
			}
		}
		System.out.println(itemRequestSet.size());
		
		warehouseManager.cancelItemRequest(itemRequestSet);
		System.out.println("TESTTT");
		getRequestItem();
	}
	private void getSentItem() throws VONEAppException{
		//menampilkan daftar item yg telah dikirim utk disetujui
		warehouseManager.getSentItem(this);
		
	}
	
	public void createGroupTree(Object value, String nomorPermintaan, Component parent) {
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

		treeCell = new Treecell("" + jmlTerima);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");

		treeCell = new Treecell(satuan);
		treeCell.setParent(treeRow);
		treeItem.setAttribute("satuan", satuan);
		treeCell.setStyle("font-size:8pt");
		
		treeCell = new Treecell("" + jmlOrder);
		treeCell.setParent(treeRow);
		treeCell.setStyle("font-size:8pt");

		treeCell = new Treecell("" + jmlSisa);
		treeCell.setParent(treeRow);
		treeItem.setAttribute("jmlSisa", jmlSisa);
		treeCell.setStyle("font-size:8pt");

	}
	public void btnCancelRequestClick() throws VONEAppException{
		if (setujuTree.getSelectedItem() == null) {
			try {
				Messagebox.show(MessagesService
						.getKey("common.modify.list.notselected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

		int confirm;
		try {
			confirm = Messagebox.show(MessagesService
					.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES
					| Messagebox.NO, Messagebox.NONE);
			if (confirm == Messagebox.NO){
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Set<TbItemMutation> itemMutationSet = new HashSet<TbItemMutation>();
		Set list = setujuTree.getSelectedItems();
		Iterator iter = list.iterator();
		TbItemMutation tbItemMutation;
		while (iter.hasNext()) {
			Treeitem elemItem = (Treeitem) iter.next();
			if (elemItem.getAttribute("tipe").equals("anak")){
				tbItemMutation = (TbItemMutation) elemItem.getValue();
				itemMutationSet.add(tbItemMutation);
			}
		}
		
		warehouseManager.cancelItemApprove(itemMutationSet);
		log.debug(itemMutationSet.size());
		getSentItem();
	}
}
