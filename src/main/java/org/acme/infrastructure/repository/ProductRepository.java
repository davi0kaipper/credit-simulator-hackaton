package org.acme.infrastructure.repository;

import org.acme.infrastructure.tables.ProductTable;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductTable, Long> {
    public ProductTable findBySolicitationValue(Double value) {
        return find("minValue <= ?1 and maxValue >= ?1", value).firstResult();
    }

    public ProductTable getBiggestMinValueProduct() {
        return find("ORDER BY minValue DESC").firstResult();
    }
}
