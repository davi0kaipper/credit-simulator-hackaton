package org.acme.domain.dtos;

import org.acme.infrastructure.model.Product;

public record  ProductDto(
    Long id,
    String name,
    Double interestRate,
    Integer minMonthsAmount,
    Integer maxMonthsAmount,
    Double minValue,
    Double maxValue
) {
    public static ProductDto from(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(),
            product.getInterestRate(),
            product.getMinMonthsAmount(),
            product.getMaxMonthsAmount(),
            product.getMinValue(),
            product.getMaxValue()
        );
    }
}
