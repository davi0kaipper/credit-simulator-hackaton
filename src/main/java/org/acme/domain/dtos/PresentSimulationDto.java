package org.acme.domain.dtos;

import java.util.ArrayList;

import org.acme.domain.enums.SimulationType;

public class PresentSimulationDto {
    public SimulationType tipo;
    public ArrayList<PresentInstallmentDto> parcelas;
    
    public PresentSimulationDto(
        SimulationType type,
        ArrayList<PresentInstallmentDto> installments
    ){
        this.tipo = type;
        this.parcelas = installments;
    }
}
