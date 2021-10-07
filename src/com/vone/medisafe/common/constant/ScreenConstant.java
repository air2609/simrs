package com.vone.medisafe.common.constant;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: Jun 8, 2005
 * Time: 12:45:19 PM
 */
 
public interface ScreenConstant {

	//ADMISSION
	public static final String PENDAFTARAN_PASIEN = "SC0001";
	public static final String MUTASI_KAMAR = "SC0002";
	public static final String ANTRIAN_KAMAR = "SC0003";
	
	//PHARMACY
	public static final String TRANSAKSI_APOTIK = "SC0011";
	public static final String TAMBAH_ITEM_APOTIK = "SC0012";
	
	//CASHIER
	public static final String TRANSAKSI_KASIR = "SC0021";
	
	//WARD
	public static final String TRANSAKSI_BANGSAL = "SC0031";
	public static final String TRANSAKSI_BED = "SC0004";
	
	//LABORATORY
	public static final String TRANSAKSI_LAB = "SC0041";
	public static final String TAMBAH_TINDAKAN_LAB = "SC0042";
	public static final String HASIL_PEMERIKSAAN_LAB = "SC0043";
	
	//VK
	public static final String TRANSAKSI_VK ="SC0161";
	
	//RADIOLOGY
	public static final String TRANSAKSI_RADIOLOGY = "SC0051";
	public static final String TAMBAH_TINDAKAN_RADIOLOGY = "SC0052";
	
	//ER
	public static final String TRANSAKSI_UGD = "SC0061";
	
	//SURGERY
	public static final String TRANSAKSI_SURGERY = "SC0071";
	
    //MEDICAL RECORD
	public static final String BERKAS_REKAM_MEDIS = "SC0081";
	public static final String PERMINTAAN_BERKAS_REKAM_MEDIS = "SC0082";
	public static final String REKAM_MEDIS_DIAGNOSA = "SC0083";
	public static final String ICD = "SC0084";
	public static final String ICD_9_CM = "SC0085";	
	
	//POLYCLINIC
	public static final String TRANSAKSI_POLIKLINIK = "SC0091";
	
	//NUTRITION CLINIC
	public static final String TRANSAKSI_KLINIK_GIZI = "SC0101";
	public static final String TAMBAH_ITEM_KGZ = "SC0102";
	
	//CAFE DIET
	public static final String TRANSAKSI_CAFE_DIET = "SC0111";
	
	//DEBTOR
	public static final String TRANSAKSI_DEBITUR = "SC0131";
	
	//FISIOTERAPI
	public static final String TRANSAKSI_FISIOTERAPI = "SC0141";
	public static final String TAMBAH_TINDAKAN_FIS = "SC0142";	
	
	//RENAL UNIT
	public static final String TRANSAKSI_RENAL_UNIT = "SC0151";
	
	//COMMON
	public static final String HISTORY_PATIENT = "SC0171";
	public static final String TAMBAH_ITEM = "SC0172";
	public static final String TAMBAH_TINDAKAN = "SC0173";
	public static final String PERMINTAAN_ITEM = "SC0174";
	public static final String PINJAM_BERKAS_RM = "SC0175";
	
	/*//MASTER 
	public static final String USER_MASTER = "SC0032";
	public static final String USER_PRIVILEGE = "SC0033";
	public static final String GROUP_MASTER = "SC0034";
	public static final String GROUP_PRIVILEGE = "SC0035";*/
	
	//MASTER
	public static final String USER_MAINTENANCE = "SCM0001";
	
	public static final String GROUP_MASTER = "SCM0002";
	public static final String SCREEN_MASTER = "SCM0003";
	public static final String SUBSYSTEM_MASTER = "SCM0004";
	public static final String USER_PRIVILEGE = "SCM0005";
	public static final String GROUP_PRIVILEGE = "SCM0006";
	public static final String BRANCH_MASTER = "SCM0007";
	
	public static final String PATIENT_MASTER = "SCM0011";
	public static final String PATIENT_TYPE_MASTER = "SCM0012";
	public static final String PROVINCE_MASTER = "SCM0013";
	public static final String REGENCY_MASTER = "SCM0014";
	public static final String SUBDISTRICT_MASTER = "SCM0015";
	public static final String VILLAGE_MASTER = "SCM0016";
	public static final String WARD_MASTER = "SCM0017";
	public static final String HALL_MASTER = "SCM0018";
	public static final String ROOM_MASTER = "SCM0019";
	public static final String BED_MASTER = "SCM0020";
	public static final String TREATMENT_CLASS_MASTER = "SCM0021";
	public static final String DIVISION_MASTER = "SCM0022";
	public static final String TREATMENT_GROUP_MASTER = "SCM0023";
	public static final String UNIT_MASTER = "SCM0024";
	public static final String LABORATORIUM_TEST_MASTER = "SCM0025";
	public static final String TREATMENT_MASTER = "SCM0026";
	public static final String ICD_MASTER = "SCM0027";
	public static final String ICD9CM_MASTER = "SCM0028";
	public static final String CAUSE_OF_DEATH_MASTER = "SCM0029";
	public static final String DOCTOR_MASTER = "SCM0030";
	public static final String STAFF_MASTER = "SCM0031";	
	public static final String ITEM_INVENTORY = "SCM0032";
	public static final String BANK_MASTER = "SCM0033";	
	
	
	//PURCHASING	
	public static final String ORDER_PERMINTAAN_PEMBELIAN = "SC0191";
	public static final String ORDER_PEMBELIAN = "SC0193";
	public static final String DELIVERY_ORDER = "SC0195";
	
}
