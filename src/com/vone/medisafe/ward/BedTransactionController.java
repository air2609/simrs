package com.vone.medisafe.ward;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.mapping.Constraint;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.IdsServiceLocator;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.common.util.ZulConstraint;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.MsStaffInUnit;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBedOccupancy;
import com.vone.medisafe.mapping.TbBedTrx;
import com.vone.medisafe.mapping.TbExamination;
import com.vone.medisafe.mapping.TbMedicalRecord;
import com.vone.medisafe.mapping.TbRegistration;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.Service;
import com.vone.medisafe.service.iface.admin.UserManager;
import com.vone.medisafe.services.locator.ServiceLocator;
import com.vone.medisafe.ui.base.BaseController;

/**
 * BedTransactionController.java
 * @author Arifullah Ibn Rusyd Al-Bantani (HP: +6281314282206)
 * @since Nov, 13 2006
 * @category Visual (User Interface)
 *
 */

public class BedTransactionController extends BaseController{
	
	public MsUser user;
	public TbMedicalRecord mr;
	public TbRegistration reg;
	public TbExamination nota;
	public TbBedTrx bedTrx;
	public MsPatient patient;
	
	public Listbox locationList;
	public Textbox transactionNumber;
	public Bandbox MRNumber;
	public Textbox crNoMR;
	public Textbox crNama;
	public Textbox crNoAlamat;
	public Listbox patientList;
	public Textbox registrationNumber;
	public Textbox patientName;
	public Textbox age;
	public Radiogroup gender;
	public Textbox patientType;
	
	public Tree bedTree;
	public Treechildren childTree;
	public Treeitem treeItem;
	public Button btnSave;
	public Button btnNew;
	
	
	Listitem item;
	Listcell cell;
	Session session;
	
	ZulConstraint constraints = new ZulConstraint();
	SimpleDateFormat ftgl = new SimpleDateFormat("dd/MM/yyyy");
	
	
	private BedTransactionManager bedServ = Service.getBedTransactionManager();
	
	UserManager userService = ServiceLocator.getUserManager();

	@Override
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	@Override
	public void init(Component win) throws InterruptedException, VONEAppException {
		
		super.init(win);
		
		locationList = (Listbox)win.getFellow("locationList");
		transactionNumber = (Textbox)win.getFellow("transactionNumber");
		MRNumber = (Bandbox)win.getFellow("MRNumber");
		crNoMR = (Textbox)win.getFellow("crNoMR");
		crNama = (Textbox)win.getFellow("crNama");
		crNoAlamat = (Textbox)win.getFellow("crNoAlamat");
		patientList = (Listbox)win.getFellow("patientList");
		registrationNumber = (Textbox)win.getFellow("registrationNumber");
		patientName = (Textbox)win.getFellow("patientName");
		age = (Textbox)win.getFellow("age");
		gender = (Radiogroup)win.getFellow("gender");
		patientType = (Textbox)win.getFellow("patientType");
		bedTree = (Tree)win.getFellow("bedTree");
		childTree = (Treechildren)win.getFellow("childTree");
		btnSave = (Button)win.getFellow("btnSave");
		btnNew = (Button)win.getFellow("btnNew");
		
		user = getUserInfoBean().getMsUser();
		session = win.getDesktop().getSession();
		
		userService.getUnitUser(user, locationList);
		
		setFieldDisable(true);
		locationList.setDisabled(false);
		MRNumber.setDisabled(false);
		
		constraints.registerComponent(crNama,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoAlamat,ZulConstraint.UPPER_CASE);
		constraints.registerComponent(crNoMR,ZulConstraint.UPPER_CASE);
		
	}
	
		
	
	public void getRegistration(int type) throws InterruptedException, VONEAppException{

		bedServ.getPatitentRegistration(this,type);
		
		setFieldDisable(true);
		MRNumber.setDisabled(false);
		locationList.setDisabled(false);
		MiscTrxController.setFont(bedTree);
		
	}
	
	public void setFieldDisable(boolean isDisable){
		
		locationList.setDisabled(isDisable);
		transactionNumber.setReadonly(isDisable);
		MRNumber.setDisabled(isDisable);
		patientList.setDisabled(isDisable);
		registrationNumber.setReadonly(isDisable);
		patientName.setReadonly(isDisable);
		age.setReadonly(isDisable);
		((Radio)gender.getChildren().get(0)).setDisabled(isDisable);
		((Radio)gender.getChildren().get(1)).setDisabled(isDisable);
		patientType.setReadonly(isDisable);
		
	}
	
	public void clear() throws VONEAppException{
		locationList.setSelectedIndex(0);
		transactionNumber.setValue(null);
		MRNumber.setValue(null);
		patientList.getItems().clear();
		registrationNumber.setValue(null);
		patientName.setValue(null);
		age.setValue(null);
		gender.setSelectedItem(null);
		patientType.setValue(null);
		patientList.getItems().clear();
		crNama.setValue(null);
		crNoAlamat.setValue(null);
		crNoMR.setValue(null);
		MRNumber.focus();
		
	}
	
	
	
		
	
