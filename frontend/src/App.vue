<script setup>
import { onMounted, ref } from 'vue'
import {
  cancelAdmissionRegistration,
  createAdmissionRegistration,
  getAdmissionReferences,
  getAdmissionRegistrations,
  searchAdmissionPatients,
} from './api/admission'
import {
  cancelInpatientBooking,
  confirmInpatientBooking,
  createInpatientBooking,
  createInpatientMutation,
  getInpatientBookings,
  getInpatientMutations,
  getInpatientQueue,
  getInpatientReferences,
} from './api/inpatient'

const apiStatus = ref('checking')
const activeTab = ref('rawat-jalan')
const loading = ref(false)
const submitMessage = ref('')
const userId = ref('developer')
const references = ref({
  patientModes: [],
  patientTypes: [],
  units: [],
  doctors: [],
  ethnicities: [],
  languages: [],
})
const registrations = ref([])
const patientSearchFilter = ref({
  mrNumber: '',
  nik: '',
  patientName: '',
})
const patientSearchResult = ref([])
const inpatientReferences = ref({
  classes: [],
  halls: [],
  beds: [],
  doctors: [],
})
const inpatientMessage = ref('')
const inpatientBookings = ref([])
const inpatientQueue = ref([])
const inpatientMutations = ref([])

const inpatientForm = ref({
  mrNumber: '',
  patientName: '',
  previousRegistrationNumber: '',
  targetClass: '',
  hall: '',
  bed: '',
  mainDoctor: '',
})

const mutationForm = ref({
  registrationNumber: '',
  mrNumber: '',
  patientName: '',
  fromBed: '',
  toBed: '',
})

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

const loadInpatientReferences = async () => {
  const data = await getInpatientReferences()
  inpatientReferences.value = data
  inpatientForm.value.targetClass = data.classes[0] || ''
  inpatientForm.value.hall = data.halls[0] || ''
  inpatientForm.value.bed = data.beds[0] || ''
  inpatientForm.value.mainDoctor = data.doctors[0] || ''
  mutationForm.value.fromBed = data.beds[0] || ''
  mutationForm.value.toBed = data.beds[1] || data.beds[0] || ''
}

