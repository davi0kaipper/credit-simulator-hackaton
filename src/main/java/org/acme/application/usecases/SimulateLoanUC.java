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
import org.acme.infrastructure.repository.ProductRepository;
import org.acme.infrastructure.tables.ProductTable;
import org.acme.infrastructure.tables.SimulationResultTable;
import org.acme.infrastructure.tables.SimulationTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SimulateLoanUC {
    private @Inject ProductRepository productRepository;

    private SimulationSolicitationDto simulationSolicitation;
    private boolean productHasBiggestMinValue;
    private ProductTable productDbEntity;
    private SimulationTable simulationDbEntity;
    private SimulationResultTable simulationResultDbEntity;

    @Transactional
    public SimulationsResultDto execute(SimulationSolicitationDto solicitation)
        throws InvalidDesiredValueException, InvalidPeriodException, NoResultException
    {
        this.simulationSolicitation = solicitation;
        this.validateSolicitation();
        
        this.persistSimulation();
        var simulationsResult = this.createSimulationsResult();
        
        return simulationsResult;
    }

    private SimulationsResultDto createSimulationsResult() {
        return new SimulationsResultDto(
            this.simulationDbEntity.id,
            this.productDbEntity.getId(),
            this.productDbEntity.getName(),
            this.productDbEntity.getInterestRate(),
            this.createSimulations()
        );
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
            this.productDbEntity = biggestMinValueProduct;
            this.productHasBiggestMinValue = true;
        } else {
            this.productDbEntity = this.productRepository.findBySolicitationValue(
                this.simulationSolicitation.desiredValue()
            );
            this.productHasBiggestMinValue = false;
        }

        this.validateSolicitationPeriodRange();
    }
    
    @Transactional
    protected ArrayList<PresentSimulationDto> createSimulations(){
        var simulationsStream = Arrays.stream(SimulationType.values()).map(
            (simulationType) -> {
                this.persistSimulationResult(simulationType);
                return this.createSimulationPresentation(simulationType);
            }
        );
        var simulations = simulationsStream.collect(Collectors.toCollection(ArrayList::new));
        return simulations;
    }

    private PresentSimulationDto createSimulationPresentation(SimulationType simulationType) {
        var installments = simulationType.getAmortizationSystem()
            .setPresentValue(this.simulationSolicitation.desiredValue())
            .setInterestRate(this.productDbEntity.getInterestRate())
            .setPeriod(this.simulationSolicitation.period())
            .calculateInstallments(this.simulationResultDbEntity)
            .stream()
            .map((installment) -> PresentInstallmentDto.from(installment))
            .collect(Collectors.toCollection(ArrayList::new));

        return new PresentSimulationDto(simulationType, installments);
    }

    private void validateSolicitationPeriodRange() throws InvalidPeriodException {
        if (this.isPeriodRangeValid()) {
            return;
        }

        String errorMessage;
        errorMessage = String.format(
            "O período para uma simulação deste valor deve ser maior ou igual a %s.",
            this.productDbEntity.getMinMonthsAmount()
        );
        if (! this.productHasBiggestMinValue) {
            errorMessage = String.format(
            "O período para uma simulação deste valor deve estar entre %s e %s.",
                this.productDbEntity.getMinMonthsAmount(),
                this.productDbEntity.getMaxMonthsAmount()
            );
        }

        throw new InvalidPeriodException(errorMessage);
    }

    private boolean isPeriodRangeValid() throws InvalidPeriodException {
        if (this.productHasBiggestMinValue) {
            return this.simulationSolicitation.period() >= this.productDbEntity.getMinMonthsAmount();
        }

        var isPeriodRangeValid =
            this.simulationSolicitation.period() >= this.productDbEntity.getMinMonthsAmount()
            && this.simulationSolicitation.period() <= this.productDbEntity.getMaxMonthsAmount();

        return isPeriodRangeValid;
    }

    @Transactional
    protected void persistSimulation() {
        var simulation = new SimulationTable();
        simulation.desiredValue = this.simulationSolicitation.desiredValue();
        simulation.period = this.simulationSolicitation.period();
        simulation.interestRate = this.productDbEntity.getInterestRate();
        simulation.product = this.productDbEntity;
        simulation.persist();
        this.simulationDbEntity = simulation;
    }

    @Transactional
    protected void persistSimulationResult(SimulationType simulationType) {
        var simulationResultDbEntity = new SimulationResultTable();
        simulationResultDbEntity.type = simulationType;
        simulationResultDbEntity.simulation = this.simulationDbEntity;
        simulationResultDbEntity.persist();
        this.simulationResultDbEntity = simulationResultDbEntity;
    }
}
