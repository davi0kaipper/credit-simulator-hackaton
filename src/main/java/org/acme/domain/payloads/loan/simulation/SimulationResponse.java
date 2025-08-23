package org.acme.domain.payloads.loan.simulation;

import java.util.ArrayList;

import org.acme.domain.models.Simulation;

public record SimulationResponse(
    int idSimulacao,
    int codigoProduto,
    String descricaoProduto,
    Double taxaJuros,
    ArrayList<Simulation> resultadoSimulacao
) { }
