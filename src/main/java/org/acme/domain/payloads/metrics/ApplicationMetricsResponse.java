package org.acme.domain.payloads.metrics;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.domain.dtos.metrics.ApplicationMetricsDto;

public record ApplicationMetricsResponse(
    String dataReferencia,
    List<MetricsEndpointFormat> listaEndpoints
){
    public static ApplicationMetricsResponse from(ApplicationMetricsDto applicationMetricsDto) {
        return new ApplicationMetricsResponse(
            applicationMetricsDto.referenceDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
            applicationMetricsDto.endpointsList()
            .stream().map(MetricsEndpointFormat::from)
            .collect(Collectors.toList())
        );
    }
}
