package com.vone.medisafe.ui.common;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;
import com.vone.medisafe.common.util.DiscontListener;
import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.general.authorization.UserInfoBean;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsUnit;
import com.vone.medisafe.mapping.MsUser;
import com.vone.medisafe.mapping.TbBundledItemUsedTrx;
import com.vone.medisafe.mapping.TbBundledTreatUsedTrx;
import com.vone.medisafe.mapping.TbBundledTrx;
import com.vone.medisafe.mapping.pojo.item.MsItem;
import com.vone.medisafe.misc.MiscTrxController;
import com.vone.medisafe.service.TreatmentService;
import com.vone.medisafe.ui.base.BaseController;

public class CommonPaketController extends BaseController{

	private TbBundledItemUsedTrx bundleItemUsedTrx;
	private TbBundledTreatUsedTrx bundleTreatTrx;
	private TbBundledTrx bundleTrx;
	
	private MsUser user;
	
	Listbox treatmentNameList;
	Listbox paketList;
	
	Component win;
	
	Listitem item;
	Listcell cell;
	
	Listbox listbox;
	
	String kelasTarif;
	
	Session session;
	

	public void doCancel(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doCancel(cmp);
	}

	public void doClose(Component cmp) throws InterruptedException, VONEAppException {
		// TODO Auto-generated method stub
		super.doClose(cmp);
	}

	
	public UserInfoBean getUserInfoBean() {
		// TODO Auto-generated method stub
		return super.getUserInfoBean();
	}

	public void init(Component win) throws InterruptedException, VONEAppException {
		treatmentNameList = (Listbox)win.getFellow("treatmentNameList");
		paketList = (Listbox)win.getFellow("paketList");
				
		this.win = win;
		
		session = win.getDesktop().getSession();
		
		listbox = (Listbox)session.getAttribute("listbox");
		Listbox location = (Listbox)session.getAttribute("location");
		
		super.init(win);
		
		UserInfoBean ub = getUserInfoBean();
		if(ub == null){
			Messagebox.show("uib kosong");
			return;
		}
		
		user = ub.getMsUser();
		
		
	
		MsUnit unit = (MsUnit)location.getSelectedItem().getValue();
		if(session.getAttribute("isRanap").equals("YES")){
			//ambil kelas tarif ranap
			kelasTarif = session.getAttribute("kelasTarif").toString();
		}else{
			//default kelas tarif : Kelas II
			kelasTarif = "KELAS II";
		}
			
		getPacketTreatment(unit, kelasTarif);
		
		paketList.getItems().clear();
	
	}
	
