package com.vone.test.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vone.medisafe.common.util.MedisafeConstants;
import com.vone.medisafe.mapping.MsCoa;
import com.vone.medisafe.mapping.MsTreatment;
import com.vone.medisafe.mapping.MsTreatmentClass;
import com.vone.medisafe.mapping.MsTreatmentFee;
import com.vone.medisafe.mapping.MsTreatmentGroup;

public class TreatmentMigration {
	
	public static void main(String[] args) throws Exception{
		String directorySource = "/home/arif/Dropbox/Public/migrasi_alirsyad/source/";
		String directoryMigrated = "/home/arif/Dropbox/Public/migrasi_alirsyad/migrated/";
		String fileName = null;
		File folder = new File(directorySource);
		File[] files = folder.listFiles();
		Connection con = MyDb.getConnection();
		
		
		System.out.println("Program start");
		
		for(int i=0; i < files.length; i++){
			fileName = files[i].getName();
			createTreatment(files[i],con);
			MigrationEngine.copyFile(directorySource+fileName, directoryMigrated+fileName);
			//files[i].delete();
		}
		System.out.println("Program end");
		con.close();
	}

	private static void createTreatment(File fileSrc, Connection con) throws Exception{
		 FileInputStream fstream = new FileInputStream(fileSrc);
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 


		 
		 String strLine;
		 String[] tindakan;
		 MsTreatment treatment;
		 MsTreatmentFee fee;
		 String[] coa;
		 String coaCode;
		 
		 Double biayaObat;
		 Double biayaRs;
		 Double biayaJasaDokter;
		 Double biayaMedis;
		 Double biayaTotal;
		 
		 while ((strLine = br.readLine()) != null){
			 tindakan = strLine.split(";");
			 System.out.println(strLine);
			 
			 treatment = new MsTreatment();
			 treatment.setNTreatmentId(MigrationEngine.getPatientId("ms_treatment_n_treatment_id_seq", con));
			 treatment.setVTreatmentCode(tindakan[0].substring(1, tindakan[0].length()-1));
			 treatment.setVTreatmentName(tindakan[1].substring(1, tindakan[1].length()-1));
			 
			 //this is for nonpacket group only (group id equal 9)
			 MsTreatmentGroup group = new MsTreatmentGroup();
			 group.setNTgroupId(9);
			 treatment.setMsTreatmentGroup(group);
			 
			 fee = new MsTreatmentFee();
			 fee.setNTreatmentFeeId(MigrationEngine.getPatientId("ms_treatment_fee_n_treatment_fee_id_seq",con));
			 fee.setMsTreatment(treatment);
			 fee.setMsTreatmentClass(getClassTarif(tindakan[2].substring(1, tindakan[2].length()-1),con));
			 
			 coa = tindakan[8].substring(1, tindakan[8].length()-1).split("-");
			 coaCode = coa[0].trim();
			 
			 fee.setMsCoa(getCoa(coaCode,con));
			 
			 biayaObat = new Double(tindakan[3].substring(1,tindakan[3].length()-1));
			 biayaRs = new Double(tindakan[4].substring(1,tindakan[4].length()-1));
			 biayaJasaDokter = new Double(tindakan[5].substring(1,tindakan[5].length()-1));
			 biayaMedis = new Double(tindakan[6].substring(1,tindakan[6].length()-1));
			 
			 fee.setNRsFee(biayaObat+biayaRs);
			 fee.setNDoctorFee(biayaJasaDokter);
			 fee.setNMedicFee(biayaMedis);
			 biayaTotal = biayaJasaDokter+biayaMedis+biayaObat+biayaRs;
			 fee.setNTrtfeeFee(biayaTotal);
			 
			 addTreatment(fee,con);
			 
		 }
		
	}

	private static void addTreatment(MsTreatmentFee fee, Connection con) throws Exception{
		
		PreparedStatement sql = con.prepareStatement("insert into ms_treatment(n_treatment_id,n_tgroup_id,v_treatment_code,v_treatment_name)" +
				" values(?,?,?,?)");
		sql.setInt(1, fee.getMsTreatment().getNTreatmentId());
		sql.setInt(2, fee.getMsTreatment().getMsTreatmentGroup().getNTgroupId());
		sql.setString(3, fee.getMsTreatment().getVTreatmentCode());
		sql.setString(4, fee.getMsTreatment().getVTreatmentName());
		
		sql.execute();
		
		sql = con.prepareStatement("insert into ms_treatment_fee(n_tclass_id,n_treatment_id,n_trtfee_fee,n_treatment_fee_id,n_coa," +
				"n_doctor_fee,n_medic_fee,n_rs_fee) values(?,?,?,?,?,?,?,?)");
		sql.setInt(1, fee.getMsTreatmentClass().getNTclassId());
		sql.setInt(2, fee.getMsTreatment().getNTreatmentId());
		sql.setDouble(3, fee.getNTrtfeeFee());
		sql.setInt(4, fee.getNTreatmentFeeId());
		sql.setInt(5, fee.getMsCoa().getNCoaId());
		sql.setDouble(6, fee.getNDoctorFee());
		sql.setDouble(7, fee.getNMedicFee());
		sql.setDouble(8, fee.getNRsFee());
		
		sql.execute();
		
	}

	private static MsCoa getCoa(String coaCode, Connection con) throws Exception {
		MsCoa coa = null;
		PreparedStatement sql = con.prepareStatement("select * from ms_coa where v_acct_no=?");
		sql.setString(1, coaCode);
		
		ResultSet hasil = sql.executeQuery();
		while(hasil.next()){
			coa = new MsCoa();
			coa.setNCoaId(hasil.getInt("n_coa_id"));
			coa.setVAcctNo(coaCode);
			coa.setVAcctName(hasil.getString("v_acct_name"));
		}
		return coa;
	}

	private static MsTreatmentClass getClassTarif(String code, Connection con) throws Exception {
		MsTreatmentClass tclass = null;
		
		PreparedStatement sql = con.prepareStatement("select * from ms_treatment_class where v_tclass_code=?");
		sql.setString(1, code);
		
		ResultSet hasil = sql.executeQuery();
		while(hasil.next()){
			tclass = new MsTreatmentClass();
			tclass.setNTclassId(hasil.getInt("n_tclass_id"));
			tclass.setVTclassCode(hasil.getString("v_tclass_code"));
			tclass.setVTclassDesc(hasil.getString("v_tclass_desc"));
		}
		return tclass;
	}
	


}
