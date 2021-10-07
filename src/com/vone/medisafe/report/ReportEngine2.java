package com.vone.medisafe.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ConfigurationManager;
import com.vone.medisafe.common.services.ReportService;
import com.vone.medisafe.common.util.MedisafeConstants;

public class ReportEngine2{
	
	private File fileConfig;
	private static final String stFile = ConfigurationManager.getKey("jasper.configuration.path");//"/home/arifullah/apache-tomcat-7.0.57/webapps/medisafe/config/jasperConf.xml";//ConfigurationManager.getKey("jasper.configuration.path");
	
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ReportEngine2.class); 

	JasperPrint jasperPrint;
	JasperDesign jasperDesign;
	JasperReport jasperReport;
	JRResultSetDataSource jasperDatasource;

	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	
    public void loadConfig() throws JDOMException, IOException
    {
    	fileConfig = new File(stFile);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(fileConfig); //lama
        Element rootElement = doc.getRootElement();

        List beanChildren = rootElement.getChildren("bean");
        Iterator beanIterator = beanChildren.iterator();
        while (beanIterator.hasNext()) {
            Element beanElement = (Element)beanIterator.next();
            String beanName = beanElement.getAttributeValue("id");
            
            if (beanName.equals("DataSource")){
                List propertyChildren = beanElement.getChildren("property");
                Iterator propIterator = propertyChildren.iterator();
                while (propIterator.hasNext()) {
                    Element propertyElement = (Element)propIterator.next();
                    String propertyName = propertyElement.getAttributeValue("name");
                    String propertyValue = propertyElement.getChildText("value");
                    
                    propertyMap.put(propertyName, propertyValue);
                }
            }
        }
    }
    
	public ReportEngine2(String query, String pathDesain) throws VONEAppException{
		initReport(query, pathDesain, new HashMap());
	}
	public ReportEngine2() {
	
	}
	public ReportEngine2(String query, String pathDesain, Map map)throws VONEAppException{
		initReport(query, pathDesain, map);
	}
	
	private void initReport(String query, String pathDesain,  Map map) throws VONEAppException{
		Connection con = null;
		Statement stm = null;
		ResultSet result = null;
		InputStream input;
		try {
			loadConfig();
			String driverClass = propertyMap.get("driverClassName");
			String url  = propertyMap.get("url");
			String username  = propertyMap.get("username");
			String pass = propertyMap.get("password");
			
			Class.forName(driverClass).newInstance();
//			System.out.println("url: " + url);
//			System.out.println("user: " + username);
//			System.out.println("pass: " + pass);
			con = DriverManager.getConnection(url, username, pass);

			stm = con.createStatement();
			result = stm.executeQuery(query);
			jasperDatasource = new JRResultSetDataSource(result);
//			System.out.println(pathDesain);
			input = new FileInputStream(new File(pathDesain));
//			jasperDesign = JRXmlLoader.load(input);
			jasperReport = (JasperReport)JRLoader.loadObject(input);
			jasperPrint = JasperFillManager.fillReport(jasperReport, map, jasperDatasource);

		} catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new VONEAppException(e.getMessage(), e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
//				e.printStackTrace();
				throw new VONEAppException(e.getMessage(),e);
			}
		}
	}
	public boolean printOut(String ipTarget){
		boolean success = false;
		Socket socket;
		try {
			socket = new Socket(ipTarget, MedisafeConstants.PORT_JASPER);
	        ObjectOutputStream os = new ObjectOutputStream(
	        		socket.getOutputStream( ));
//	        		 Construct and write the Object
//	        JRViewer jrViewer = new JRViewer(jasperPrint);
//
//			JFrame viewer = new JFrame();
//			viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//			
//			viewer.setTitle("PREVIEW LAPORAN"); 
//			viewer.getContentPane().add(jrViewer); 
			
    		os.writeObject(jasperPrint);
//			os.writeObject(jrViewer);
    		os.close( );
    		success = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	public void openPdf(){
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			
			AMedia media = new AMedia("myreport", "pdf", "application/pdf", output.toByteArray());
			
			final Window win = (Window) Executions.createComponents(
					"/zkpages/laboratory/preview.zul", null, null);
			
			Iframe myIframe = (Iframe)win.getFellow("myIframe");
			myIframe.setContent(media);
			win.doModal();
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void openXls(){
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			// coding For Excel:
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,output);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
//			exporterXLS.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
			exporterXLS.exportReport();
			
			AMedia media = new AMedia("myreport", "xls", "application/xls", output.toByteArray());
			final Window win;
			win = (Window) Executions.createComponents(
					"/zkpages/laboratory/preview.zul", null, null);
			
			Iframe myIframe = (Iframe)win.getFellow("myIframe");
			myIframe.setContent(media);
			//win.doOverlapped();
			//win.doEmbedded();
			//win.doPopup();
			win.doModal();
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void savePdf(String pathOutput) throws VONEAppException{
		try {
			OutputStream output = new FileOutputStream(new File(ReportService.getKey("pdfOutput") + pathOutput + ".pdf"));
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			output.close();
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
	}

	public void createReport(String query, String pathDesain, String pathOutput) throws JDOMException, IOException, VONEAppException {
		initReport(query, pathDesain, new HashMap());
		savePdf(pathOutput);
	}
	
	public static void main(String[] args) {
//		long awal = System.currentTimeMillis();
//		System.out.println("start..");
//		String query;
//		
//		query = "select " +
//			" lr.v_lab_rslt_code, " +
//			" tgroup.v_tgroup_name,   " +
//			" treat.v_treatment_name,  " +
//			" lrd.v_lab_rslt_desc, " +
//			" lrd.v_lab_rslt_quantify, " +
//			" lrd.v_nrml_range_man, " +
//			" lrd.v_nrml_range_woman " +
//
//			"from " +
//			" ms_treatment_group tgroup, " +
//			" ms_treatment treat, " +
//			" tb_laboratory_result_detail lrd, " +
//			" tb_laboratory_result lr " +
//	
//			"where " +
//			" tgroup.n_tgroup_id = treat.n_tgroup_id " +
//			" and treat.n_treatment_id = lrd.n_treatment_id " +
//			" and lrd.n_lab_rslt_id = lr.n_lab_rslt_id " +
//			" and lr.v_lab_rslt_code like '%J-LA%' " +
//	
//			"order by " +
//			" lr.v_lab_rslt_code, tgroup.v_tgroup_name,  v_treatment_name; ";
//
//		Map<String, String> parameters = new HashMap<String, String>();
//		parameters.put("nama", "COBAA-COBA");
//		parameters.put("dokter", "");
//		parameters.put("alamat", "");
//		parameters.put("tanggal", "");
//		parameters.put("umur", "25");
//		parameters.put("rawat", "");
//		parameters.put("noRM", "");
//		parameters.put("register", "");
//		parameters.put("noHasil", "");
//		parameters.put("ruangBed", "");
//		parameters.put("jamPengambilan", "");
//		parameters.put("jamDikerjakan", "");
//		parameters.put("noLaborat", "");
//		parameters.put("keterangan", "");
//		parameters.put("drPengirim", "");
//		
//		ReportEngine re = new ReportEngine(query,"C:/tomcat5/webapps/medisafe/jasper/labResult.jrxml", parameters);
//		re.savePdf("d:\\test.pdf");
//		long akhir = System.currentTimeMillis();
//		//double d = ()/1000;
//		System.out.println("finish.. " + (akhir - awal) + " ms");
		
//		String query = "select * from report.func_gl(35,1)";
//		Map<String, String> parameters = new HashMap<String, String>();
//		String url = "C:/tomcat5/webapps/medisafe/jasper/general_ledger.jrxml";
		
//		ReportEngine re = new ReportEngine(query, url, parameters);
//		re.savePdf("d:\\home\\kun\\test.pdf");

	}

	public void openPdf(String string) throws VONEAppException{
		if(string == null)
			return;
		try {
			String pdfFile = ReportService.getKey("pdfOutput") + string + ".pdf";
			
			InputStream is = new FileInputStream(pdfFile);
			
			AMedia media = new AMedia("myreport", "pdf", "application/pdf", is);
			final Window win = (Window) Executions.createComponents(
					"/zkpages/laboratory/preview.zul", null, null);
			
			Iframe myIframe = (Iframe)win.getFellow("myIframe");
			myIframe.setContent(media);
			win.doModal();
//			is.close();
			
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new VONEAppException(e.getMessage(), e);
		}
	}

}
