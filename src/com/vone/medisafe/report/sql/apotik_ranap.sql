-- Function: report.laporan_penjualan_ranap(startDate varchar, endDate varchar, unitName varchar, shift varchar)

-- DROP FUNCTION report.laporan_penjualan_ranap("varchar", "varchar", "varchar", "varchar");

CREATE OR REPLACE FUNCTION report.laporan_penjualan_ranap(startDate varchar, endDate varchar, unitName varchar, shift varchar)
  RETURNS SETOF report.laporan_ranap AS
$BODY$
declare nota record;
declare ranap_report report.laporan_ranap;
declare nomor int2;
declare nomor_bahan_medis int2;

declare nilai_sebelum_diskon float8;
declare nilai_setelah_diskon float8;
declare nilai_diskon float8;

declare nilai_sebelum_diskon_bahan_medis float8;
declare nilai_setelah_diskon_bahan_medis float8;
declare nilai_diskon_bahan_medis float8;

declare item record;
declare jumlah record;
declare bed_record record;

declare r_obat int4;
declare r_bahan_medis int4;

declare jumlah_total float8;
declare jumlah_total_akhir float8;
declare jumlah_diskon float8;


declare jumlah_total_bahan_medis float8;
declare jumlah_total_akhir_bahan_medis float8;
declare jumlah_diskon_bahan_medis float8;

