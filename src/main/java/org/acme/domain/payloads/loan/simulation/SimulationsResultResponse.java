package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;
import java.util.List;

import org.acme.domain.dtos.SimulationsResultDto;

public record SimulationsResultResponse(
	Long idSimulacao,
	Long codigoProduto,
	String descricaoProduto,
	BigDecimal taxaJuros,
	List<PresentSimulationFormat> resultadoSimulacao
) {

	public static SimulationsResultResponse from(SimulationsResultDto simulationsResultDto) {
		return new SimulationsResultResponse(
			simulationsResultDto.simulationId(),
			simulationsResultDto.productId(),
			simulationsResultDto.productName(),
			simulationsResultDto.interestRate(),
			simulationsResultDto.simulationResult().stream()
			.map(PresentSimulationFormat::from).toList()
		);
	}
}
