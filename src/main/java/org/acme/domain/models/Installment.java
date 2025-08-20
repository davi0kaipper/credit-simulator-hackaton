package org.acme.domain.models;

public class Installment {
    int number;
    double amortization;
    double interest;
    double value;

    public Installment(
        int number,
        double amortization,
        double interest,
        double value
    ){
        this.number = number;
        this.amortization = amortization;
        this.interest = interest;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public double getAmortization() {
        return amortization;
    }

    public double getInterest() {
        return interest;
    }

    public double getValue() {
        return value;
    }
}
