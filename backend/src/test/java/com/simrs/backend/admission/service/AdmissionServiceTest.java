package com.simrs.backend.admission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.simrs.backend.admission.dto.AdmissionRegistrationRequest;
import com.simrs.backend.admission.dto.AdmissionRegistrationResponse;
import com.simrs.backend.admission.persistence.repository.OutpatientRegistrationRepository;
import com.simrs.backend.admission.persistence.repository.PatientRepository;
import com.simrs.backend.audit.AuditLogRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdmissionServiceTest {

    @Autowired
    private AdmissionService admissionService;

    @Autowired
    private OutpatientRegistrationRepository outpatientRegistrationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        outpatientRegistrationRepository.deleteAll();
        patientRepository.deleteAll();
        auditLogRepository.deleteAll();
    }

    @Test
    void createRegistrationForNewPatientShouldGenerateMrAndRegistrationNumber() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse response = admissionService.createRegistration(request, "tester");

        assertTrue(response.getMrNumber().startsWith("MR"));
        assertTrue(response.getRegistrationNumber().startsWith("RJ"));
        assertEquals("ACTIVE", response.getStatus());
    }

    @Test
    void cancelRegistrationShouldUpdateStatus() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse created = admissionService.createRegistration(request, "tester");
        AdmissionRegistrationResponse cancelled = admissionService.cancelRegistration(created.getRegistrationNumber(), "tester");

        assertEquals("CANCELLED", cancelled.getStatus());
    }

    @Test
    void listRegistrationWithStatusShouldFilterActiveOnly() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse created = admissionService.createRegistration(request, "tester");
        admissionService.cancelRegistration(created.getRegistrationNumber(), "tester");

        List<AdmissionRegistrationResponse> active = admissionService.listRegistrations(null, "ACTIVE");
        assertEquals(0, active.size());
    }

    private AdmissionRegistrationRequest sampleRequest() {
        String suffix = String.valueOf(System.nanoTime());
        AdmissionRegistrationRequest request = new AdmissionRegistrationRequest();
        request.setNik("1701010101" + suffix.substring(Math.max(0, suffix.length() - 6)));
        request.setPatientName("BUDI TEST " + suffix);
        request.setGender("M");
        request.setBirthDate("1995-08-17");
        request.setAddress("Jl. Test No. 1");
        request.setUnit("POLI UMUM");
        request.setDoctor("dr. Andi");
        request.setPatientType("UMUM");
        request.setEthnicity("JAWA");
        request.setLanguage("BAHASA INDONESIA");
        request.setPhone("08123456789");
        return request;
    }
}
