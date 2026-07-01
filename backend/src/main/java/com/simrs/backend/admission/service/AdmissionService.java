package com.simrs.backend.admission.service;

import com.simrs.backend.admission.dto.AdmissionReferenceDataResponse;
import com.simrs.backend.admission.dto.AdmissionRegistrationRequest;
import com.simrs.backend.admission.dto.AdmissionRegistrationResponse;
import com.simrs.backend.admission.model.AdmissionRegistration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AdmissionService {

    private static final DateTimeFormatter REG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    private final AtomicLong registrationSequence = new AtomicLong(1L);
    private final AtomicLong mrSequence = new AtomicLong(100001L);

    private final Map<String, AdmissionRegistration> registrations = new ConcurrentHashMap<String, AdmissionRegistration>();
    private final Map<String, String> nikToMr = new ConcurrentHashMap<String, String>();

    public AdmissionReferenceDataResponse getReferenceData() {
        AdmissionReferenceDataResponse response = new AdmissionReferenceDataResponse();
        response.setPatientModes(Arrays.asList("PASIEN_BARU", "PASIEN_LAMA"));
        response.setPatientTypes(Arrays.asList("UMUM", "BPJS", "ASURANSI"));
        response.setUnits(Arrays.asList("POLI UMUM", "POLI ANAK", "POLI PENYAKIT DALAM", "POLI BEDAH"));
        response.setDoctors(Arrays.asList("dr. Andi", "dr. Sari", "dr. Budi", "dr. Nia"));
        response.setEthnicities(Arrays.asList("JAWA", "SUNDA", "MINANG", "BATAK", "LAINNYA"));
        response.setLanguages(Arrays.asList("BAHASA INDONESIA", "BAHASA DAERAH", "BAHASA INGGRIS"));
        return response;
    }

    public AdmissionRegistrationResponse createRegistration(AdmissionRegistrationRequest request) {
        String mrNumber = resolveMrNumber(request);

        AdmissionRegistration registration = new AdmissionRegistration();
        registration.setRegistrationNumber(generateRegistrationNumber());
        registration.setMrNumber(mrNumber);
        registration.setPatientName(request.getPatientName());
        registration.setNik(request.getNik());
        registration.setGender(request.getGender());
        registration.setBirthDate(request.getBirthDate());
        registration.setAddress(request.getAddress());
        registration.setUnit(request.getUnit());
        registration.setDoctor(request.getDoctor());
        registration.setPatientType(request.getPatientType());
        registration.setEthnicity(request.getEthnicity());
        registration.setLanguage(request.getLanguage());
        registration.setPhone(request.getPhone());
        registration.setStatus("ACTIVE");
        registration.setRegisteredAt(LocalDateTime.now());

        registrations.put(registration.getRegistrationNumber(), registration);

        return toResponse(registration);
    }

    public List<AdmissionRegistrationResponse> listRegistrations(String mrNumber, String status) {
        List<AdmissionRegistration> values = new ArrayList<AdmissionRegistration>(registrations.values());
        Collections.sort(values, (a, b) -> b.getRegisteredAt().compareTo(a.getRegisteredAt()));

        return values.stream()
                .filter(item -> mrNumber == null || mrNumber.trim().isEmpty() || item.getMrNumber().equalsIgnoreCase(mrNumber.trim()))
                .filter(item -> status == null || status.trim().isEmpty() || item.getStatus().equalsIgnoreCase(status.trim()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AdmissionRegistrationResponse cancelRegistration(String registrationNumber) {
        AdmissionRegistration registration = registrations.get(registrationNumber);
        if (registration == null) {
            throw new IllegalArgumentException("Nomor registrasi tidak ditemukan");
        }

        registration.setStatus("CANCELLED");
        return toResponse(registration);
    }

    private String resolveMrNumber(AdmissionRegistrationRequest request) {
        String mode = request.getPatientMode() == null ? "" : request.getPatientMode().trim();
        if ("PASIEN_LAMA".equalsIgnoreCase(mode)) {
            if (request.getMrNumber() == null || request.getMrNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("No MR wajib diisi untuk pasien lama");
            }
            return request.getMrNumber().trim().toUpperCase();
        }

        String normalizedNik = request.getNik() == null ? "" : request.getNik().trim();
        if (nikToMr.containsKey(normalizedNik)) {
            return nikToMr.get(normalizedNik);
        }

        String mrNumber = generateMrNumber();
        nikToMr.put(normalizedNik, mrNumber);
        return mrNumber;
    }

    private String generateMrNumber() {
        return "MR" + mrSequence.getAndIncrement();
    }

    private String generateRegistrationNumber() {
        String datePart = LocalDateTime.now().format(REG_DATE_FORMAT);
        long sequence = registrationSequence.getAndIncrement();
        return "RJ" + datePart + "-" + String.format("%05d", sequence);
    }

    private AdmissionRegistrationResponse toResponse(AdmissionRegistration registration) {
        AdmissionRegistrationResponse response = new AdmissionRegistrationResponse();
        response.setRegistrationNumber(registration.getRegistrationNumber());
        response.setMrNumber(registration.getMrNumber());
        response.setPatientName(registration.getPatientName());
        response.setNik(registration.getNik());
        response.setUnit(registration.getUnit());
        response.setDoctor(registration.getDoctor());
        response.setStatus(registration.getStatus());
        response.setRegisteredAt(registration.getRegisteredAt().toString());
        return response;
    }
}
