# SIMRS Split Architecture Setup

Dokumen ini menjelaskan setup awal pemisahan frontend dan backend.

## Struktur baru

- `frontend/` : Vue.js (Vite)
- `backend/` : Spring Boot (Maven)

## Menjalankan backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend berjalan di `http://localhost:8080`.

Endpoint uji:

- `GET /api/health`

Endpoint modul admisi (tahap migrasi awal):

- `GET /api/admissions/references`
- `POST /api/admissions/registrations`
- `GET /api/admissions/registrations?status=ACTIVE`
- `POST /api/admissions/registrations/{registrationNumber}/cancel`

Endpoint modul admisi rawat inap (tahap migrasi lanjutan):

- `GET /api/admissions/inpatient/references`
- `POST /api/admissions/inpatient/bookings`
- `GET /api/admissions/inpatient/bookings?status=ACTIVE`
- `GET /api/admissions/inpatient/queues`
- `POST /api/admissions/inpatient/bookings/{bookingNumber}/confirm`
- `POST /api/admissions/inpatient/bookings/{bookingNumber}/cancel`
- `POST /api/admissions/inpatient/mutations`
- `GET /api/admissions/inpatient/mutations`

## Menjalankan frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend berjalan di `http://localhost:5173`.

Vite sudah dipasang proxy:

- `/api/*` -> `http://localhost:8080`

## Cakupan migrasi admisi saat ini

- Frontend: tab `Pasien Rawat Jalan` sudah terhubung API baru.
- Backend: create/list/cancel registrasi rawat jalan dengan penyimpanan sementara in-memory.
- Frontend: tab `Pasien Rawat Inap` sudah mendukung booking kamar, antrian, konfirmasi/batal, dan mutasi kamar.
- Backend: alur ranap tersebut sudah tersedia dengan penyimpanan sementara in-memory.

## Validasi build

Frontend:

```bash
cd frontend
npm run build
```

Backend:

```bash
cd backend
./mvnw test
```

## Catatan kompatibilitas

Environment saat ini memakai Java 8, jadi backend diset ke Spring Boot `2.7.x` agar kompatibel.
