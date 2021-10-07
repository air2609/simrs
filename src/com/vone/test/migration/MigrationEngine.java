package com.vone.test.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.vone.medisafe.common.util.MedisafeUtil;
import com.vone.medisafe.mapping.MsPatient;
import com.vone.medisafe.mapping.TbMedicalRecord;

public class MigrationEngine {
	
	public static void main(String[] args) throws Exception {
		String directorySource = "D:/source/migrasi";
		String directoryMigrated = "D:/source/migrated";
		String fileName = null;
		File folder = new File(directorySource);
		File[] files = folder.listFiles();
		System.out.println("Program start");
		Connection con = MyDb.getConnection();
		for(int i=0; i < files.length; i++){
			fileName = files[i].getName();
			migratePatient(files[i],con);
			copyFile(directorySource+fileName, directoryMigrated+fileName);
			files[i].delete();
		}
		con.close();
		System.out.println("Program end");
	}
	
	public static void migratePatient(File fileSrc, Connection con){
		 FileInputStream fstream;
		 MsPatient patient;
		 TbMedicalRecord mr;
		try {
			fstream = new FileInputStream(fileSrc);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			String[] pasien;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while ((strLine = br.readLine()) != null){
//				  strLine.replace(oldChar, newChar)
				  pasien = strLine.split(";");
				  System.out.println(strLine);
//				  System.out.println(pasien.length);
				  
				  //fortmat : "000001";"MOH. SAHRONI";"2005-06-26";"IKAN GURAMI III / 55";"PRIA";
//				  System.out.println(pasien[2]);
				  patient = new MsPatient();
				  patient.setNPatientId(getPatientId("ms_patient_n_patient_id_seq",con));
				  patient.setVPatientAge((short)0);
				  patient.setVPatientName(pasien[1].substring(1, pasien[1].length()-1));
				  patient.setVPatientMainAddr(pasien[3].substring(1, pasien[3].length()-1));
				  patient.setDPatientDob(sdf.parse(pasien[2].substring(1,pasien[2].length()-1)));
				  if(pasien[4].substring(1, pasien[4].length()-1).equalsIgnoreCase("PRIA"))
					  patient.setVPatientGender("M");
				  else patient.setVPatientGender("F");
//				  if(pasien.length == 6) patient.setVPatientMotherName(pasien[5].substring(1, pasien[5].length()-1));
				  
				  mr = new TbMedicalRecord();
				  mr.setNMrId(getPatientId("tb_medical_record_n_mr_id_seq",con));
				  mr.setVMrCode(MedisafeUtil.convertToMrCode(pasien[0].substring(1, pasien[0].length()-1)));
				  mr.setMsPatient(patient);
				  
				  migrate(mr);
				  
			}
				 
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void migrate(TbMedicalRecord mr) {
		Connection con;
		PreparedStatement sql1;
		PreparedStatement sql2;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		con = MyDb.getConnection();
		try {
			sql1 = con.prepareStatement("INSERT INTO MS_PATIENT(n_patient_id,v_patient_name,v_patient_gender,d_patient_dob,v_patient_main_addr,v_patient_mother_name,v_patient_age) " +
					"VALUES(?,?,?,to_date(?,'dd/MM/YYYY'),?,?,?)");
			sql1.setInt(1, mr.getMsPatient().getNPatientId());
			sql1.setString(2, mr.getMsPatient().getVPatientName());
			sql1.setString(3, mr.getMsPatient().getVPatientGender());
			sql1.setString(4, sdf.format(mr.getMsPatient().getDPatientDob()));
			sql1.setString(5, mr.getMsPatient().getVPatientMainAddr());
//			if(mr.getMsPatient().getVPatientMotherName() != null)
//				sql1.setString(6, mr.getMsPatient().getVPatientMotherName());
//			else sql1.setString(6, null);
			sql1.setInt(7, mr.getMsPatient().getVPatientAge());
			
			
			sql2 = con.prepareStatement("INSERT INTO tb_medical_record(n_mr_id,n_patient_id,v_mr_code) VALUES(?,?,?)");
			sql2.setInt(1, mr.getNMrId());
			sql2.setInt(2, mr.getMsPatient().getNPatientId());
			sql2.setString(3, mr.getVMrCode());
			
			sql1.execute();
			sql2.execute();
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static Integer getPatientId(String sequenceName, Connection con) {
		Integer patId = null;
		PreparedStatement sql;
//		Connection con;
		ResultSet hasil;
		
		con = MyDb.getConnection();
		try {
			sql = con.prepareStatement("select nextval('"+sequenceName+"') as hasil");
			hasil = sql.executeQuery();
			while(hasil.next()){
				patId = hasil.getInt("hasil");
			}
//			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patId;
	}

	public static void copyFile(String fileSrc, String fileDestination){
		File f1 = new File(fileSrc);
		File f2 = new File(fileDestination);
		
		try {
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			 }
			 in.close();
			 out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
