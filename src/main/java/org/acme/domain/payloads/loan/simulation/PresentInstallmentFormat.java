package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
            PresentInstallmentFormat.formatValue(presentInstallmentDto.amortization()),
            PresentInstallmentFormat.formatValue(presentInstallmentDto.interest()),
            PresentInstallmentFormat.formatValue(presentInstallmentDto.value())
        );
    }

    static BigDecimal formatValue(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
