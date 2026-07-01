<script setup>
import { onMounted, ref } from 'vue'
import {
  cancelAdmissionRegistration,
  createAdmissionRegistration,
  getAdmissionReferences,
  getAdmissionRegistrations,
} from './api/admission'

const apiStatus = ref('checking')
const activeTab = ref('rawat-jalan')
const loading = ref(false)
const submitMessage = ref('')
const references = ref({
  patientModes: [],
  patientTypes: [],
  units: [],
  doctors: [],
  ethnicities: [],
  languages: [],
})
const registrations = ref([])

const form = ref({
  patientMode: 'PASIEN_BARU',
  mrNumber: '',
  nik: '',
  patientName: '',
  gender: 'M',
  birthDate: '',
  address: '',
  unit: '',
  doctor: '',
  patientType: '',
  ethnicity: '',
  language: '',
  phone: '',
})

const checkBackend = async () => {
  try {
    const response = await fetch('/api/health')

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    await response.json()
    apiStatus.value = 'connected'
  } catch (error) {
    apiStatus.value = `failed (${error.message})`
  }
}

const loadReferences = async () => {
  const data = await getAdmissionReferences()
  references.value = data
  form.value.patientType = data.patientTypes[0] || ''
  form.value.unit = data.units[0] || ''
  form.value.doctor = data.doctors[0] || ''
  form.value.ethnicity = data.ethnicities[0] || ''
  form.value.language = data.languages[0] || ''
}

const loadRegistrations = async () => {
  registrations.value = await getAdmissionRegistrations({ status: 'ACTIVE' })
}

const resetForm = () => {
  form.value = {
    patientMode: 'PASIEN_BARU',
    mrNumber: '',
    nik: '',
    patientName: '',
    gender: 'M',
    birthDate: '',
    address: '',
    unit: references.value.units[0] || '',
    doctor: references.value.doctors[0] || '',
    patientType: references.value.patientTypes[0] || '',
    ethnicity: references.value.ethnicities[0] || '',
    language: references.value.languages[0] || '',
    phone: '',
  }
}

const submitRegistration = async () => {
  submitMessage.value = ''
  loading.value = true

  try {
    const payload = { ...form.value }
    const result = await createAdmissionRegistration(payload)
    submitMessage.value = `Registrasi berhasil: ${result.registrationNumber} / ${result.mrNumber}`
    await loadRegistrations()
    resetForm()
  } catch (error) {
    submitMessage.value = `Gagal simpan registrasi: ${error.message}`
  } finally {
    loading.value = false
  }
}

const cancelRegistration = async (registrationNumber) => {
  loading.value = true
  submitMessage.value = ''

  try {
    await cancelAdmissionRegistration(registrationNumber)
    submitMessage.value = `Registrasi ${registrationNumber} berhasil dibatalkan`
    await loadRegistrations()
  } catch (error) {
    submitMessage.value = `Gagal membatalkan registrasi: ${error.message}`
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  Promise.all([checkBackend(), loadReferences(), loadRegistrations()]).catch(() => {
    submitMessage.value = 'Tidak dapat memuat data admisi. Pastikan backend berjalan.'
  })
})
</script>

<template>
  <main class="page">
    <section class="card">
      <p class="eyebrow">SIMRS Modernization</p>
      <h1>Modul Admisi - Migrasi Tahap 1</h1>
      <p>Scope saat ini: pendaftaran rawat jalan, daftar registrasi aktif, dan batal registrasi.</p>

      <div class="status-box">
        <p><strong>Backend status:</strong> {{ apiStatus }}</p>
      </div>

      <div class="tab-row">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'rawat-jalan' }"
          @click="activeTab = 'rawat-jalan'"
        >
          Pasien Rawat Jalan
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'rawat-inap' }"
          @click="activeTab = 'rawat-inap'"
        >
          Pasien Rawat Inap
        </button>
      </div>

      <div v-if="activeTab === 'rawat-jalan'" class="panel">
        <h2>Form Pendaftaran Pasien Rawat Jalan</h2>

        <div class="form-grid">
          <label>
            Mode Pasien
            <select v-model="form.patientMode">
              <option v-for="mode in references.patientModes" :key="mode" :value="mode">{{ mode }}</option>
            </select>
          </label>

          <label>
            No. MR (wajib untuk pasien lama)
            <input v-model="form.mrNumber" type="text" placeholder="MR100123" />
          </label>

          <label>
            NIK
            <input v-model="form.nik" type="text" placeholder="Nomor KTP/NIK" />
          </label>

          <label>
            Nama Pasien
            <input v-model="form.patientName" type="text" placeholder="Nama lengkap" />
          </label>

          <label>
            Jenis Kelamin
            <select v-model="form.gender">
              <option value="M">PRIA</option>
              <option value="F">WANITA</option>
            </select>
          </label>

          <label>
            Tanggal Lahir
            <input v-model="form.birthDate" type="date" />
          </label>

          <label class="wide">
            Alamat
            <input v-model="form.address" type="text" placeholder="Alamat utama pasien" />
          </label>

          <label>
            Unit
            <select v-model="form.unit">
              <option v-for="unit in references.units" :key="unit" :value="unit">{{ unit }}</option>
            </select>
          </label>

          <label>
            Dokter Pemeriksa
            <select v-model="form.doctor">
              <option v-for="doctor in references.doctors" :key="doctor" :value="doctor">{{ doctor }}</option>
            </select>
          </label>

          <label>
            Tipe Pasien
            <select v-model="form.patientType">
              <option v-for="type in references.patientTypes" :key="type" :value="type">{{ type }}</option>
            </select>
          </label>

          <label>
            Etnis/Suku
            <select v-model="form.ethnicity">
              <option v-for="ethnicity in references.ethnicities" :key="ethnicity" :value="ethnicity">{{ ethnicity }}</option>
            </select>
          </label>

          <label>
            Bahasa
            <select v-model="form.language">
              <option v-for="language in references.languages" :key="language" :value="language">{{ language }}</option>
            </select>
          </label>

          <label>
            No Telp/HP
            <input v-model="form.phone" type="text" placeholder="08xxxx" />
          </label>
        </div>

        <div class="button-row">
          <button :disabled="loading" @click="submitRegistration">Simpan Registrasi</button>
          <button class="ghost" :disabled="loading" @click="loadRegistrations">Refresh Daftar Aktif</button>
        </div>

        <p v-if="submitMessage" class="message">{{ submitMessage }}</p>

        <h3>Registrasi Aktif</h3>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>No Registrasi</th>
                <th>No MR</th>
                <th>Nama</th>
                <th>Unit</th>
                <th>Dokter</th>
                <th>Status</th>
                <th>Aksi</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in registrations" :key="item.registrationNumber">
                <td>{{ item.registrationNumber }}</td>
                <td>{{ item.mrNumber }}</td>
                <td>{{ item.patientName }}</td>
                <td>{{ item.unit }}</td>
                <td>{{ item.doctor }}</td>
                <td>{{ item.status }}</td>
                <td>
                  <button class="danger" :disabled="loading" @click="cancelRegistration(item.registrationNumber)">
                    Batal
                  </button>
                </td>
              </tr>
              <tr v-if="registrations.length === 0">
                <td colspan="7" class="empty">Belum ada registrasi aktif</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-else class="panel">
        <h2>Rawat Inap</h2>
        <p>
          Tahap berikutnya: migrasi alur rawat inap (booking kamar, antrian kamar, mutasi kamar)
          dari modul legacy ke API + UI baru.
        </p>
      </div>
    </section>
  </main>
</template>
