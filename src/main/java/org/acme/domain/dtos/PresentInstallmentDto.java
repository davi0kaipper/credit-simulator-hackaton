package org.acme.domain.dtos;

import java.math.BigDecimal;

import org.acme.domain.models.Installment;

public record PresentInstallmentDto(
    int number,
    BigDecimal amortization,
    BigDecimal interest,
    BigDecimal value
) {
    public static PresentInstallmentDto from(Installment installment) {
        return new PresentInstallmentDto(
            installment.getNumber(),
            installment.getAmortization(),
            installment.getInterest(),
            installment.getValue()
        );
    }
}
