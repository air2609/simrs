package com.simrs.backend.admission.dto;

import java.util.List;

public class AdmissionReferenceDataResponse {

    private List<String> patientModes;
    private List<String> patientTypes;
    private List<String> units;
    private List<String> doctors;
    private List<String> ethnicities;
    private List<String> languages;

    public List<String> getPatientModes() {
        return patientModes;
    }

    public void setPatientModes(List<String> patientModes) {
        this.patientModes = patientModes;
    }

    public List<String> getPatientTypes() {
        return patientTypes;
    }

    public void setPatientTypes(List<String> patientTypes) {
        this.patientTypes = patientTypes;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public List<String> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<String> doctors) {
        this.doctors = doctors;
    }

    public List<String> getEthnicities() {
        return ethnicities;
    }

    public void setEthnicities(List<String> ethnicities) {
        this.ethnicities = ethnicities;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