begin
	nomor = 1;
	nomor_bahan_medis = 1;

	nilai_sebelum_diskon = 0;
	nilai_setelah_diskon = 0;
	nilai_diskon = 0;

	nilai_sebelum_diskon_bahan_medis = 0;
	nilai_setelah_diskon_bahan_medis = 0;
	nilai_diskon_bahan_medis = 0;

	jumlah_total = 0;
	jumlah_total_akhir = 0;
	jumlah_diskon = 0;

	jumlah_total_bahan_medis = 0;
	jumlah_total_akhir_bahan_medis = 0;
	jumlah_diskon_bahan_medis = 0;

	r_obat = 0;
	r_bahan_medis = 0;
	

	for nota in 
	select 
		exam.*, 
		pasien.v_patient_name as nama_pasien,
		reg.v_reg_secondary_id as nomor_registrasi,
		reg.n_reg_id as id_registrasi
		---ptype.v_tpatient as tipe_pasient

	from 
		tb_examination exam,
		ms_patient pasien,
		tb_registration reg
		--ms_patient_type ptype
	where 
		exam.n_patient_id=pasien.n_patient_id
		-- and pasien.n_patient_type_id=ptype.n_patient_type_id
		and exam.n_reg_id=reg.n_reg_id
		and exam.v_note_no like unitName --'I-APTK%' 
		and exam.n_exam_status='2'
		and exam.n_shift_id=shift 
		and exam.d_whn_create between startDate and endDate
	
	loop
		select into jumlah count(*) as banyak from (select * from report.hitung_banyak_jenis_item(nota.n_exam_id, 'OBAT') 		
					group by hitung_banyak_jenis_item) as a; 
		
		if jumlah.banyak > 0 then
			ranap_report.no_transaksi = nota.v_note_no;
			ranap_report.nomor = nomor;
			ranap_report.no_resep = nota.v_recipe_no;
			ranap_report.nama_pasien = nota.nama_pasien;
			ranap_report.no_registrasi = nota.nomor_registrasi;
				
				select into bed_record 
					bed.v_bed_desc, hall.v_hall_name 
				from 
					tb_bed_occupancy boc,
					ms_bed bed,
					ms_room room,
					ms_hall hall
				where 
					boc.n_reg_primary_id=nota.id_registrasi
					and boc.n_bed_primary_id=bed.n_bed_id
					and bed.n_room_id=room.n_room_id
					and room.n_hall_id= hall.n_hall_id
					order by boc.d_check_in_time limit 1;
			
			ranap_report.bed = bed_record.v_bed_desc;
			ranap_report.ruangan = bed_record.v_hall_name;
			ranap_report.r = jumlah.banyak;

			r_obat = r_obat + jumlah.banyak;
			--hitung nilai transaksi obat
			
			select into bed_record
				sum(trx.n_amount_trx) as sebelum, 
				sum(trx.n_amount_after_disc) as sesudah
			from 
				tb_item_trx trx, 
				ms_item itm, 
				ms_item_group grup 
			where 
				trx.n_note_id=nota.n_exam_id
				and trx.n_item_id=itm.n_item_id
				and itm.n_item_group_id=grup.n_item_group_id
				and grup.v_item_group_name='OBAT';
			
			
			nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(bed_record.sebelum);
			nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(bed_record.sesudah);
			nilai_diskon = nilai_diskon + (report.convert_null(bed_record.sebelum) - report.convert_null(bed_record.sesudah));

			select into item 
				sum(trx.n_amount_trx) as sebelum , 
				sum(trx.n_amount_after_disc) as sesudah 
			from 
				tb_drug_ingredients trx
			where 
				trx.n_note_id=nota.n_exam_id;
		
			nilai_sebelum_diskon = nilai_sebelum_diskon + report.convert_null(item.sebelum);
			nilai_setelah_diskon = nilai_setelah_diskon + report.convert_null(item.sesudah);
			nilai_diskon = nilai_diskon + (report.convert_null(item.sebelum) - report.convert_null(item.sesudah));

			ranap_report.total=nilai_sebelum_diskon;
			ranap_report.diskon=nilai_diskon;
			ranap_report.total_akhir=nilai_setelah_diskon;

			jumlah_total = jumlah_total + nilai_sebelum_diskon;
			jumlah_total_akhir = jumlah_total_akhir + nilai_setelah_diskon;
			jumlah_diskon = jumlah_diskon + nilai_diskon;

			select into item ptype.v_tpatient as tipe_pasien	
			from ms_patient pat, ms_patient_type ptype
			where pat.n_patient_id=nota.n_patient_id
			and pat.n_patient_type_id=ptype.n_patient_type_id;
			
			ranap_report.grup='OBAT';
			ranap_report.jns_pas=item.tipe_pasien;
			nomor = nomor + 1;
			return next ranap_report;
			
		end if;


		select into jumlah count(*) as banyak from (select * from report.hitung_banyak_jenis_item(nota.n_exam_id, 'BAHAN MEDIS') 		
					group by hitung_banyak_jenis_item) as a;
		
		if jumlah.banyak > 0 then
			ranap_report.no_transaksi = nota.v_note_no;
			ranap_report.nomor = nomor;
			ranap_report.no_resep = nota.v_recipe_no;
			ranap_report.nama_pasien = nota.nama_pasien;
			ranap_report.no_registrasi = nota.nomor_registrasi;
				
				select into bed_record 
					bed.v_bed_desc, hall.v_hall_name 
				from 
					tb_bed_occupancy boc,
					ms_bed bed,
					ms_room room,
					ms_hall hall
				where 
					boc.n_reg_primary_id=nota.id_registrasi
					and boc.n_bed_primary_id=bed.n_bed_id
					and bed.n_room_id=room.n_room_id
					and room.n_hall_id= hall.n_hall_id
					order by boc.d_check_in_time desc limit 1;
			
			ranap_report.bed = bed_record.v_bed_desc;
			ranap_report.ruangan = bed_record.v_hall_name;
			ranap_report.r = jumlah.banyak;
			
			r_bahan_medis = r_bahan_medis + jumlah.banyak;
			--hitung nilai transaksi bahan medis
			
			select into bed_record
				sum(trx.n_amount_trx) as sebelum, 
				sum(trx.n_amount_after_disc) as sesudah
			from 
				tb_item_trx trx, 
				ms_item itm, 
				ms_item_group grup 
			where 
				trx.n_note_id=nota.n_exam_id
				and trx.n_item_id=itm.n_item_id
				and itm.n_item_group_id=grup.n_item_group_id
				and grup.v_item_group_name='BAHAN MEDIS';
			
			
			nilai_sebelum_diskon_bahan_medis = nilai_sebelum_diskon_bahan_medis + report.convert_null(bed_record.sebelum);
			nilai_setelah_diskon_bahan_medis = nilai_setelah_diskon_bahan_medis + report.convert_null(bed_record.sesudah);
			nilai_diskon_bahan_medis = nilai_diskon_bahan_medis + 
						(report.convert_null(bed_record.sebelum) - report.convert_null(bed_record.sesudah));

			
			jumlah_total_bahan_medis = jumlah_total_bahan_medis + nilai_sebelum_diskon_bahan_medis;
			jumlah_total_akhir_bahan_medis = jumlah_total_akhir_bahan_medis + nilai_setelah_diskon_bahan_medis;
			jumlah_diskon_bahan_medis = jumlah_diskon_bahan_medis + nilai_diskon_bahan_medis;

			ranap_report.total=nilai_sebelum_diskon_bahan_medis;
			ranap_report.diskon=nilai_diskon_bahan_medis;
			ranap_report.total_akhir=nilai_setelah_diskon_bahan_medis;

			select into item ptype.v_tpatient as tipe_pasien	
			from ms_patient pat, ms_patient_type ptype
			where pat.n_patient_id=nota.n_patient_id
			and pat.n_patient_type_id=ptype.n_patient_type_id;
			
			ranap_report.grup='BAHAN MEDIS';
			ranap_report.jns_pas=item.tipe_pasien;
			nomor_bahan_medis = nomor_bahan_medis + 1;
			return next ranap_report;
			
		end if;
			
		
			
		

	end loop;
	
	if jumlah_total > 0 then
		ranap_report.no_transaksi = null;
		ranap_report.nomor = null;
		ranap_report.no_resep = null;
		ranap_report.nama_pasien = null;
		ranap_report.no_registrasi = null;
		ranap_report.ruangan = null;
		ranap_report.r = r_obat;
		ranap_report.bed='TOTAL';
		ranap_report.total=jumlah_total;
		ranap_report.diskon=jumlah_diskon;
		ranap_report.total_akhir=jumlah_total_akhir;
		ranap_report.grup='OBAT';
		ranap_report.jns_pas= null;
		return next ranap_report;
	end if;

	if jumlah_total_bahan_medis > 0 then
		ranap_report.no_transaksi = null;
		ranap_report.nomor = null;
		ranap_report.no_resep = null;
		ranap_report.nama_pasien = null;
		ranap_report.no_registrasi = null;
		ranap_report.ruangan = null;
		ranap_report.r = r_bahan_medis;
		ranap_report.bed='TOTAL';
		ranap_report.total=jumlah_total_bahan_medis;
		ranap_report.diskon=jumlah_diskon_bahan_medis;
		ranap_report.total_akhir=jumlah_total_akhir_bahan_medis;
		ranap_report.grup='BAHAN MEDIS';
		ranap_report.jns_pas= null;
		return next ranap_report;
	end if;
	

end$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION report.laporan_penjualan_ranap(varchar, varchar, varchar, varchar) OWNER TO postgres;

select * from report.laporan_penjualan_ranap('2007-02-01 00:00:00', '2007-02-21 23:59:59', 'I-APTK%', '2')
