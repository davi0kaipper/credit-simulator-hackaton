package org.acme.domain.dtos.summary;

import java.math.BigDecimal;

import org.acme.domain.dtos.metrics.MetricStatisticalDataDto;
import org.acme.infrastructure.tables.ProductTable;

public record SimulationsSummaryByProductDto(
    Long productId,
    String productDescription,
    BigDecimal interestRateAverage,
    Double installmentAverage,
    Double totalDesiredValue,
    Double totalCredit
) {
    public static SimulationsSummaryByProductDto from(MetricStatisticalDataDto data) {
        return new SimulationsSummaryByProductDto(
            data.getProductId(),
            data.getProductDescription(),
            data.getInterestRateAverage(),
            data.getInstallmentAverage(),
            data.getTotalDesiredValue(),
            data.getTotalCredit()
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