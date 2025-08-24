package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;

import org.acme.domain.dtos.PresentInstallmentDto;

public record PresentInstallmentFormat(
    int numero,
    BigDecimal valorAmortizacao,
    BigDecimal valorJuros,
    BigDecimal valorPrestacao
) {
    public static PresentInstallmentFormat from(PresentInstallmentDto presentInstallmentDto) {
        return new PresentInstallmentFormat(
            presentInstallmentDto.number(),
            presentInstallmentDto.amortization(),
            presentInstallmentDto.interest(),
            presentInstallmentDto.value()
        );
    }
}
