package com.simrs.backend.admission.inpatient.dto;

import javax.validation.constraints.NotBlank;

public class InpatientBookingRequest {

    @NotBlank
    private String mrNumber;

    @NotBlank
    private String patientName;

    @NotBlank
    private String previousRegistrationNumber;

    @NotBlank
    private String targetClass;

    @NotBlank
    private String hall;

    @NotBlank
    private String bed;

    @NotBlank
    private String mainDoctor;

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

    public String getPreviousRegistrationNumber() {
        return previousRegistrationNumber;
    }

    public void setPreviousRegistrationNumber(String previousRegistrationNumber) {
        this.previousRegistrationNumber = previousRegistrationNumber;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getMainDoctor() {
        return mainDoctor;
    }

    public void setMainDoctor(String mainDoctor) {
        this.mainDoctor = mainDoctor;
    }
}