	public void createNote() throws VONEAppException, InterruptedException
	{
		Set treeItems =  this.bedTree.getSelectedItems();
		
		if(treeItems.size() == 0){
			Messagebox.show(MessagesService.getKey("pilih.transaksi.bed.yang.akan.dibuat.notanya"));
			return;
		}
		
		Date createDate = new Date();
		
		Treeitem parent = null;
		
		boolean isClosed = false;
		
		nota = new TbExamination();
		bedTrx = new TbBedTrx();
		
		nota.setDWhnCreate(createDate);
		nota.setNPaymentStatus(new Short(MedisafeConstants.BELUM_LUNAS));
		nota.setNExamStatus(new Integer(MedisafeConstants.VALIDATED_NOTE));
		nota.setVWhoCreate(user.getVUserName());
		
		bedTrx.setDWhoCreate(createDate);
		bedTrx.setVWhoCreate(user.getVUserName());
		
		int jumlahJam = 0;
		double jumlahTrx = 0;
		boolean isTheLast=false;
		
		List<Date> bors = new ArrayList<Date>();
		
		int counter = 0;
		int realCounter = 0;
		Iterator it = treeItems.iterator();
		while(it.hasNext()){
			treeItem = (Treeitem)it.next();
			Date selectedDate = (Date)treeItem.getValue();
			bors.add(selectedDate);
			
			if(ftgl.format(createDate).equals(ftgl.format((Date)treeItem.getValue())))
				isClosed = true;
			Treecell tcell = (Treecell)treeItem.getTreerow().getChildren().get(4);
			
			if (realCounter == (treeItems.size() - 1)){
				isTheLast = true;
			}
			else isTheLast = false;
			
			if(tcell.getLabel().equals("-")){
				
				
				if(counter == 0){
					
					bedTrx.setDDateFrom((Date)treeItem.getValue());
					parent = (Treeitem)treeItem.getParent().getParent();
					
				}
				
				
//				if(counter == (realCounter - counter)){
//					bedTrx.setDDateTo((Date)treeItem.getValue());
//					parent = (Treeitem)treeItem.getParent().getParent();
//				}
				
				
				if(isTheLast){
					bedTrx.setDDateTo((Date)treeItem.getValue());
					parent = (Treeitem)treeItem.getParent().getParent();
				}
				
				jumlahJam = jumlahJam + ((Integer)treeItem.getAttribute("jam")).intValue();
				jumlahTrx = jumlahTrx + ((Double)treeItem.getAttribute("total")).doubleValue();
				
				counter++;
			}
			
			
			realCounter++;
			
				
		}
		
		if(isClosed){
			int result = Messagebox.show(MessagesService.getKey("bed.transaction.closed.confirmation"), 
					"Question", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			if(result == Messagebox.NO) return;
		}
		if(parent != null){
			
			TbBedOccupancy boc = (TbBedOccupancy)parent.getValue();
			boc.setVWhoChange(user.getVUserName());
			
			bedTrx.setNTotalHour((short)jumlahJam);
			bedTrx.setNAmountAfterDisc(jumlahTrx);
			bedTrx.setNDiscAmount(0);
			bedTrx.setNFee(jumlahTrx);
			bedTrx.setMsBed(((TbBedOccupancy)parent.getValue()).getId().getMsBed());
			bedTrx.setMsPatient(patient);
			
			nota.setMsUnit((MsUnit)locationList.getSelectedItem().getValue());
			nota.setMsPatient(patient);
			nota.setTotal(jumlahTrx);
			nota.setTbRegistration(reg);
			
			Integer noNota = IdsServiceLocator.getIdsManager().getSequence(MedisafeConstants.NOTA_RANAP);
			nota.setVNoteNo(MedisafeUtil.generateNotaNumber(noNota, createDate, nota.getMsUnit().getVUnitCode(),
					MedisafeConstants.RANAP));
			
			if(bedServ.save(nota, bedTrx, isClosed, boc, bors)){
				Messagebox.show(MessagesService.getKey("common.save.success"));
				this.transactionNumber.setValue(nota.getVNoteNo());
				it = treeItems.iterator();
				while(it.hasNext()){
					treeItem = (Treeitem)it.next();
					Treecell tcell = (Treecell)treeItem.getTreerow().getChildren().get(4);
					if(tcell.getLabel().equals("-"))
						tcell.setLabel(nota.getVNoteNo());
				}
				bedTree.clearSelection();
				
			}else{
				Messagebox.show(MessagesService.getKey("common.save.fail"));
			}
			
		}
		else{
			Messagebox.show(MessagesService.getKey("bangsal.bed.sudah.dibuat.nota"));
		}
		
	}
	
	
	public void createNew() throws VONEAppException{
		childTree.getChildren().clear();
		clear();
	}

}
