package org.acme.domain.models;

import java.util.ArrayList;

public class SimulationsResult {
    int idSimulacao;
    int codigoProduto;
    String descricaoProduto;
    double taxaJuros;
    ArrayList<Simulation> resultadoSimulacao;

    public SimulationsResult(
        int idSimulacao,
        int codigoProduto,
        String descricaoProduto,
        double taxaJuros,
        ArrayList<Simulation> resultadoSimulacao
    ){
        this.idSimulacao = idSimulacao;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.taxaJuros = taxaJuros;
        this.resultadoSimulacao = resultadoSimulacao;
    }
}
