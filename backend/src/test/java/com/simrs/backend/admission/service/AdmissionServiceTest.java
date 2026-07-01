package com.simrs.backend.admission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.simrs.backend.admission.dto.AdmissionRegistrationRequest;
import com.simrs.backend.admission.dto.AdmissionRegistrationResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdmissionServiceTest {

    private AdmissionService admissionService;

    @BeforeEach
    void setUp() {
        admissionService = new AdmissionService();
    }

    @Test
    void createRegistrationForNewPatientShouldGenerateMrAndRegistrationNumber() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse response = admissionService.createRegistration(request);

        assertTrue(response.getMrNumber().startsWith("MR"));
        assertTrue(response.getRegistrationNumber().startsWith("RJ"));
        assertEquals("ACTIVE", response.getStatus());
    }

    @Test
    void cancelRegistrationShouldUpdateStatus() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse created = admissionService.createRegistration(request);
        AdmissionRegistrationResponse cancelled = admissionService.cancelRegistration(created.getRegistrationNumber());

        assertEquals("CANCELLED", cancelled.getStatus());
    }

    @Test
    void listRegistrationWithStatusShouldFilterActiveOnly() {
        AdmissionRegistrationRequest request = sampleRequest();
        request.setPatientMode("PASIEN_BARU");

        AdmissionRegistrationResponse created = admissionService.createRegistration(request);
        admissionService.cancelRegistration(created.getRegistrationNumber());

        List<AdmissionRegistrationResponse> active = admissionService.listRegistrations(null, "ACTIVE");
        assertEquals(0, active.size());
    }

    private AdmissionRegistrationRequest sampleRequest() {
        AdmissionRegistrationRequest request = new AdmissionRegistrationRequest();
        request.setNik("1701010101010001");
        request.setPatientName("BUDI TEST");
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
