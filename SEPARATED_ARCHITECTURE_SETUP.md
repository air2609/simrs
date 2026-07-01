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
- Tab `Pasien Rawat Inap` disiapkan sebagai placeholder untuk tahap migrasi berikutnya.

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
