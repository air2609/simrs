package com.simrs.backend.admission.persistence.repository;

import com.simrs.backend.admission.persistence.entity.InpatientMutationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InpatientMutationRepository extends JpaRepository<InpatientMutationEntity, Long> {

    boolean existsByMutationNumber(String mutationNumber);

    List<InpatientMutationEntity> findByRegistrationNumberOrderByMutatedAtDesc(String registrationNumber);

    List<InpatientMutationEntity> findAllByOrderByMutatedAtDesc();
}
