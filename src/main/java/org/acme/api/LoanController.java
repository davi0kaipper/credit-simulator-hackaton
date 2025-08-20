package org.acme.api;

import java.util.ArrayList;

import org.acme.application.usecases.SimulateLoanUC;
import org.acme.domain.dtos.SimulationSolicitationDto;
import org.acme.domain.exceptions.InvalidDesiredValueException;
import org.acme.domain.exceptions.InvalidPeriodException;
import org.acme.domain.payloads.errors.ErrorResponse;
import org.acme.domain.payloads.loan.simulation.SimulationRequest;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/loan")
public class LoanController {
@Inject
private SimulateLoanUC simulateLoan;

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
            var simulationsResult = simulateLoan.execute(simulationSolicitation);
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
                "/loan/simulation"
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
                "/loan/simulation"
            );
            response = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse);
        }

        return response.build();
    }
}
