package org.acme.domain.payloads.loan.simulation;

import java.util.List;

import org.acme.domain.dtos.ListSimulationsDto;

public record ListSimulationsPayload(
    int pagina,
    int qtdRegistros,
    int qtdRegistrosPagina,
    List<SimulationRecordPayload> registros
){
    public static ListSimulationsPayload from(
        ListSimulationsDto listSimulationsDto
    ){
        return new ListSimulationsPayload(
            listSimulationsDto.page(),
            listSimulationsDto.recordsAmount(),
            listSimulationsDto.recordsAmountByPage(),
            listSimulationsDto.records().stream().map(SimulationRecordPayload::from).toList()
        );
    }
}