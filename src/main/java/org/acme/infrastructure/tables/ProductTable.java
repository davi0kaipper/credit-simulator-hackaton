package org.acme.infrastructure.tables;

import java.math.BigDecimal;

import com.google.errorprone.annotations.Immutable;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUTO")
@Immutable
public class ProductTable extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CO_PRODUTO")
    private Long id;

    @Column(name = "NO_PRODUTO")
    private String name;

    @Column(name = "PC_TAXA_JUROS", precision = 10, scale = 8)
    private BigDecimal interestRate;

    @Column(name = "NU_MINIMO_MESES")
    private Integer minMonthsAmount;

    @Column(name = "NU_MAXIMO_MESES")
    private Integer maxMonthsAmount;

    @Column(name = "VR_MINIMO")
    private Double minValue;

    @Column(name = "VR_MAXIMO")
    private Double maxValue;

    public ProductTable() { }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public Integer getMinMonthsAmount() {
        return minMonthsAmount;
    }

    public Integer getMaxMonthsAmount() {
        return maxMonthsAmount;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }
}
