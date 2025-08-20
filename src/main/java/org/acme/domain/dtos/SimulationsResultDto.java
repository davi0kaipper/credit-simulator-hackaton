package org.acme.domain.dtos;

import java.util.ArrayList;

public record SimulationsResultDto(
        int idSimulacao,
        Long codigoProduto,
        String descricaoProduto,
        double taxaJuros,
        ArrayList<PresentSimulationDto> resultadoSimulacao
) { }
