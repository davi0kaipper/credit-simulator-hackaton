package org.acme.application.usecases;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.acme.domain.dtos.ListSimulationsDto;
import org.acme.domain.dtos.SimulationRecordDto;
import org.acme.domain.exceptions.InvalidPageNumberException;
import org.acme.domain.exceptions.InvalidRecordAmountByPageException;
import org.acme.infrastructure.repository.SimulationRepository;
import org.acme.infrastructure.tables.SimulationTable;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListSimulationsUC {
    private @Inject SimulationRepository simulationRepository;

    @Transactional
    public ListSimulationsDto execute(
        Integer page,
        Integer recordsAmountByPage
    ) throws InvalidPageNumberException, InvalidRecordAmountByPageException {
        if (page != null) {
            if (page < 1) {
                throw new InvalidPageNumberException(
                    "O número da página deve ser maior ou igual a 1."
                );
            }
        }

        if (recordsAmountByPage != null) {
            if (recordsAmountByPage < 1) {
                throw new InvalidRecordAmountByPageException(
                    "A quantidade de registros por páginas deve ser maior ou igual a 1."
                );
            }
        }

        var currentPage = page != null ? page - 1 : 0;
        var currentRecordsAmountByPage = recordsAmountByPage != null
            ? recordsAmountByPage
            : 200;
        var pagination = Page.of(currentPage, currentRecordsAmountByPage);
        var simulations = this.simulationRepository.getSimulationsAndItsInstallmentsTotalAmount(pagination);
        try (Stream<SimulationRecordDto> stream = simulations.stream()) {
            var simulationsRecords = stream.collect(Collectors.toList());

            var simulationsList = new ListSimulationsDto(
                currentPage + 1,
                (int) SimulationTable.count(),
                simulationsRecords.size(),
                simulationsRecords
            );
            return simulationsList;
        }
    }
}
