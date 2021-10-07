CREATE TYPE report.laporan_ranap AS
   (nomor int2,
    no_transaksi varchar(20),
    no_resep varchar(10),
    no_registrasi varchar(18),
    nama_pasien varchar(50),
    bed varchar(15),
    ruangan varchar(20),
    r int2,
    total float8,
    diskon float8,
    total_akhir float8,
    jns_pas int2,
    grup varchar(20));
ALTER TYPE report.laporan_ranap OWNER TO postgres;

select * from tb_examination limit 5


select 
		exam.*, 
		pasien.v_patient_name as nama_pasien,
		reg.v_reg_secondary_id as nomor_registrasi,
		bed.
	from 
		tb_examination exam,
		ms_patient pasien,
		tb_registration reg
	where 
		exam.n_patient_id=pasien.n_patient_id
		and exam.n_reg_id=reg.n_reg_id

limit 5