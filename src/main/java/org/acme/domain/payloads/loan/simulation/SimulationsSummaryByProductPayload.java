package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.acme.domain.dtos.summary.SimulationsSummaryByProductDto;

public record SimulationsSummaryByProductPayload(
    Long codigoProduto,
    String descricaoProduto,
    Double taxaMediaJuro,
    BigDecimal valorMedioPrestacao,
    BigDecimal valorTotalDesejado,
    BigDecimal valorTotalCredito
){
    public static SimulationsSummaryByProductPayload from(SimulationsSummaryByProductDto summaryDto) {
        return new SimulationsSummaryByProductPayload(
            summaryDto.productId(),
            summaryDto.productDescription(),
            summaryDto.interestRateAverage(),
            SimulationsSummaryByProductPayload.formatValue(summaryDto.installmentAverage()),
            SimulationsSummaryByProductPayload.formatValue(summaryDto.totalDesiredValue()),
            SimulationsSummaryByProductPayload.formatValue(summaryDto.totalCredit())
        );
    }

    static BigDecimal formatValue(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}