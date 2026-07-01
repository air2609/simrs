package com.simrs.backend.admission.inpatient.dto;

import java.util.List;

public class InpatientReferenceDataResponse {

    private List<String> classes;
    private List<String> halls;
    private List<String> beds;
    private List<String> doctors;

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public List<String> getHalls() {
        return halls;
    }

    public void setHalls(List<String> halls) {
        this.halls = halls;
    }

    public List<String> getBeds() {
        return beds;
    }

    public void setBeds(List<String> beds) {
        this.beds = beds;
    }

    public List<String> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<String> doctors) {
        this.doctors = doctors;
    }
}
