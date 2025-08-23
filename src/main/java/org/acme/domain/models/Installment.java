package org.acme.domain.models;

public class Installment {
    int number;
    Double amortization;
    Double interest;
    Double value;

    public Installment(
        int number,
        Double amortization,
        Double interest,
        Double value
    ){
        this.number = number;
        this.amortization = amortization;
        this.interest = interest;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public Double getAmortization() {
        return amortization;
    }

    public Double getInterest() {
        return interest;
    }

    public Double getValue() {
        return value;
    }
}
