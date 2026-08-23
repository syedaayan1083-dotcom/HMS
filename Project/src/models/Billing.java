
package models;

public class Billing {

    private int id;

    private String patientName;

    private String service;

    private double amount;

    private String paymentStatus;

    public Billing(int id,
                   String patientName,
                   String service,
                   double amount,
                   String paymentStatus) {

        this.id = id;

        this.patientName = patientName;

        this.service = service;

        this.amount = amount;

        this.paymentStatus = paymentStatus;
    }

    public int getId() {

        return id;
    }

    public String getPatientName() {

        return patientName;
    }

    public String getService() {

        return service;
    }

    public double getAmount() {

        return amount;
    }

    public String getPaymentStatus() {

        return paymentStatus;
    }

    public void setPatientName(String patientName) {

        this.patientName = patientName;
    }

    public void setService(String service) {

        this.service = service;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }

    public void setPaymentStatus(String paymentStatus) {

        this.paymentStatus = paymentStatus;
    }
}

