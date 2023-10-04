package com.example.RestOAuth2JPA.enums;

public enum HospitalDepartments {
    OutPatientD ("Outpatient department"),
    InpatientP ("Inpatient Service"),
    MedicalD ("Medical Department"),
    NursingD ("Nursing Department"),
    ParamedicalD ("Paramedical Department"),
    PMRD ("Physical Medicine and Rehabilitation Department"),
    OerationalTheatre ("Operation Theatre Complex"),
    PharmacyD ("Pharmacy Department"),
    RadiologyD ("Radiology Department");

    private String name;

    HospitalDepartments(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return name;
    }
}
