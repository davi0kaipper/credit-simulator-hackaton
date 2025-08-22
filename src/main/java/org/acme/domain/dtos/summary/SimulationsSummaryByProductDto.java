package org.acme.domain.dtos.summary;

import org.acme.domain.dtos.MetricStatisticalDataDto;
import org.acme.infrastructure.tables.ProductTable;

public record SimulationsSummaryByProductDto(
    Long productId,
    String productDescription,
    Double interestRateAverage,
    Double installmentAverage,
    Double totalDesiredValue,
    Double totalCredit
) {
    public static SimulationsSummaryByProductDto from(
        ProductTable product,
        MetricStatisticalDataDto data
    ){
        return new SimulationsSummaryByProductDto(
            product.getId(),
            product.getName(),
            data.interestRateAverage(),
            data.installmentAverage(),
            data.totalDesiredValue(),
            data.totalCredit()
        );
    }

    public static SimulationsSummaryByProductDto empty(
        ProductTable product
    ){
        return new SimulationsSummaryByProductDto(
            product.getId(),
            product.getName(),
            null,
            null,
            null,
            null
        );
    }
}