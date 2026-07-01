package com.simrs.backend.admission.persistence.repository;

import com.simrs.backend.admission.persistence.entity.OutpatientRegistrationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutpatientRegistrationRepository extends JpaRepository<OutpatientRegistrationEntity, Long> {

    Optional<OutpatientRegistrationEntity> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<OutpatientRegistrationEntity> findByMrNumberContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByRegisteredAtDesc(
            String mrNumber,
            String status);

    List<OutpatientRegistrationEntity> findByStatusContainingIgnoreCaseOrderByRegisteredAtDesc(String status);

    List<OutpatientRegistrationEntity> findByMrNumberContainingIgnoreCaseOrderByRegisteredAtDesc(String mrNumber);
}
