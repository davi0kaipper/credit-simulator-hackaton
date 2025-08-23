package org.acme.infrastructure.repository;

import java.util.List;

import org.acme.infrastructure.tables.ProductTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductTable> findAll() {
        return entityManager
            .createQuery("FROM ProductTable", ProductTable.class)
            .getResultList();
    }

    public ProductTable findById(Long id) {
        return entityManager.find(ProductTable.class, id);
    }

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

    public ProductTable getSummaryByProductId() {
        return entityManager
            .createQuery(
                "SELECT p FROM ProductTable p ORDER BY p.minValue DESC LIMIT 1",
                ProductTable.class
            )
            .getSingleResult();
    }

    public List<Long> listIdsOfProductsReferecendBySimulations() {
        return entityManager
            .createQuery(
                "SELECT DISTINCT s.product.id FROM SimulationTable s",
                Long.class
            )
            .getResultList();
    }
}
