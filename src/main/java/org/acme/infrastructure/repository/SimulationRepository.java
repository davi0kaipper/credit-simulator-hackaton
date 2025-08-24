package org.acme.infrastructure.repository;

import org.acme.domain.dtos.SimulationRecordDto;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulationRepository implements PanacheRepository<SimulationRecordDto> {
    public PanacheQuery<SimulationRecordDto> getSimulationsAndItsInstallmentsTotalAmount(Page pagination) {
        String query =
            """
                SELECT
                    new org.acme.domain.dtos.SimulationRecordDto(
                        s.id,
                        s.desiredValue,
                        s.period,
                        (SUM(i.value) / 2.0)
                    )
                FROM InstallmentEntity i
                JOIN SimulationResultEntity sr ON i.simulationResult.id = sr.id
                JOIN SimulationEntity s ON sr.simulation.id = s.id
                GROUP BY s.id, s.desiredValue, s.period
            """;

        var result = find(query).page(pagination);
        return result;
    }
}
