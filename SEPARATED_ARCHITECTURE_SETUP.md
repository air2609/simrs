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

## Menjalankan frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend berjalan di `http://localhost:5173`.

Vite sudah dipasang proxy:

- `/api/*` -> `http://localhost:8080`

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
