package org.acme.domain.dtos;

import java.util.ArrayList;

public record SimulationsResultDto(
        Long idSimulacao,
        Long codigoProduto,
        String descricaoProduto,
        Double taxaJuros,
        ArrayList<PresentSimulationDto> resultadoSimulacao
) { }
