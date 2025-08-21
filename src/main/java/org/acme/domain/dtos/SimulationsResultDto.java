package org.acme.domain.dtos;

import java.util.ArrayList;

public record SimulationsResultDto(
        Long idSimulacao,
        Long codigoProduto,
        String descricaoProduto,
        double taxaJuros,
        ArrayList<PresentSimulationDto> resultadoSimulacao
) { }
