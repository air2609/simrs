const BASE_URL = '/api/admissions/inpatient'

function resolveUserId() {
  return localStorage.getItem('simrsUserId') || 'developer'
}

async function parseResponse(response) {
  const payload = await response.json().catch(() => ({}))

  if (!response.ok) {
    throw new Error(payload.message || `HTTP ${response.status}`)
  }

  return payload
}

export async function getInpatientReferences() {
  const response = await fetch(`${BASE_URL}/references`)
  return parseResponse(response)
}

export async function createInpatientBooking(payload) {
  const response = await fetch(`${BASE_URL}/bookings`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': resolveUserId(),
    },
    body: JSON.stringify(payload),
  })
  return parseResponse(response)
}

export async function getInpatientBookings(status) {
  const search = new URLSearchParams()
  if (status) {
    search.set('status', status)
  }
  const response = await fetch(`${BASE_URL}/bookings?${search.toString()}`)
  return parseResponse(response)
}

export async function getInpatientQueue(hall) {
  const search = new URLSearchParams()
  if (hall) {
    search.set('hall', hall)
  }
  const response = await fetch(`${BASE_URL}/queues?${search.toString()}`)
  return parseResponse(response)
}

export async function confirmInpatientBooking(bookingNumber) {
  const response = await fetch(`${BASE_URL}/bookings/${bookingNumber}/confirm`, {
    method: 'POST',
    headers: {
      'X-User-Id': resolveUserId(),
    },
  })
  return parseResponse(response)
}

export async function cancelInpatientBooking(bookingNumber) {
  const response = await fetch(`${BASE_URL}/bookings/${bookingNumber}/cancel`, {
    method: 'POST',
    headers: {
      'X-User-Id': resolveUserId(),
    },
  })
  return parseResponse(response)
}

export async function createInpatientMutation(payload) {
  const response = await fetch(`${BASE_URL}/mutations`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': resolveUserId(),
    },
    body: JSON.stringify(payload),
  })
  return parseResponse(response)
}

export async function getInpatientMutations(registrationNumber) {
  const search = new URLSearchParams()
  if (registrationNumber) {
    search.set('registrationNumber', registrationNumber)
  }
  const response = await fetch(`${BASE_URL}/mutations?${search.toString()}`)
  return parseResponse(response)
}
