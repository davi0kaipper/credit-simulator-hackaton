package org.acme.domain.dtos;

import org.acme.domain.models.Installment;

public record PresentInstallmentDto(
    int number,
    Double amortization,
    Double interest,
    Double value
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