const loadInpatientLists = async () => {
  inpatientBookings.value = await getInpatientBookings('ACTIVE')
  inpatientQueue.value = await getInpatientQueue()
  inpatientMutations.value = await getInpatientMutations()
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

const searchPatients = async () => {
  loading.value = true
  submitMessage.value = ''

  try {
    patientSearchResult.value = await searchAdmissionPatients({ ...patientSearchFilter.value })
    if (patientSearchResult.value.length === 0) {
      submitMessage.value = 'Data pasien tidak ditemukan'
    }
  } catch (error) {
    submitMessage.value = `Gagal mencari pasien: ${error.message}`
  } finally {
    loading.value = false
  }
}

const usePatientFromSearch = (patient) => {
  form.value.patientMode = 'PASIEN_LAMA'
  form.value.mrNumber = patient.mrNumber || ''
  form.value.nik = patient.nik || ''
  form.value.patientName = patient.patientName || ''
  form.value.gender = patient.gender || 'M'
  form.value.birthDate = patient.birthDate || ''
  form.value.address = patient.address || ''
  form.value.phone = patient.phone || ''
}

const submitInpatientBooking = async () => {
  loading.value = true
  inpatientMessage.value = ''

  try {
    const result = await createInpatientBooking({ ...inpatientForm.value })
    inpatientMessage.value = `Booking berhasil: ${result.bookingNumber} / ${result.registrationNumber}`
    mutationForm.value.registrationNumber = result.registrationNumber
    mutationForm.value.mrNumber = result.mrNumber
    mutationForm.value.patientName = result.patientName
    mutationForm.value.fromBed = result.bed
    await loadInpatientLists()
  } catch (error) {
    inpatientMessage.value = `Booking rawat inap gagal: ${error.message}`
  } finally {
    loading.value = false
  }
}

const confirmBooking = async (bookingNumber) => {
  loading.value = true
  inpatientMessage.value = ''

  try {
    await confirmInpatientBooking(bookingNumber)
    inpatientMessage.value = `Booking ${bookingNumber} berhasil dikonfirmasi`
    await loadInpatientLists()
  } catch (error) {
    inpatientMessage.value = `Konfirmasi booking gagal: ${error.message}`
  } finally {
    loading.value = false
  }
}

const cancelBooking = async (bookingNumber) => {
  loading.value = true
  inpatientMessage.value = ''

  try {
    await cancelInpatientBooking(bookingNumber)
    inpatientMessage.value = `Booking ${bookingNumber} berhasil dibatalkan`
    await loadInpatientLists()
  } catch (error) {
    inpatientMessage.value = `Batal booking gagal: ${error.message}`
  } finally {
    loading.value = false
  }
}

const submitMutation = async () => {
  loading.value = true
  inpatientMessage.value = ''

  try {
    const result = await createInpatientMutation({ ...mutationForm.value })
    inpatientMessage.value = `Mutasi berhasil: ${result.mutationNumber}`
    mutationForm.value.fromBed = result.toBed
    await loadInpatientLists()
  } catch (error) {
    inpatientMessage.value = `Mutasi kamar gagal: ${error.message}`
  } finally {
    loading.value = false
  }
}

const saveUserId = () => {
  const normalized = (userId.value || '').trim() || 'developer'
  userId.value = normalized
  localStorage.setItem('simrsUserId', normalized)
}

onMounted(() => {
  userId.value = localStorage.getItem('simrsUserId') || 'developer'
  Promise.all([
    checkBackend(),
    loadReferences(),
    loadRegistrations(),
    loadInpatientReferences(),
    loadInpatientLists(),
  ]).catch(() => {
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

      <div class="user-row">
        <label>
          User ID untuk aksi sensitif
          <input v-model="userId" type="text" @change="saveUserId" />
        </label>
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

        <div class="sub-panel">
          <h3>Cari Pasien Lama</h3>
          <div class="form-grid">
            <label>
              No MR
              <input v-model="patientSearchFilter.mrNumber" type="text" placeholder="MR..." />
            </label>

            <label>
              NIK
              <input v-model="patientSearchFilter.nik" type="text" placeholder="NIK" />
            </label>

            <label class="wide">
              Nama Pasien
              <input v-model="patientSearchFilter.patientName" type="text" placeholder="Nama pasien" />
            </label>
          </div>

          <div class="button-row">
            <button :disabled="loading" @click="searchPatients">Cari Pasien</button>
          </div>

          <div class="table-wrap" v-if="patientSearchResult.length > 0">
            <table>
              <thead>
                <tr>
                  <th>No MR</th>
                  <th>NIK</th>
                  <th>Nama</th>
                  <th>Tgl Lahir</th>
                  <th>Aksi</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="patient in patientSearchResult" :key="patient.mrNumber">
                  <td>{{ patient.mrNumber }}</td>
                  <td>{{ patient.nik }}</td>
                  <td>{{ patient.patientName }}</td>
                  <td>{{ patient.birthDate }}</td>
                  <td>
                    <button :disabled="loading" @click="usePatientFromSearch(patient)">Pilih</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

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

        <div class="sub-panel">
          <h3>Booking Kamar</h3>
          <div class="form-grid">
            <label>
              No. MR
              <input v-model="inpatientForm.mrNumber" type="text" placeholder="MR100123" />
            </label>

            <label>
              Nama Pasien
              <input v-model="inpatientForm.patientName" type="text" />
            </label>

            <label>
              No. Registrasi Lama
              <input v-model="inpatientForm.previousRegistrationNumber" type="text" placeholder="RJ..." />
            </label>

            <label>
              Kelas Tarif
              <select v-model="inpatientForm.targetClass">
                <option v-for="item in inpatientReferences.classes" :key="item" :value="item">{{ item }}</option>
              </select>
            </label>

            <label>
              Ruangan
              <select v-model="inpatientForm.hall">
                <option v-for="item in inpatientReferences.halls" :key="item" :value="item">{{ item }}</option>
              </select>
            </label>

            <label>
              Bed
              <select v-model="inpatientForm.bed">
                <option v-for="item in inpatientReferences.beds" :key="item" :value="item">{{ item }}</option>
              </select>
            </label>

            <label>
              Dokter Utama
              <select v-model="inpatientForm.mainDoctor">
                <option v-for="item in inpatientReferences.doctors" :key="item" :value="item">{{ item }}</option>
              </select>
            </label>
          </div>

          <div class="button-row">
            <button :disabled="loading" @click="submitInpatientBooking">Simpan Booking</button>
            <button class="ghost" :disabled="loading" @click="loadInpatientLists">Refresh Data Ranap</button>
          </div>
        </div>

        <div class="sub-panel">
          <h3>Mutasi Kamar</h3>
          <div class="form-grid">
            <label>
              No. Registrasi
              <input v-model="mutationForm.registrationNumber" type="text" placeholder="RI..." />
            </label>

            <label>
              No. MR
              <input v-model="mutationForm.mrNumber" type="text" />
            </label>

            <label>
              Nama Pasien
              <input v-model="mutationForm.patientName" type="text" />
            </label>

            <label>
              Bed Asal
              <select v-model="mutationForm.fromBed">
                <option v-for="item in inpatientReferences.beds" :key="`from-${item}`" :value="item">{{ item }}</option>
              </select>
            </label>

            <label>
              Bed Tujuan
              <select v-model="mutationForm.toBed">
                <option v-for="item in inpatientReferences.beds" :key="`to-${item}`" :value="item">{{ item }}</option>
              </select>
            </label>
          </div>

          <div class="button-row">
            <button :disabled="loading" @click="submitMutation">Simpan Mutasi</button>
          </div>
        </div>

        <p v-if="inpatientMessage" class="message">{{ inpatientMessage }}</p>

        <h3>Antrian Kamar (QUEUED)</h3>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>No Booking</th>
                <th>No Registrasi</th>
                <th>No MR</th>
                <th>Nama</th>
                <th>Kelas</th>
                <th>Ruangan</th>
                <th>Bed</th>
                <th>Aksi</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in inpatientQueue" :key="item.bookingNumber">
                <td>{{ item.bookingNumber }}</td>
                <td>{{ item.registrationNumber }}</td>
                <td>{{ item.mrNumber }}</td>
                <td>{{ item.patientName }}</td>
                <td>{{ item.targetClass }}</td>
                <td>{{ item.hall }}</td>
                <td>{{ item.bed }}</td>
                <td>
                  <div class="action-inline">
                    <button :disabled="loading" @click="confirmBooking(item.bookingNumber)">Konfirmasi</button>
                    <button class="danger" :disabled="loading" @click="cancelBooking(item.bookingNumber)">Batal</button>
                  </div>
                </td>
              </tr>
              <tr v-if="inpatientQueue.length === 0">
                <td colspan="8" class="empty">Belum ada antrian kamar</td>
              </tr>
            </tbody>
          </table>
        </div>

        <h3>Rawat Inap Aktif</h3>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>No Registrasi</th>
                <th>No MR</th>
                <th>Nama</th>
                <th>Ruangan</th>
                <th>Bed</th>
                <th>Dokter</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in inpatientBookings" :key="`active-${item.bookingNumber}`">
                <td>{{ item.registrationNumber }}</td>
                <td>{{ item.mrNumber }}</td>
                <td>{{ item.patientName }}</td>
                <td>{{ item.hall }}</td>
                <td>{{ item.bed }}</td>
                <td>{{ item.mainDoctor }}</td>
                <td>{{ item.status }}</td>
              </tr>
              <tr v-if="inpatientBookings.length === 0">
                <td colspan="7" class="empty">Belum ada rawat inap aktif</td>
              </tr>
            </tbody>
          </table>
        </div>

        <h3>Riwayat Mutasi Kamar</h3>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>No Mutasi</th>
                <th>No Registrasi</th>
                <th>No MR</th>
                <th>Nama</th>
                <th>Bed Asal</th>
                <th>Bed Tujuan</th>
                <th>Waktu</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in inpatientMutations" :key="item.mutationNumber">
                <td>{{ item.mutationNumber }}</td>
                <td>{{ item.registrationNumber }}</td>
                <td>{{ item.mrNumber }}</td>
                <td>{{ item.patientName }}</td>
                <td>{{ item.fromBed }}</td>
                <td>{{ item.toBed }}</td>
                <td>{{ item.mutatedAt }}</td>
              </tr>
              <tr v-if="inpatientMutations.length === 0">
                <td colspan="7" class="empty">Belum ada riwayat mutasi</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  </main>
</template>
