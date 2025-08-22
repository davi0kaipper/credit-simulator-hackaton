package org.acme.domain.payloads.loan.simulation;

import org.acme.domain.dtos.summary.SimulationsSummaryByProductDto;

public record SimulationsSummaryByProductPayload(
    Long codigoProduto,
    String descricaoProduto,
    double taxaMediaJuro,
    double valorMedioPrestacao,
    double valorTotalDesejado,
    double valorTotalCredito
){
    public static SimulationsSummaryByProductPayload from(SimulationsSummaryByProductDto summaryDto) {
        return new SimulationsSummaryByProductPayload(
            summaryDto.productId(),
            summaryDto.productDescription(),
            summaryDto.interestRateAverage(),
            summaryDto.installmentAverage(),
            summaryDto.totalDesiredValue(),
            summaryDto.totalCredit()
        );
    }
}