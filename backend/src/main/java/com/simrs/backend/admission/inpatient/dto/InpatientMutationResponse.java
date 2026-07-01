package com.simrs.backend.admission.inpatient.dto;

public class InpatientMutationResponse {

    private String mutationNumber;
    private String registrationNumber;
    private String mrNumber;
    private String patientName;
    private String fromBed;
    private String toBed;
    private String mutatedAt;

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

    public String getMutatedAt() {
        return mutatedAt;
    }

    public void setMutatedAt(String mutatedAt) {
        this.mutatedAt = mutatedAt;
    }
}
