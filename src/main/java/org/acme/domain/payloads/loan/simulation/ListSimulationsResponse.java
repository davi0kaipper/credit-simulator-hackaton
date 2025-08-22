package org.acme.domain.payloads.loan.simulation;

import java.util.List;

import org.acme.domain.dtos.ListSimulationsDto;

public record ListSimulationsResponse(
    int pagina,
    int qtdRegistros,
    int qtdRegistrosPagina,
    List<SimulationRecordResponse> registros
){
    public static ListSimulationsResponse from(
        ListSimulationsDto listSimulationsDto
    ){
        return new ListSimulationsResponse(
            listSimulationsDto.page(),
            listSimulationsDto.recordsAmount(),
            listSimulationsDto.recordsAmountByPage(),
            listSimulationsDto.records().stream().map(SimulationRecordResponse::from).toList()
        );
    }
}