package org.acme.application.usecases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.acme.domain.dtos.PresentInstallmentDto;
import org.acme.domain.dtos.PresentSimulationDto;
import org.acme.domain.dtos.SimulationSolicitationDto;
import org.acme.domain.dtos.SimulationsResultDto;
import org.acme.domain.enums.SimulationType;
import org.acme.domain.exceptions.InvalidDesiredValueException;
import org.acme.domain.exceptions.InvalidPeriodException;
import org.acme.infrastructure.model.Product;
import org.acme.infrastructure.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class SimulateLoanUC {
    @Inject
    private ProductRepository productRepository;
    private SimulationSolicitationDto simulationSolicitation;
    private Product product;
    private boolean productHasBiggestMinValue;

    public SimulationsResultDto execute(SimulationSolicitationDto solicitation)
        throws InvalidDesiredValueException, InvalidPeriodException, NoResultException
    {
        this.simulationSolicitation = solicitation;
        this.validateSolicitation();
        
        var idSimulacao = 20180702;
        var simulationsResult = new SimulationsResultDto(
            idSimulacao,
            product.getId(),
            product.getName(),
            product.getInterestRate(),
            this.calculateSimulations()
        );
        return simulationsResult;
    }

    private void validateSolicitation() throws InvalidDesiredValueException, InvalidPeriodException {
        if (this.simulationSolicitation.desiredValue() < 200.0) {
            throw new InvalidDesiredValueException("O valor mínimo para a simulação é R$ 200,00.");
        }

        if (this.simulationSolicitation.period() < 0) {
            throw new InvalidPeriodException("O período mínimo para a simulação é 0.");
        }

        var biggestMinValueProduct = this.productRepository.getBiggestMinValueProduct();
        if (this.simulationSolicitation.desiredValue() > biggestMinValueProduct.getMinValue()) {
            this.product = biggestMinValueProduct;
            this.productHasBiggestMinValue = true;
        } else {
            this.product = this.productRepository.findBySolicitationValue(
                this.simulationSolicitation.desiredValue()
            );
            this.productHasBiggestMinValue = false;
        }

        this.validateSolicitationPeriodRange();
    }

    private void validateSolicitationPeriodRange() throws InvalidPeriodException {
        if (this.isPeriodRangeValid()) {
            return;
        }

        String errorMessage;
        errorMessage = String.format(
            "O período para uma simulação deste valor deve ser maior ou igual a %s.",
            this.product.getMinMonthsAmount()
        );
        if (! this.productHasBiggestMinValue) {
            errorMessage = String.format(
            "O período para uma simulação deste valor deve estar entre %s e %s.",
                this.product.getMinMonthsAmount(),
                this.product.getMaxMonthsAmount()
            );
        }

        throw new InvalidPeriodException(errorMessage);
    }

    private boolean isPeriodRangeValid() throws InvalidPeriodException {
        if (this.productHasBiggestMinValue) {
            return this.simulationSolicitation.period() >= this.product.getMinMonthsAmount();
        }

        var isPeriodRangeValid =
            this.simulationSolicitation.period() >= this.product.getMinMonthsAmount()
            && this.simulationSolicitation.period() <= this.product.getMaxMonthsAmount();

        return isPeriodRangeValid;
    }

    private ArrayList<PresentSimulationDto> calculateSimulations(){
        var simulationsStream = Arrays.stream(SimulationType.values()).map(
            (simulationType) -> this.createSimulation(simulationType)
        );
        var simulations = simulationsStream.collect(Collectors.toCollection(ArrayList::new));
        return simulations;
    }

    private PresentSimulationDto createSimulation(SimulationType simulationType) {
        var installments = simulationType.getAmortizationSystem()
            .setPresentValue(this.simulationSolicitation.desiredValue())
            .setInterestRate(this.product.getInterestRate())
            .setPeriod(this.simulationSolicitation.period())
            .calculateInstallments()
            .stream()
            .map((installment) -> PresentInstallmentDto.from(installment))
            .collect(Collectors.toCollection(ArrayList::new));
        return new PresentSimulationDto(simulationType, installments);
    }
}
