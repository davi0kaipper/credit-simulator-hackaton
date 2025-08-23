package org.acme.domain.dtos.metrics;

import java.time.LocalDate;
import java.util.List;

public record ApplicationMetricsDto(
    LocalDate referenceDate,
    List<EndpointMetricsDto> endpointsList
) { }
