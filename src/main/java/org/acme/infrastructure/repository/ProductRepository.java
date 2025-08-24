package org.acme.infrastructure.repository;

import org.acme.infrastructure.tables.ProductTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductTable findBySolicitationValue(Double value) {
        return entityManager.createQuery(
        "SELECT p FROM ProductTable p WHERE p.minValue <= :val AND p.maxValue >= :val",
        ProductTable.class)
        .setParameter("val", value)
        .getSingleResult();
    }

    public ProductTable getBiggestMinValueProduct() {
        return entityManager
            .createQuery(
                "SELECT p FROM ProductTable p ORDER BY p.minValue DESC LIMIT 1",
                ProductTable.class
            )
            .getSingleResult();
    }
}
