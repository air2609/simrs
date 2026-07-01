package com.simrs.backend.admission.service;

import com.simrs.backend.admission.dto.AdmissionPatientSearchResponse;
import com.simrs.backend.admission.dto.AdmissionReferenceDataResponse;
import com.simrs.backend.admission.dto.AdmissionRegistrationRequest;
import com.simrs.backend.admission.dto.AdmissionRegistrationResponse;
import com.simrs.backend.admission.persistence.entity.OutpatientRegistrationEntity;
import com.simrs.backend.admission.persistence.entity.PatientEntity;
import com.simrs.backend.admission.persistence.repository.OutpatientRegistrationRepository;
import com.simrs.backend.admission.persistence.repository.PatientRepository;
import com.simrs.backend.audit.AuditLogService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdmissionService {

    private static final DateTimeFormatter REG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    private final AtomicLong registrationSequence = new AtomicLong(1L);
    private final AtomicLong mrSequence = new AtomicLong(100001L);

    private final PatientRepository patientRepository;
    private final OutpatientRegistrationRepository outpatientRegistrationRepository;
    private final AuditLogService auditLogService;

    public AdmissionService(
            PatientRepository patientRepository,
            OutpatientRegistrationRepository outpatientRegistrationRepository,
            AuditLogService auditLogService) {
        this.patientRepository = patientRepository;
        this.outpatientRegistrationRepository = outpatientRegistrationRepository;
        this.auditLogService = auditLogService;
    }

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

    @Transactional
    public AdmissionRegistrationResponse createRegistration(AdmissionRegistrationRequest request, String userId) {
        PatientEntity patient = resolvePatient(request);

        OutpatientRegistrationEntity registration = new OutpatientRegistrationEntity();
        registration.setRegistrationNumber(generateRegistrationNumber());
        registration.setMrNumber(patient.getMrNumber());
        registration.setPatientName(patient.getPatientName());
        registration.setNik(patient.getNik());
        registration.setUnit(request.getUnit());
        registration.setDoctor(request.getDoctor());
        registration.setPatientType(request.getPatientType());
        registration.setEthnicity(request.getEthnicity());
        registration.setLanguage(request.getLanguage());
        registration.setStatus("ACTIVE");
        registration.setRegisteredAt(LocalDateTime.now());
        outpatientRegistrationRepository.save(registration);

        auditLogService.log(
                "OUTPATIENT_REGISTER",
                "OUTPATIENT_REGISTRATION",
                registration.getRegistrationNumber(),
                userId,
                "create outpatient registration");

        return toResponse(registration);
    }

    public List<AdmissionRegistrationResponse> listRegistrations(String mrNumber, String status) {
        String normalizedMr = mrNumber == null ? "" : mrNumber.trim();
        String normalizedStatus = status == null ? "" : status.trim();

        List<OutpatientRegistrationEntity> values;
        if (!normalizedMr.isEmpty() && !normalizedStatus.isEmpty()) {
            values = outpatientRegistrationRepository
                    .findByMrNumberContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByRegisteredAtDesc(
                            normalizedMr,
                            normalizedStatus);
        } else if (!normalizedStatus.isEmpty()) {
            values = outpatientRegistrationRepository.findByStatusContainingIgnoreCaseOrderByRegisteredAtDesc(normalizedStatus);
        } else if (!normalizedMr.isEmpty()) {
            values = outpatientRegistrationRepository.findByMrNumberContainingIgnoreCaseOrderByRegisteredAtDesc(normalizedMr);
        } else {
            values = outpatientRegistrationRepository.findAll();
            values.sort((a, b) -> b.getRegisteredAt().compareTo(a.getRegisteredAt()));
        }

        return values.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdmissionRegistrationResponse cancelRegistration(String registrationNumber, String userId) {
        OutpatientRegistrationEntity registration = outpatientRegistrationRepository
                .findByRegistrationNumber(registrationNumber)
                .orElse(null);
        if (registration == null) {
            throw new IllegalArgumentException("Nomor registrasi tidak ditemukan");
        }

        registration.setStatus("CANCELLED");
        outpatientRegistrationRepository.save(registration);

        auditLogService.log(
                "OUTPATIENT_CANCEL",
                "OUTPATIENT_REGISTRATION",
                registrationNumber,
                userId,
                "cancel outpatient registration");

        return toResponse(registration);
    }

    public List<AdmissionPatientSearchResponse> searchPatients(String mrNumber, String nik, String patientName) {
        String mr = mrNumber == null ? "" : mrNumber.trim();
        String normalizedNik = nik == null ? "" : nik.trim();
        String name = patientName == null ? "" : patientName.trim();

        List<PatientEntity> result;
        if (!name.isEmpty()) {
            result = patientRepository.findByPatientNameContainingIgnoreCase(name);
        } else {
            result = patientRepository.findByMrNumberContainingIgnoreCaseAndNikContainingIgnoreCase(mr, normalizedNik);
        }

        return result.stream().map(this::toPatientSearchResponse).collect(Collectors.toList());
    }

    private PatientEntity resolvePatient(AdmissionRegistrationRequest request) {
        String mode = request.getPatientMode() == null ? "" : request.getPatientMode().trim();
        if ("PASIEN_LAMA".equalsIgnoreCase(mode)) {
            if (request.getMrNumber() == null || request.getMrNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("No MR wajib diisi untuk pasien lama");
            }
            String normalizedMr = request.getMrNumber().trim().toUpperCase();
            return patientRepository.findByMrNumber(normalizedMr)
                    .orElseThrow(() -> new IllegalArgumentException("Data pasien lama tidak ditemukan"));
        }

        String normalizedNik = request.getNik() == null ? "" : request.getNik().trim();
        PatientEntity existing = patientRepository.findByNik(normalizedNik).orElse(null);
        if (existing != null) {
            return existing;
        }

        PatientEntity patient = new PatientEntity();
        patient.setMrNumber(generateMrNumber());
        patient.setNik(normalizedNik);
        patient.setPatientName(request.getPatientName());
        patient.setGender(request.getGender());
        patient.setBirthDate(request.getBirthDate());
        patient.setAddress(request.getAddress());
        patient.setPhone(request.getPhone());
        patient.setCreatedAt(LocalDateTime.now());
        return patientRepository.save(patient);
    }

    private String generateMrNumber() {
        String mrNumber;
        do {
            mrNumber = "MR" + mrSequence.getAndIncrement();
        } while (patientRepository.findByMrNumber(mrNumber).isPresent());
        return mrNumber;
    }

    private String generateRegistrationNumber() {
        String registrationNumber;
        do {
            String datePart = LocalDateTime.now().format(REG_DATE_FORMAT);
            long sequence = registrationSequence.getAndIncrement();
            registrationNumber = "RJ" + datePart + "-" + String.format("%05d", sequence);
        } while (outpatientRegistrationRepository.existsByRegistrationNumber(registrationNumber));
        return registrationNumber;
    }

    private AdmissionRegistrationResponse toResponse(OutpatientRegistrationEntity registration) {
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

    private AdmissionPatientSearchResponse toPatientSearchResponse(PatientEntity patient) {
        AdmissionPatientSearchResponse response = new AdmissionPatientSearchResponse();
        response.setMrNumber(patient.getMrNumber());
        response.setNik(patient.getNik());
        response.setPatientName(patient.getPatientName());
        response.setGender(patient.getGender());
        response.setBirthDate(patient.getBirthDate());
        response.setAddress(patient.getAddress());
        response.setPhone(patient.getPhone());
        return response;
    }
}
