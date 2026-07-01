package com.simrs.backend.admission.inpatient.service;

import com.simrs.backend.admission.inpatient.dto.InpatientBookingRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientBookingResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationRequest;
import com.simrs.backend.admission.inpatient.dto.InpatientMutationResponse;
import com.simrs.backend.admission.inpatient.dto.InpatientReferenceDataResponse;
import com.simrs.backend.admission.persistence.entity.InpatientBookingEntity;
import com.simrs.backend.admission.persistence.entity.InpatientMutationEntity;
import com.simrs.backend.admission.persistence.repository.InpatientBookingRepository;
import com.simrs.backend.admission.persistence.repository.InpatientMutationRepository;
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
public class InpatientAdmissionService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    private final AtomicLong bookingSequence = new AtomicLong(1L);
    private final AtomicLong registrationSequence = new AtomicLong(1L);
    private final AtomicLong mutationSequence = new AtomicLong(1L);

    private final InpatientBookingRepository inpatientBookingRepository;
    private final InpatientMutationRepository inpatientMutationRepository;
    private final AuditLogService auditLogService;

    public InpatientAdmissionService(
            InpatientBookingRepository inpatientBookingRepository,
            InpatientMutationRepository inpatientMutationRepository,
            AuditLogService auditLogService) {
        this.inpatientBookingRepository = inpatientBookingRepository;
        this.inpatientMutationRepository = inpatientMutationRepository;
        this.auditLogService = auditLogService;
    }

    public InpatientReferenceDataResponse getReferenceData() {
        InpatientReferenceDataResponse response = new InpatientReferenceDataResponse();
        response.setClasses(Arrays.asList("1. Kelas 1", "2. Kelas 2", "3. Kelas 3", "0. VIP", "9. VVIP"));
        response.setHalls(Arrays.asList("Anggrek", "Mawar", "Melati", "ICU"));
        response.setBeds(Arrays.asList("A-01", "A-02", "A-03", "B-01", "B-02", "ICU-01"));
        response.setDoctors(Arrays.asList("dr. Andi", "dr. Sari", "dr. Budi", "dr. Nia"));
        return response;
    }

    @Transactional
    public InpatientBookingResponse createBooking(InpatientBookingRequest request, String userId) {
        InpatientBookingEntity booking = new InpatientBookingEntity();
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

        inpatientBookingRepository.save(booking);

        auditLogService.log(
                "INPATIENT_BOOKING_CREATE",
                "INPATIENT_BOOKING",
                booking.getBookingNumber(),
                userId,
                "create inpatient booking");

        return toResponse(booking);
    }

    public List<InpatientBookingResponse> listBookings(String status) {
        List<InpatientBookingEntity> values;
        if (status == null || status.trim().isEmpty()) {
            values = inpatientBookingRepository.findAll();
            values.sort((a, b) -> b.getQueuedAt().compareTo(a.getQueuedAt()));
        } else {
            values = inpatientBookingRepository.findByStatusContainingIgnoreCaseOrderByQueuedAtDesc(status.trim());
        }

        return values.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<InpatientBookingResponse> listQueue(String hall) {
        if (hall == null || hall.trim().isEmpty()) {
            return listBookings("QUEUED");
        }

        return inpatientBookingRepository
                .findByStatusContainingIgnoreCaseAndHallContainingIgnoreCaseOrderByQueuedAtDesc("QUEUED", hall.trim())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InpatientBookingResponse confirmBooking(String bookingNumber, String userId) {
        InpatientBookingEntity booking = findBooking(bookingNumber);
        booking.setStatus("ACTIVE");
        inpatientBookingRepository.save(booking);

        auditLogService.log(
                "INPATIENT_BOOKING_CONFIRM",
                "INPATIENT_BOOKING",
                bookingNumber,
                userId,
                "confirm inpatient booking");

        return toResponse(booking);
    }

    @Transactional
    public InpatientBookingResponse cancelBooking(String bookingNumber, String userId) {
        InpatientBookingEntity booking = findBooking(bookingNumber);
        booking.setStatus("CANCELLED");
        inpatientBookingRepository.save(booking);

        auditLogService.log(
                "INPATIENT_BOOKING_CANCEL",
                "INPATIENT_BOOKING",
                bookingNumber,
                userId,
                "cancel inpatient booking");

        return toResponse(booking);
    }

    @Transactional
    public InpatientMutationResponse createMutation(InpatientMutationRequest request, String userId) {
        if (request.getFromBed().equalsIgnoreCase(request.getToBed())) {
            throw new IllegalArgumentException("Bed asal dan tujuan tidak boleh sama");
        }

        InpatientMutationEntity mutation = new InpatientMutationEntity();
        mutation.setMutationNumber(generateMutationNumber());
        mutation.setRegistrationNumber(request.getRegistrationNumber());
        mutation.setMrNumber(request.getMrNumber());
        mutation.setPatientName(request.getPatientName());
        mutation.setFromBed(request.getFromBed());
        mutation.setToBed(request.getToBed());
        mutation.setMutatedAt(LocalDateTime.now());

        inpatientMutationRepository.save(mutation);

        inpatientBookingRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .ifPresent(item -> {
                    item.setBed(request.getToBed());
                    inpatientBookingRepository.save(item);
                });

        auditLogService.log(
                "INPATIENT_MUTATION_CREATE",
                "INPATIENT_MUTATION",
                mutation.getMutationNumber(),
                userId,
                "mutate bed from " + request.getFromBed() + " to " + request.getToBed());

        return toResponse(mutation);
    }

    public List<InpatientMutationResponse> listMutations(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            return inpatientMutationRepository.findAllByOrderByMutatedAtDesc().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }

        return inpatientMutationRepository.findByRegistrationNumberOrderByMutatedAtDesc(registrationNumber)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private InpatientBookingEntity findBooking(String bookingNumber) {
        InpatientBookingEntity booking = inpatientBookingRepository.findByBookingNumber(bookingNumber).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Nomor booking tidak ditemukan");
        }
        return booking;
    }

    private String generateBookingNumber() {
        String bookingNumber;
        do {
            bookingNumber = "BK" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", bookingSequence.getAndIncrement());
        } while (inpatientBookingRepository.existsByBookingNumber(bookingNumber));
        return bookingNumber;
    }

    private String generateRegistrationNumber() {
        String registrationNumber;
        do {
            registrationNumber = "RI" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", registrationSequence.getAndIncrement());
        } while (inpatientBookingRepository.existsByRegistrationNumber(registrationNumber));
        return registrationNumber;
    }

    private String generateMutationNumber() {
        String mutationNumber;
        do {
            mutationNumber = "MT" + LocalDateTime.now().format(DATE_FORMAT) + "-" + String.format("%05d", mutationSequence.getAndIncrement());
        } while (inpatientMutationRepository.existsByMutationNumber(mutationNumber));
        return mutationNumber;
    }

    private InpatientBookingResponse toResponse(InpatientBookingEntity booking) {
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

    private InpatientMutationResponse toResponse(InpatientMutationEntity mutation) {
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
