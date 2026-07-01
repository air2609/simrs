# Product Context — SIMRS (MEDiSafe)

## Tujuan Produk

SIMRS ini dirancang untuk mendukung proses end-to-end operasional rumah sakit dalam satu sistem terintegrasi, dari registrasi pasien sampai billing, farmasi, dan pelaporan.

## Masalah Bisnis yang Diselesaikan

- Fragmentasi data antar unit (pendaftaran, klinik, lab, farmasi, kasir).
- Keterlambatan alur layanan akibat proses manual.
- Sulitnya pelacakan transaksi klinis dan finansial secara konsisten.
- Kebutuhan kepatuhan pelaporan dan integrasi eksternal (contoh: BPJS, SATUSEHAT).

## Pengguna Utama

- Front office/pendaftaran
- Dokter/perawat/unit klinis
- Petugas laboratorium/radiologi
- Apoteker & petugas gudang
- Kasir & keuangan/akuntansi
- Admin sistem & manajemen rumah sakit

## Ruang Lingkup Fungsional

1. **Administrasi Pasien**
   - registrasi, rawat inap, mutasi kamar, data master pasien
2. **Pelayanan Klinis**
   - pemeriksaan poli, tindakan, rekam medis, diagnosis
3. **Penunjang Medis**
   - laboratorium, radiologi, bedah, fisioterapi, VK, renal
4. **Farmasi & Inventori**
   - item master, stok, mutasi, pembelian, retur
5. **Keuangan**
   - billing pasien, deposit, settlement, jurnal/akuntansi
6. **Pelaporan**
   - laporan operasional dan administratif
7. **Integrasi Eksternal**
   - modul antrian/BPJS dan SATUSEHAT (sesuai implementasi saat ini)

## Nilai Produk

- Satu sumber data lintas unit layanan
- Meningkatkan kecepatan dan konsistensi proses operasional
- Mendukung keputusan manajemen melalui data transaksi dan laporan
- Mempermudah integrasi regulasi/ekosistem kesehatan nasional

## Batasan yang Terlihat dari Kode Saat Ini

- Arsitektur legacy (konfigurasi XML-heavy)
- Ketergantungan pada deployment style lama (Tomcat + konfigurasi path lokal)
- Mekanisme build/deploy belum distandarkan pada tooling modern di root repository
