package org.acme.infrastructure.tables;

import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "telemetry")
public class TelemetryTable extends PanacheEntity {
    public String method;
    public String uri;
    public Integer averageTime;
    public Integer minTime;
    public Integer maxTime;
    public Long requestsAmount;
    public Long successfulRequests;
    public LocalDate referenceDate;

    public TelemetryTable () { }

    public static Integer calculateSuccesfulRequest(Integer responseStatus) {
        return responseStatus.equals(200) ? 1 : 0;
    }
}
