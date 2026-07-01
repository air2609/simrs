package com.simrs.backend.admission.persistence.repository;

import com.simrs.backend.admission.persistence.entity.InpatientBookingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InpatientBookingRepository extends JpaRepository<InpatientBookingEntity, Long> {

    Optional<InpatientBookingEntity> findByBookingNumber(String bookingNumber);

    Optional<InpatientBookingEntity> findByRegistrationNumber(String registrationNumber);

    boolean existsByBookingNumber(String bookingNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<InpatientBookingEntity> findByStatusContainingIgnoreCaseOrderByQueuedAtDesc(String status);

    List<InpatientBookingEntity> findByStatusContainingIgnoreCaseAndHallContainingIgnoreCaseOrderByQueuedAtDesc(
            String status,
            String hall);
}
