package org.acme.domain.dtos.metrics;

import java.math.BigDecimal;

public class MetricStatisticalDataDto {
    private Long productId;
    private String productDescription;
    private Double interestRateAverage;
    private Double installmentAverage;
    private Double totalDesiredValue;
    private Double totalCredit;

    public MetricStatisticalDataDto(
        Long productId,
        Double interestRateAverage,
        Double installmentAverage,
        Double totalDesiredValue,
        Double totalCredit
    ){
        this.productId = productId;
        this.interestRateAverage = interestRateAverage;
        this.installmentAverage = installmentAverage;
        this.totalDesiredValue = totalDesiredValue;
        this.totalCredit = totalCredit;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public BigDecimal getInterestRateAverage() {
        return BigDecimal.valueOf(interestRateAverage);
    }

    public Double getInstallmentAverage() {
        return installmentAverage;
    }

    public Double getTotalDesiredValue() {
        return totalDesiredValue;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }
}