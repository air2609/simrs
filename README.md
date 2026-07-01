# SIMRS (MEDiSafe)

Aplikasi **Sistem Informasi Manajemen Rumah Sakit (SIMRS)** berbasis Java web, dengan antarmuka ZK Framework dan penyimpanan data PostgreSQL.

## Ringkasan

Repository ini berisi source code aplikasi operasional rumah sakit, mencakup:
- pendaftaran pasien dan rawat inap
- antrian & integrasi BPJS
- farmasi, gudang, purchasing, kasir
- laboratorium, radiologi, bedah, fisioterapi, VK, renal
- rekam medis, pelaporan, dan akuntansi
- integrasi SATUSEHAT (modul tertentu)

## Teknologi Utama

- Java (legacy enterprise style)
- Servlet/JSP + ZK (`.zul`)
- Spring (XML configuration)
- Hibernate 3 (`.hbm.xml`)
- PostgreSQL
- Tomcat deployment style (terlihat dari konfigurasi)

## Struktur Proyek (ringkas)

- `/home/runner/work/simrs/simrs/src/com/vone/medisafe` — kode Java utama per domain/modul
- `/home/runner/work/simrs/simrs/zkpages` — halaman UI ZK per modul
- `/home/runner/work/simrs/simrs/WEB-INF` — konfigurasi web (`web.xml`, `zk.xml`) dan library
- `/home/runner/work/simrs/simrs/config` — file konfigurasi aplikasi (database, logging, jasper, dll)
- `/home/runner/work/simrs/simrs/script` — skrip SQL tambahan

## Cara Menjalankan (gambaran umum)

1. Siapkan PostgreSQL dan database SIMRS.
2. Sesuaikan konfigurasi koneksi database pada file konfigurasi aplikasi.
3. Deploy aplikasi ke servlet container (umumnya Tomcat) sebagai webapp.
4. Akses halaman awal (`index.jsp`) untuk menuju login ZK (`zkpages/login.zul`).

> Catatan: repository ini tidak menyediakan entrypoint build modern (mis. `pom.xml`/`gradle`) di root, sehingga proses build/deploy mengikuti pola proyek legacy yang sudah ada di lingkungan operasional.

## Konfigurasi Penting

- `/home/runner/work/simrs/simrs/config/medisafe.properties`
- `/home/runner/work/simrs/simrs/src/applicationContext.xml`
- `/home/runner/work/simrs/simrs/WEB-INF/web.xml`
- `/home/runner/work/simrs/simrs/WEB-INF/zk.xml`

## Keamanan Konfigurasi

Beberapa file konfigurasi di repository berisi contoh kredensial/path lokal. Untuk penggunaan riil:
- pindahkan kredensial ke secret manager atau environment variable
- hindari commit password/kunci baru ke repository
- gunakan file konfigurasi environment-specific di luar source control jika memungkinkan
