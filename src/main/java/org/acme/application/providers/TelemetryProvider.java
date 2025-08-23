package org.acme.application.providers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.acme.domain.dtos.metrics.MicrometerTimer;
import org.acme.infrastructure.repository.StatisticsRepository;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TelemetryProvider implements ContainerResponseFilter {
    private @Inject StatisticsRepository statsRepository;
    private @Inject MeterRegistry registry;
    private String method;
    private String uri;
    private Integer status;
    private MicrometerTimer timer;

    @Override
    @Transactional
    public void filter(
        ContainerRequestContext requestContext,
        ContainerResponseContext responseContext
    ) throws IOException {
        this.method = requestContext.getMethod();
        this.uri = requestContext.getUriInfo().getPath();
        this.status = responseContext.getStatus();

        if (uri.equals("/metrics")) {
            return;
        }

        Optional<Timer> timerOpt = Optional.ofNullable(
            registry.find("http.server.requests")
            .tag("method", this.method)
            .tag("uri", this.uri)
            .tag("status", Integer.toString(this.status))
            .timer()
        );
        if (timerOpt.isPresent()) {
            this.timer = MicrometerTimer.from(timerOpt.get());
        }

        var telemetryOpt = this.statsRepository.findTelemetryDataByUriMethodAndDate(
            this.uri,
            this.method,
            LocalDate.now()
        );
        if (telemetryOpt.isEmpty()) {
            this.statsRepository.createNewTelemetry(
                this.method,
                this.uri,
                this.status,
                this.timer
            );
            return;
        }
        var telemetry = telemetryOpt.get();
        this.statsRepository.updateWithRequest(
            telemetry,
            this.status,
            this.timer
        );
    }
}
