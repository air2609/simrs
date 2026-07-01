const BASE_URL = '/api/admissions'

function resolveUserId() {
  return localStorage.getItem('simrsUserId') || 'developer'
}

async function parseResponse(response) {
  const payload = await response.json().catch(() => ({}))

  if (!response.ok) {
    const message = payload.message || `HTTP ${response.status}`
    throw new Error(message)
  }

  return payload
}

export async function getAdmissionReferences() {
  const response = await fetch(`${BASE_URL}/references`)
  return parseResponse(response)
}

export async function createAdmissionRegistration(form) {
  const response = await fetch(`${BASE_URL}/registrations`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': resolveUserId(),
    },
    body: JSON.stringify(form),
  })

  return parseResponse(response)
}

export async function getAdmissionRegistrations(filter = {}) {
  const search = new URLSearchParams()

  if (filter.mrNumber) {
    search.set('mrNumber', filter.mrNumber)
  }

  if (filter.status) {
    search.set('status', filter.status)
  }

  const response = await fetch(`${BASE_URL}/registrations?${search.toString()}`)
  return parseResponse(response)
}

export async function cancelAdmissionRegistration(registrationNumber) {
  const response = await fetch(`${BASE_URL}/registrations/${registrationNumber}/cancel`, {
    method: 'POST',
    headers: {
      'X-User-Id': resolveUserId(),
    },
  })

  return parseResponse(response)
}

export async function searchAdmissionPatients(filter = {}) {
  const search = new URLSearchParams()

  if (filter.mrNumber) {
    search.set('mrNumber', filter.mrNumber)
  }

  if (filter.nik) {
    search.set('nik', filter.nik)
  }

  if (filter.patientName) {
    search.set('patientName', filter.patientName)
  }

  const response = await fetch(`${BASE_URL}/patients/search?${search.toString()}`)
  return parseResponse(response)
}
