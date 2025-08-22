package org.acme.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.acme.application.usecases.PresentSimulationsMetricsUC;
import org.acme.domain.exceptions.InvalidProductId;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.domain.exceptions.ProductNotFound;
import org.acme.domain.payloads.errors.ErrorResponse;
import org.acme.domain.payloads.generic.ResponseMessage;
import org.acme.domain.payloads.loan.simulation.SimulationsSummaryResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
@Path("/metrics")
public class StatisticsController {
    private @Inject PresentSimulationsMetricsUC presentSimulationsMetricsUC;

    @GET
    @Path("/simulations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationsMetrics(
        @QueryParam("dataReferencia") String date,
        @QueryParam("codigoProduto") Long productId
    ){
        Response.ResponseBuilder response;
        try {
            LocalDate referenceDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            var metrics = presentSimulationsMetricsUC.execute(
                referenceDate,
                productId
            );
            if (metrics.simulationsSummary().interestRateAverage() == null) {
                var text = String.format(
                    "Não há simulações parametrizadas pelo produto '%s' em %s.",
                    metrics.simulationsSummary().productDescription(),
                    referenceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                var message = new ResponseMessage(text);
                response = Response.ok(message);
                return response.build();
            }
            var simulationsMetrics = SimulationsSummaryResponse.from(metrics);
            response = Response.ok(simulationsMetrics);
            return response.build();
        } catch (InvalidReferenceDate | InvalidProductId e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                "/metrics/simulations"
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
            return response.build();
        } catch (DateTimeParseException e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add("Entre uma data válida, no formato aaaa-MM-dd");
            var unprocessableEntityCode = 404;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                "/metrics/simulations"
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
            return response.build();
        } catch (ProductNotFound e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var notFoundCode = 404;
            var errorResponse = ErrorResponse.from(
                "Not Found",
                String.valueOf(notFoundCode),
                errorMessages,
                "/metrics/simulations"
            );
            response = Response.status(Response.Status.NOT_FOUND).entity(errorResponse);
            return response.build();
        }
    }
}
