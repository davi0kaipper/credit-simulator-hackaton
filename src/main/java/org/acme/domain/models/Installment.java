package org.acme.domain.models;

import java.math.BigDecimal;

public class Installment {
    int number;
    BigDecimal amortization;
    BigDecimal interest;
    BigDecimal value;

    public Installment(
        int number,
        BigDecimal amortization,
        BigDecimal interest,
        BigDecimal value
    ){
        this.number = number;
        this.amortization = amortization;
        this.interest = interest;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public BigDecimal getAmortization() {
        return amortization;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getValue() {
        return value;
    }
}
