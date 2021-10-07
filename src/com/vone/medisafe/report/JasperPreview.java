package com.vone.medisafe.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;


public class JasperPreview {

	private static final String REPORT_PATH = "D:\\Tomcat5\\webapps\\medisafe2\\jasper\\";
	private static final String VIEWER_TITLE = "Print preview";
	
	private static final String driverClass = "org.postgresql.Driver";
	private static final String url = "jdbc:postgresql://192.168.0.78:5432/medisafe_testing";
	private static final String username = "postgres";
	private static final String pass = "visioneserver";
	
	private JasperDesign jrDesign;
	private JasperReport jrReport;
	private JasperPrint jrPrint;
	private JRViewer jrViewer;
	private JRResultSetDataSource jrDatasource;
	
	private Connection con;
	private Statement statement;
	private ResultSet resultset;
	
	private Map<String, String> parameters;
	
	private String reportName;
	private String sqlQuery;
	
	private JFrame viewer;
	
	public JasperPreview(String report, String sql) {
		long awal = System.currentTimeMillis();
		System.out.println("begin..");
		this.reportName = report;
		this.sqlQuery = sql;
		try {
			createResultset();
			createReport();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		openViewer(); 
		System.out.println("end..");
		long akhir = System.currentTimeMillis();
		System.out.println((akhir - awal) + " ms");
	}

	public JasperPreview(JasperPrint inputReader) {
		// todo Auto-generated constructor stub
		jrPrint = inputReader;
		openViewer();
	}

	private void createResultset() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName(driverClass).newInstance();
		con = DriverManager.getConnection(url, username, pass);
		statement = con.createStatement();
		resultset = statement.executeQuery(sqlQuery);
	}

	private void createReport() throws JRException {
		parameters = new HashMap<String, String>();
		parameters.put("nama", "TEST");
		parameters.put("dokter", "");
		parameters.put("alamat", "ALAMATNYA");
		parameters.put("tanggal", "");
		parameters.put("umur", "30 TH");
		parameters.put("rawat", "");
		parameters.put("noRM", "");
		parameters.put("register", "");
		parameters.put("noHasil", "");
		parameters.put("ruangBed", "");
		parameters.put("jamPengambilan", "");
		parameters.put("jamDikerjakan", "");
		parameters.put("noLaborat", "");
		parameters.put("keterangan", "");
		parameters.put("drPengirim", "DR. DOBB");
		
		parameters.put("noRekamMedik", "DR. DOBB");
		parameters.put("noRegister", "DR. DOBB");
		parameters.put("namaPasien", "DR. DOBB");
		parameters.put("alamatPasien", "DR. DOBB");
		
		
		jrDesign = JRXmlLoader.load(REPORT_PATH + reportName);
		jrReport = JasperCompileManager.compileReport(jrDesign);
		jrDatasource = new JRResultSetDataSource(resultset);
//        jrPrint = JasperFillManager.fillReport(jrReport, parameters, jrDatasource);
        
        //kirimSocket("localhost");

	}

//	private void kirimSocket(String ipTarget) {
//		Socket socket;
//		try {
//			socket = new Socket(ipTarget, PrintServerJasper.PORT);
//	        ObjectOutputStream os = new ObjectOutputStream(
//	        		socket.getOutputStream( ));
////	        		 Construct and write the Object
//    		os.writeObject(jrPrint);
//    		os.close( );
//		} catch (UnknownHostException e) {
//			// todo Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// todo Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void openViewer() {
		jrViewer = new JRViewer(jrPrint);

		viewer = new JFrame();
		viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		viewer.setTitle(VIEWER_TITLE); 
		viewer.getContentPane().add(jrViewer); 
		viewer.pack(); 
		viewer.setSize(800,600);
		viewer.setResizable(true);
		viewer.setVisible(true);
	}

	public static void main(String[] args) {
		//new JasperPreview("general_ledger.jrxml","select * from report.func_gl(1)");
		//new JasperPreview("general_ledger.jrxml","select * from report.func_gl_all()");
		new JasperPreview("apotik_ranap.jrxml","select * from report.laporan_penjualan_ranap('2007-02-01 00:00:00', '2007-02-21 23:59:59', 'I-APTK%', '2')");
	}
	
}
