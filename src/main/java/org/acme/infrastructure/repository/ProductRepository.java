package org.acme.infrastructure.repository;

import java.util.List;

import org.acme.infrastructure.model.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Product> findAll() {
        return entityManager
            .createQuery("FROM Product", Product.class)
            .getResultList();
    }

    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }

    public Product findBySolicitationValue(double value) {
        return entityManager.createQuery(
        "SELECT p FROM Product p WHERE p.minValue <= :val AND p.maxValue >= :val",
        Product.class)
        .setParameter("val", value)
        .getSingleResult();
    }

    public Product getBiggestMinValueProduct() {
        return entityManager
            .createQuery(
                "SELECT p FROM Product p ORDER BY p.minValue DESC LIMIT 1",
                Product.class
            )
            .getSingleResult();
    }
}
