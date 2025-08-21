package org.acme.domain.dtos;

import java.util.List;

public record ListSimulationsDto(
    int page,
    int recordsAmount,
    int recordsAmountByPage,
    List<SimulationRecordDto> records
){
    public static ListSimulationsDto from(
        int page,
        int recordsAmount,
        int recordsAmountByPage,
        List<SimulationRecordDto> records
    ){
        return new ListSimulationsDto(
            page,
            recordsAmount,
            recordsAmountByPage,
            records
        );
    }
}