<script setup>
import { onMounted, ref } from 'vue'

const apiStatus = ref('checking')
const apiPayload = ref(null)

const checkBackend = async () => {
  try {
    const response = await fetch('/api/health')

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    apiPayload.value = await response.json()
    apiStatus.value = 'connected'
  } catch (error) {
    apiStatus.value = `failed (${error.message})`
  }
}

onMounted(() => {
  checkBackend()
})
</script>

<template>
  <main class="page">
    <section class="card">
      <p class="eyebrow">SIMRS Modernization</p>
      <h1>Vue Frontend Ready</h1>
      <p>Frontend sekarang terpisah dari aplikasi legacy dan terhubung ke API Spring Boot.</p>

      <div class="status-box">
        <p><strong>Backend status:</strong> {{ apiStatus }}</p>
        <pre v-if="apiPayload">{{ apiPayload }}</pre>
      </div>

      <button @click="checkBackend">Cek ulang koneksi API</button>
    </section>
  </main>
</template>
