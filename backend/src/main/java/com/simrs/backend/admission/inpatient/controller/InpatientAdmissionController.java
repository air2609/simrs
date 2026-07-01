package com.simrs.backend.admission.inpatient.controller;

import com.simrs.backend.admission.inpatient.dto.InpatientBookingRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientBookingResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientReferenceDataResponse;
import com.simrs.backend.admission.inpatient.service.InpatientAdmissionService;
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
@RequestMapping("/api/admissions/inpatient")
public class InpatientAdmissionController {

    private final InpatientAdmissionService inpatientAdmissionService;

    public InpatientAdmissionController(InpatientAdmissionService inpatientAdmissionService) {
        this.inpatientAdmissionService = inpatientAdmissionService;
    }

    @GetMapping("/references")
    public InpatientReferenceDataResponse getReferences() {
        return inpatientAdmissionService.getReferenceData();
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(
            @Valid @RequestBody InpatientBookingRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            InpatientBookingResponse response = inpatientAdmissionService.createBooking(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @GetMapping("/bookings")
    public List<InpatientBookingResponse> listBookings(@RequestParam(required = false) String status) {
        return inpatientAdmissionService.listBookings(status);
    }

    @GetMapping("/queues")
    public List<InpatientBookingResponse> listQueue(@RequestParam(required = false) String hall) {
        return inpatientAdmissionService.listQueue(hall);
    }

    @PostMapping("/bookings/{bookingNumber}/confirm")
    public ResponseEntity<?> confirmBooking(
            @PathVariable String bookingNumber,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            return ResponseEntity.ok(inpatientAdmissionService.confirmBooking(bookingNumber, userId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/bookings/{bookingNumber}/cancel")
    public ResponseEntity<?> cancelBooking(
            @PathVariable String bookingNumber,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            return ResponseEntity.ok(inpatientAdmissionService.cancelBooking(bookingNumber, userId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/mutations")
    public ResponseEntity<?> createMutation(
            @Valid @RequestBody InpatientMutationRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            String userId = extractUserId(httpServletRequest);
            InpatientMutationResponse response = inpatientAdmissionService.createMutation(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @GetMapping("/mutations")
    public List<InpatientMutationResponse> listMutations(@RequestParam(required = false) String registrationNumber) {
        return inpatientAdmissionService.listMutations(registrationNumber);
    }

    private String extractUserId(HttpServletRequest httpServletRequest) {
        Object value = httpServletRequest.getAttribute(USER_ATTRIBUTE);
        return value == null ? "unknown" : value.toString();
    }

    private Map<String, String> error(String message) {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("message", message);
        return payload;
    }
}
