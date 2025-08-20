package org.acme.domain.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.acme.domain.models.Installment;

public record PresentInstallmentDto(
    int numero,
    BigDecimal valorAmortizacao,
    BigDecimal valorJuros,
    BigDecimal valorPrestacao
) {
    public static PresentInstallmentDto from(Installment installment) {
        return new PresentInstallmentDto(
            installment.getNumber(),
            PresentInstallmentDto.formatValue(installment.getAmortization()),
            PresentInstallmentDto.formatValue(installment.getInterest()),
            PresentInstallmentDto.formatValue(installment.getValue())
        );
    }

    static BigDecimal formatValue(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
