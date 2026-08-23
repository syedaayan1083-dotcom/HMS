package models;

public class Appointment {

    private int id;

    private String patientName;

    private String doctorName;

    private String appointmentDate;

    private String appointmentTime;

    private String status;

    public Appointment(int id,
                       String patientName,
                       String doctorName,
                       String appointmentDate,
                       String appointmentTime,
                       String status) {

        this.id = id;

        this.patientName = patientName;

        this.doctorName = doctorName;

        this.appointmentDate = appointmentDate;

        this.appointmentTime = appointmentTime;

        this.status = status;
    }

    public int getId() {

        return id;
    }

    public String getPatientName() {

        return patientName;
    }

    public String getDoctorName() {

        return doctorName;
    }

    public String getAppointmentDate() {

        return appointmentDate;
    }

    public String getAppointmentTime() {

        return appointmentTime;
    }

    public String getStatus() {

        return status;
    }

    public void setPatientName(String patientName) {

        this.patientName = patientName;
    }

    public void setDoctorName(String doctorName) {

        this.doctorName = doctorName;
    }

    public void setAppointmentDate(String appointmentDate) {

        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentTime(String appointmentTime) {

        this.appointmentTime = appointmentTime;
    }

    public void setStatus(String status) {

        this.status = status;
    }
}