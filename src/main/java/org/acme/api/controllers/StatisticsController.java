package org.acme.api.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.acme.api.RouteList;
import org.acme.application.usecases.PresentApplicationMetricsUC;
import org.acme.application.usecases.PresentSimulationsMetricsUC;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.domain.payloads.errors.ErrorResponse;
import org.acme.domain.payloads.loan.simulation.SimulationsSummaryResponse;
import org.acme.domain.payloads.metrics.ApplicationMetricsResponse;

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
    private @Inject PresentApplicationMetricsUC presentApplicationMetricsUC;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationMetrics(@QueryParam("dataReferencia") String date) {
        Response.ResponseBuilder response;
        try {
            LocalDate referenceDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            var metrics = presentApplicationMetricsUC.execute(referenceDate);
            var applicationMetrics = ApplicationMetricsResponse.from(metrics);
            response = Response.ok(applicationMetrics);
        } catch (InvalidReferenceDate e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.SHOW_APPLICATION_METRICS_PATH
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
        }
        return response.build();
    }

    @GET
    @Path("/simulations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationsMetrics(@QueryParam("dataReferencia") String date ) {
        Response.ResponseBuilder response;
        try {
            LocalDate referenceDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            var metrics = presentSimulationsMetricsUC.execute(referenceDate);
            var simulationsMetrics = SimulationsSummaryResponse.from(metrics);
            response = Response.ok(simulationsMetrics);
            return response.build();
        } catch (InvalidReferenceDate e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add(e.getMessage());
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.SHOW_SIMULATIONS_METRICS_PATH
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
            return response.build();
        } catch (DateTimeParseException e) {
            var errorMessages = new ArrayList<String>();
            errorMessages.add("Entre uma data válida, no formato aaaa-MM-dd");
            var unprocessableEntityCode = 422;
            var errorResponse = ErrorResponse.from(
                "Unprocessable Entity",
                String.valueOf(unprocessableEntityCode),
                errorMessages,
                RouteList.SHOW_SIMULATIONS_METRICS_PATH
            );
            response = Response.status(unprocessableEntityCode).entity(errorResponse);
            return response.build();
        }
    }
}
