package com.simrs.backend.admission.inpatient.service;

import com.simrs.backend.admission.inpatient.dto.InpatientBookingRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientBookingResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientReferenceDataResponse;
import com.simrs.backend.admission.inpatient.model.InpatientBooking;
import com.simrs.backend.admission.inpatient.model.InpatientMutation;
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
public class InpatientAdmissionService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    private final AtomicLong bookingSequence = new AtomicLong(1L);
    private final AtomicLong registrationSequence = new AtomicLong(1L);
    private final AtomicLong mutationSequence = new AtomicLong(1L);

    private final Map<String, InpatientBooking> bookings = new ConcurrentHashMap<String, InpatientBooking>();
    private final Map<String, List<InpatientMutation>> mutationsByRegistration = new ConcurrentHashMap<String, List<InpatientMutation>>();

    public InpatientReferenceDataResponse getReferenceData() {
        InpatientReferenceDataResponse response = new InpatientReferenceDataResponse();
        response.setClasses(Arrays.asList("1. Kelas 1", "2. Kelas 2", "3. Kelas 3", "0. VIP", "9. VVIP"));
        response.setHalls(Arrays.asList("Anggrek", "Mawar", "Melati", "ICU"));
        response.setBeds(Arrays.asList("A-01", "A-02", "A-03", "B-01", "B-02", "ICU-01"));
        response.setDoctors(Arrays.asList("dr. Andi", "dr. Sari", "dr. Budi", "dr. Nia"));
        return response;
    }

    public InpatientBookingResponse createBooking(InpatientBookingRequest request) {
        InpatientBooking booking = new InpatientBooking();
        booking.setBookingNumber(generateBookingNumber());
        booking.setRegistrationNumber(generateRegistrationNumber());
        booking.setMrNumber(request.getMrNumber().trim().toUpperCase());
        booking.setPatientName(request.getPatientName());
        booking.setTargetClass(request.getTargetClass());
        booking.setHall(request.getHall());
        booking.setBed(request.getBed());
        booking.setMainDoctor(request.getMainDoctor());
        booking.setStatus("QUEUED");
        booking.setQueuedAt(LocalDateTime.now());

        bookings.put(booking.getBookingNumber(), booking);
        return toResponse(booking);
    }

    public List<InpatientBookingResponse> listBookings(String status) {
        List<InpatientBooking> values = new ArrayList<InpatientBooking>(bookings.values());
        Collections.sort(values, (a, b) -> b.getQueuedAt().compareTo(a.getQueuedAt()));

        return values.stream()
                .filter(item -> status == null || status.trim().isEmpty() || item.getStatus().equalsIgnoreCase(status.trim()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<InpatientBookingResponse> listQueue(String hall) {
        return listBookings("QUEUED").stream()
                .filter(item -> hall == null || hall.trim().isEmpty() || item.getHall().equalsIgnoreCase(hall.trim()))
                .collect(Collectors.toList());
    }

    public InpatientBookingResponse confirmBooking(String bookingNumber) {
        InpatientBooking booking = findBooking(bookingNumber);
        booking.setStatus("ACTIVE");
        return toResponse(booking);
    }

    public InpatientBookingResponse cancelBooking(String bookingNumber) {
        InpatientBooking booking = findBooking(bookingNumber);
        booking.setStatus("CANCELLED");
        return toResponse(booking);
    }

    public InpatientMutationResponse createMutation(InpatientMutationRequest request) {
        if (request.getFromBed().equalsIgnoreCase(request.getToBed())) {
            throw new IllegalArgumentException("Bed asal dan tujuan tidak boleh sama");
        }

        InpatientMutation mutation = new InpatientMutation();
        mutation.setMutationNumber(generateMutationNumber());
        mutation.setRegistrationNumber(request.getRegistrationNumber());
        mutation.setMrNumber(request.getMrNumber());
        mutation.setPatientName(request.getPatientName());
        mutation.setFromBed(request.getFromBed());
        mutation.setToBed(request.getToBed());
        mutation.setMutatedAt(LocalDateTime.now());

        List<InpatientMutation> history = mutationsByRegistration.computeIfAbsent(
                request.getRegistrationNumber(), key -> new ArrayList<InpatientMutation>());
        history.add(mutation);

        bookings.values().stream()
                .filter(item -> item.getRegistrationNumber().equalsIgnoreCase(request.getRegistrationNumber()))
                .findFirst()
                .ifPresent(item -> item.setBed(request.getToBed()));

        return toResponse(mutation);
    }

    public List<InpatientMutationResponse> listMutations(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            return mutationsByRegistration.values().stream()
                    .flatMap(List::stream)
                    .sorted((a, b) -> b.getMutatedAt().compareTo(a.getMutatedAt()))
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }

        return mutationsByRegistration
                .getOrDefault(registrationNumber, new ArrayList<InpatientMutation>())
                .stream()
                .sorted((a, b) -> b.getMutatedAt().compareTo(a.getMutatedAt()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private InpatientBooking findBooking(String bookingNumber) {
        InpatientBooking booking = bookings.get(bookingNumber);
        if (booking == null) {
            throw new IllegalArgumentException("Nomor booking tidak ditemukan");
        }
        return booking;
    }

    private String generateBookingNumber() {
        return "BK" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", bookingSequence.getAndIncrement());
    }

    private String generateRegistrationNumber() {
        return "RI" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", registrationSequence.getAndIncrement());
    }

    private String generateMutationNumber() {
        return "MT" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", mutationSequence.getAndIncrement());
    }

    private InpatientBookingResponse toResponse(InpatientBooking booking) {
        InpatientBookingResponse response = new InpatientBookingResponse();
        response.setBookingNumber(booking.getBookingNumber());
        response.setRegistrationNumber(booking.getRegistrationNumber());
        response.setMrNumber(booking.getMrNumber());
        response.setPatientName(booking.getPatientName());
        response.setTargetClass(booking.getTargetClass());
        response.setHall(booking.getHall());
        response.setBed(booking.getBed());
        response.setMainDoctor(booking.getMainDoctor());
        response.setStatus(booking.getStatus());
        response.setQueuedAt(booking.getQueuedAt().toString());
        return response;
    }

    private InpatientMutationResponse toResponse(InpatientMutation mutation) {
        InpatientMutationResponse response = new InpatientMutationResponse();
        response.setMutationNumber(mutation.getMutationNumber());
        response.setRegistrationNumber(mutation.getRegistrationNumber());
        response.setMrNumber(mutation.getMrNumber());
        response.setPatientName(mutation.getPatientName());
        response.setFromBed(mutation.getFromBed());
        response.setToBed(mutation.getToBed());
        response.setMutatedAt(mutation.getMutatedAt().toString());
        return response;
    }
}