	public void getPacketTreatment(MsUnit unit, String kelasTarif) throws InterruptedException, 
		VONEAppException{
		
		TreatmentService.getTreatmentManager().getTreatmentPacketForSelect(treatmentNameList, 
				unit, kelasTarif, MedisafeConstants.PAKET);
		
	}
	
	
	public void addItemOrTreatment(){
		session.setAttribute("bundlelist", paketList);
	}
	
	
	public void getPacketTreatment() throws InterruptedException{
		
		bundleTrx = new TbBundledTrx();
		
		if(treatmentNameList.getSelectedItem().getValue().toString() != MedisafeConstants.LISTKOSONG){
			bundleTrx.setMsTreatmentFee((MsTreatmentFee)treatmentNameList.getSelectedItem().getValue());
		}else{
			Messagebox.show(MessagesService.getKey("common.packet.not.selected"));
			treatmentNameList.focus();
			return;
		}
		
		Set<TbBundledTreatUsedTrx> bundlteTreat = new HashSet<TbBundledTreatUsedTrx>(0);
		Set<TbBundledItemUsedTrx> bundleItem = new HashSet<TbBundledItemUsedTrx>(0);
		
		List paket = paketList.getItems();
		Iterator it = paket.iterator();
		while(it.hasNext()){
			item = (Listitem)it.next();
			if(item.getValue() instanceof Object[]){
				bundleItemUsedTrx = new TbBundledItemUsedTrx();
				Object[] obj = (Object[])item.getValue();
				Integer itemUsed = new Integer(((Listcell)item.getChildren().get(1)).getLabel());
				bundleItemUsedTrx.setNQty(itemUsed.shortValue());
				MsItem msItem = new MsItem();
				msItem.setNItemId((Integer)obj[0]);
				bundleItemUsedTrx.setMsItem(msItem);
				bundleItemUsedTrx.setVWhoCreate(user.getVUserName());
				bundleItemUsedTrx.setNSellingPrice((Double)obj[4]);
				bundleItem.add(bundleItemUsedTrx);
			}
			else{
				MsTreatmentFee  biaya = (MsTreatmentFee)item.getValue();
				bundleTreatTrx = new TbBundledTreatUsedTrx();
				bundleTreatTrx.setMsTreatment(biaya.getMsTreatment());
				bundleTreatTrx.setVWhoCreate(user.getVUserName());
				bundleTreatTrx.setNSellingPrice(biaya.getNTrtfeeFee());
				bundleTreatTrx.setMsCoa(biaya.getMsCoa());
				bundlteTreat.add(bundleTreatTrx);
			}
		}
		
		bundleTrx.setTbBundledTreatUsedTrxes(bundlteTreat);
		bundleTrx.setTbBundledItemUsedTrxes(bundleItem);
		bundleTrx.setVWhoCreate(user.getVUserName());
		
		Listitem listitem;
		Listcell listcell;
		Label harga;
	
		
		
		listitem = new Listitem();
		listitem.setValue(bundleTrx);
		listitem.setParent(listbox);
			
		listcell = new Listcell(bundleTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentCode());
		listcell.setParent(listitem);
			
		listcell = new Listcell(bundleTrx.getMsTreatmentFee().getMsTreatment().getVTreatmentName());
		listcell.setParent(listitem);
			
		listcell = new Listcell("1");
		listcell.setParent(listitem);
			
		listcell = new Listcell("-");
		listcell.setParent(listitem);
			
		Decimalbox dbharga = new Decimalbox();
		dbharga.setValue(new BigDecimal(bundleTrx.getMsTreatmentFee().getNTrtfeeFee()));
		dbharga.setFormat("#,###");
		harga = new Label(dbharga.getText());
		listcell = new Listcell(harga.getValue());
		listcell.setParent(listitem);
			
		Listcell diskon  = new Listcell();
		diskon.setParent(listitem);
		Decimalbox decimalbox = new Decimalbox();
		decimalbox.setValue(new BigDecimal(0));
		decimalbox.setParent(diskon);
		decimalbox.setWidth("50%");
		decimalbox.setFormat("#,###");
		decimalbox.setStyle("font-size:8pt");
		decimalbox.setHeight("14px");
		Listbox diskonList = new Listbox();
		diskonList.setParent(diskon);
		diskonList.setWidth("40%");
		diskonList.setMold("select");
		diskonList.setStyle("font-size:9pt");
		item = new Listitem();
		item.setValue(MedisafeConstants.RP);
		item.setSelected(true);
		item.setLabel("1. RP");
		item.setParent(diskonList);
		item = new Listitem();
		item.setValue(MedisafeConstants.PERCENT);
		item.setLabel("2. %");
		item.setParent(diskonList);
			
//			Label finalPrice = new Label();
		listcell = new Listcell(harga.getValue());
		listcell.setParent(listitem);
		
		Object[] obj = new Object[]{dbharga.getValue(),dbharga.getValue(),"RP",new Integer(1),decimalbox.getValue()};
		listitem.setAttribute("manipulation", obj);
		
		decimalbox.addEventListener("onChange", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell,new Intbox(1), new Short("0")));
		diskonList.addEventListener("onSelect", new DiscontListener(listitem,diskonList,harga,decimalbox,listcell, new Intbox(1), new Short("0")));
			
		this.win.detach();
		MiscTrxController.setFont(this.listbox);
	}
	
	public void getOut(){
		session.removeAttribute("listbox");
	}
	
	public void delete() throws InterruptedException{
		if(paketList.getSelectedItem() == null){
			Messagebox.show(MessagesService.getKey("common.modify.list.notselected"));
			return;
		}
		int index = paketList.getSelectedIndex();
		
		int confirm = Messagebox.show(MessagesService.getKey("common.delete.confirm"), "KONFIRMASI", Messagebox.YES |
				Messagebox.NO, Messagebox.NONE);
		if(confirm == Messagebox.NO)return;
		paketList.removeItemAt(index);
	}
	


}
