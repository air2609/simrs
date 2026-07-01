package com.simrs.backend.admission.persistence.repository;

import com.simrs.backend.admission.persistence.entity.PatientEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByMrNumber(String mrNumber);

    Optional<PatientEntity> findByNik(String nik);

    List<PatientEntity> findByMrNumberContainingIgnoreCaseAndNikContainingIgnoreCase(String mrNumber, String nik);

    List<PatientEntity> findByPatientNameContainingIgnoreCase(String patientName);
}
