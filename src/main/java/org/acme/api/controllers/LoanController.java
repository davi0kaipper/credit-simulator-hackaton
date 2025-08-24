package org.acme.api.controllers;

import java.util.ArrayList;

import org.acme.api.RouteList;
import org.acme.application.usecases.ListSimulationsUC;
import org.acme.application.usecases.SimulateLoanUC;
import org.acme.domain.dtos.SimulationSolicitationDto;
import org.acme.domain.exceptions.InvalidDesiredValueException;
import org.acme.domain.exceptions.InvalidPageNumberException;
import org.acme.domain.exceptions.InvalidPeriodException;
import org.acme.domain.exceptions.InvalidRecordAmountByPageException;
import org.acme.domain.payloads.errors.ErrorResponse;
import org.acme.domain.payloads.loan.simulation.ListSimulationsResponse;
import org.acme.domain.payloads.loan.simulation.SimulationRequest;
import org.acme.domain.payloads.loan.simulation.SimulationsResultResponse;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
@Path("/loan")
public class LoanController {
    private @Inject ListSimulationsUC listSimulationsUC;
    private @Inject SimulateLoanUC simulateLoanUC;

    @POST
    @Path("/simulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulateLoan(SimulationRequest request) {
        Response.ResponseBuilder response;
        try {
            var simulationSolicitation = new SimulationSolicitationDto(
                request.valorDesejado(),
                request.prazo()
            );
            var simulationsResultDto = simulateLoanUC.execute(simulationSolicitation);
            var simulationsResult = SimulationsResultResponse.from(simulationsResultDto);
            response = Response.ok(simulationsResult);
        }
        catch (InvalidDesiredValueException | InvalidPeriodException e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.SIMULATE_LOAN_PATH
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
        } catch (NoResultException e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add("Ocorrou um erro.");
            var unprocessableEntityCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            var errorResponse = ErrorResponse.from(
                "Server Error",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.SIMULATE_LOAN_PATH
            );
            response = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse);
        }
        return response.build();
    }

    @GET
    @Path("/simulations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulations(
        @QueryParam("pagina") Integer page,
        @QueryParam("qtdRegistrosPagina") Integer recordsAmountByPage
    ){
        Response.ResponseBuilder response;
        try {
            var list = listSimulationsUC.execute(page, recordsAmountByPage);
            var simulationsList = ListSimulationsResponse.from(list);
            response = Response.ok(simulationsList);
        } catch (InvalidPageNumberException | InvalidRecordAmountByPageException e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.LIST_SIMULATIONS_PATH
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
        }
        return response.build();
    }
}
