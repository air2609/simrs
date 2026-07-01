package com.simrs.backend.admission.persistence.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ad_inpatient_mutation")
public class InpatientMutationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mutation_number", nullable = false, unique = true)
    private String mutationNumber;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "mr_number", nullable = false)
    private String mrNumber;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "from_bed", nullable = false)
    private String fromBed;

    @Column(name = "to_bed", nullable = false)
    private String toBed;

    @Column(name = "mutated_at", nullable = false)
    private LocalDateTime mutatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMutationNumber() {
        return mutationNumber;
    }

    public void setMutationNumber(String mutationNumber) {
        this.mutationNumber = mutationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMrNumber() {
        return mrNumber;
    }

    public void setMrNumber(String mrNumber) {
        this.mrNumber = mrNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getFromBed() {
        return fromBed;
    }

    public void setFromBed(String fromBed) {
        this.fromBed = fromBed;
    }

    public String getToBed() {
        return toBed;
    }

    public void setToBed(String toBed) {
        this.toBed = toBed;
    }

    public LocalDateTime getMutatedAt() {
        return mutatedAt;
    }

    public void setMutatedAt(LocalDateTime mutatedAt) {
        this.mutatedAt = mutatedAt;
    }
}
