package com.simrs.backend.admission.inpatient.dto;

import javax.validation.constraints.NotBlank;

public class InpatientMutationRequest {

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String mrNumber;

    @NotBlank
    private String patientName;

    @NotBlank
    private String fromBed;

    @NotBlank
    private String toBed;

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
}
