# ARCHITECTUIRE — SIMRS (MEDiSafe)

## Gambaran Arsitektur

Aplikasi menggunakan arsitektur **Java web monolith modular**:
- UI berbasis **ZK** (`.zul`) dan JSP
- logic/controller di package `com.vone.medisafe.ui` dan modul domain lain
- business/service layer di package `service`, `services`, dan manager per modul
- persistence layer berbasis **Hibernate 3** dengan mapping `.hbm.xml`
- database utama **PostgreSQL**

## Layer Utama

### 1) Presentation Layer

- Entry web: `index.jsp`
- Routing UI ZK: `WEB-INF/web.xml`, `WEB-INF/zk.xml`
- Halaman modul: `/home/runner/work/simrs/simrs/zkpages/**`
- Controller UI: `/home/runner/work/simrs/simrs/src/com/vone/medisafe/ui/**`

### 2) Application / Service Layer

- Service locator dan service interface/implementasi pada:
  - `/home/runner/work/simrs/simrs/src/com/vone/medisafe/service`
  - `/home/runner/work/simrs/simrs/src/com/vone/medisafe/services`
- Menangani orkestrasi use case lintas domain (admission, treatment, purchase, dll)

### 3) Domain & Persistence Layer

- Entity, DAO, dan mapping Hibernate pada:
  - `/home/runner/work/simrs/simrs/src/com/vone/medisafe/mapping`
  - `/home/runner/work/simrs/simrs/src/com/vone/medisafe/mapping/hbm`
- Konfigurasi session factory, datasource, transaction manager di:
  - `/home/runner/work/simrs/simrs/src/applicationContext.xml`

### 4) Data Layer

- PostgreSQL sebagai datastore transaksi operasional rumah sakit

## Alur Request (sederhana)

1. User membuka halaman ZK/JSP.
2. Event UI diproses controller Java.
3. Controller memanggil service/manager.
4. Service mengakses DAO/entity Hibernate.
5. Hibernate berinteraksi dengan PostgreSQL.
6. Hasil dikembalikan ke UI dan dirender ke pengguna.

## Modul Domain (berdasarkan struktur package/zkpages)

- admin, master data, admission, antrian, apotik, cashier
- laboratorium, radiology, surgery, ward, emergency, renal, vk
- purchasing, warehouse, accounting, report
- satusehat integration

## Konfigurasi Runtime Penting

- `config/medisafe.properties` — parameter aplikasi (DB, path, timeout, dll)
- `config/log4j.properties` — logging
- `WEB-INF/web.xml` — servlet mapping dan web app config
- `WEB-INF/zk.xml` — konfigurasi ZK framework

## Catatan Arsitektural

- Sistem saat ini berorientasi monolitik dengan modularisasi per domain.
- Dependency dikelola via kumpulan JAR di `WEB-INF/lib`.
- Cocok untuk stabilitas operasional existing system, namun modernisasi bertahap dapat mempertimbangkan:
  - externalized configuration
  - dependency/build standardization
  - observability dan hardening keamanan konfigurasi
