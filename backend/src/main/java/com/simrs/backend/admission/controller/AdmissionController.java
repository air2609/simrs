package com.simrs.backend.admission.controller;

import com.simrs.backend.admission.dto.AdmissionPatientSearchResponse;
import com.simrs.backend.admission.dto.AdmissionReferenceDataResponse;
import com.simrs.backend.admission.dto.AdmissionRegistrationRequest;
import com.simrs.backend.admission.dto.AdmissionRegistrationResponse;
import com.simrs.backend.admission.service.AdmissionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.simrs.backend.security.AuthUserInterceptor.USER_ATTRIBUTE;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @GetMapping("/references")
    public AdmissionReferenceDataResponse references() {
        return admissionService.getReferenceData();
    }

    @PostMapping("/registrations")
    public ResponseEntity<?> createRegistration(
            @Valid @RequestBody AdmissionRegistrationRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            AdmissionRegistrationResponse response = admissionService.createRegistration(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @GetMapping("/patients/search")
    public List<AdmissionPatientSearchResponse> searchPatients(
            @RequestParam(required = false) String mrNumber,
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) String patientName) {
        return admissionService.searchPatients(mrNumber, nik, patientName);
    }

    @GetMapping("/registrations")
    public List<AdmissionRegistrationResponse> listRegistrations(
            @RequestParam(required = false) String mrNumber,
            @RequestParam(required = false) String status) {
        return admissionService.listRegistrations(mrNumber, status);
    }

    @PostMapping("/registrations/{registrationNumber}/cancel")
    public ResponseEntity<?> cancelRegistration(
            @PathVariable String registrationNumber,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            AdmissionRegistrationResponse response = admissionService.cancelRegistration(registrationNumber, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
        }
    }

    private String extractUserId(HttpServletRequest httpServletRequest) {
        Object value = httpServletRequest.getAttribute(USER_ATTRIBUTE);
        return value == null ? "unknown" : value.toString();
    }

    private Map<String, String> error(String message) {
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", message);
        return response;
    }
}
