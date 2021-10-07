package com.vone.medisafe.antrian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.service.MasterServiceLocator;
import com.vone.medisafe.service.iface.master.item.ItemManager;
import com.vone.medisafe.ui.base.BaseController;

public class BatchItemController extends BaseController{

	Button btnSave;
	Button btnEdit;
	Button btnUpload;
	Listbox itemList;
	
	ItemManager manager = MasterServiceLocator.getItemManager();
	
	@Override
	public void init(Component cmp) throws InterruptedException,
			VONEAppException {
		
		super.init(cmp);
		btnSave = (Button)cmp.getFellow("btnSave");
		btnEdit = (Button)cmp.getFellow("btnEdit");
		btnUpload = (Button)cmp.getFellow("btnUpload");
		itemList = (Listbox)cmp.getFellow("itemList");
		
		manager.getItemObat(itemList);
		initButton();
	}
	
	public void initButton(){
		btnSave.setDisabled(true);
		btnEdit.setDisabled(false);
		btnUpload.setDisabled(false);
	}
	
	public void save() throws VONEAppException{
		manager.updateItemBatch(itemList);
		initButton();
	}
	
	public void upload() throws InterruptedException, IOException{
		Media media = Fileupload.get();
		Listitem item;
		Listcell cell;
		
		int counter = 1;
		if(media != null){
			if(media.getContentType().equalsIgnoreCase("text/csv")){
				BufferedReader br = new BufferedReader(media.getReaderData());
				String readLine = null;
				String[] contents = null;
				itemList.getItems().clear();
				while((readLine = br.readLine()) != null){
					if(counter > 1){
						contents = readLine.split(";");
						item = new Listitem();
						item.setParent(itemList);
						
						cell = new Listcell(contents[0]);
						cell.setParent(item);
						
						cell = new Listcell(contents[1]);
						cell.setParent(item);
						
						cell = new Listcell(contents[2]);
						cell.setParent(item);
						
						cell = new Listcell(contents[3]);
						cell.setParent(item);
						
						cell = new Listcell(contents[4]);
						cell.setParent(item);
						
						cell = new Listcell(contents[5]);
						cell.setParent(item);
					}
					counter = counter + 1;
				}
				
				btnSave.setDisabled(false);
			}
			else{
				Messagebox.show("Format File Tidak Valid...!", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
			
	}
	
	
	public void createFile(){
		String path = this.getCurrentSession().getWebApp().getRealPath("tmp")+"/batchItem.csv";
		File f = new File(path);
		StringBuffer sb = new StringBuffer();
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			List<Listitem> items = itemList.getItems();
			sb.append("KODE;NAMA ITEM;BUFFER;MAX ORDER;HRG BELI;HRG JUAL");
			sb.append("\n");
			for(Listitem item : items){
				sb.append(((Listcell)item.getChildren().get(0)).getLabel());
				sb.append(";");
				
				sb.append(((Listcell)item.getChildren().get(1)).getLabel());
				sb.append(";");
				
				sb.append(((Listcell)item.getChildren().get(2)).getLabel());
				sb.append(";");
				
				sb.append(((Listcell)item.getChildren().get(3)).getLabel());
				sb.append(";");
				
				sb.append(((Listcell)item.getChildren().get(4)).getLabel());
				sb.append(";");
				
				sb.append(((Listcell)item.getChildren().get(5)).getLabel());
				sb.append("\n");
			}
			out.write(sb.toString());
			sb = null;
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileInputStream in;
		try {
			in = new FileInputStream(path);
			Filedownload.save(in, "txt/html", "batchItem.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
