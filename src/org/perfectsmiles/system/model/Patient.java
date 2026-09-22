package org.perfectsmiles.system.model;

public class Patient {

    private int idPatient;
    private String firstName;
    private String lastName;
    private String dpi;
    private String phone;
    private String email;
    private String address;
    private boolean patientStatus;

    public Patient() {
    }

    public Patient(int idPatient, String firstName, String lastName, String dpi,
            String phone, String email, String address, boolean patientStatus) {
        this.idPatient = idPatient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dpi = dpi;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.patientStatus = patientStatus;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(boolean patientStatus) {
        this.patientStatus = patientStatus;
    }

    /**
     * Nombre completo, útil para tablas y combo boxes
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Patient{id=" + idPatient + ", name='" + getFullName() + "', dpi='" + dpi + "'}";
    }
}
