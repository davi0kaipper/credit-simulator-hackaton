package org.acme.domain.dtos.metrics;

import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Timer;

public record MicrometerTimer(
    String uri,
    String method,
    String status,
    Long requestsAmount,
    Double averageTime,
    Double minTime,
    Double maxTime
) {
    public static MicrometerTimer from(Timer timer) {
        return new MicrometerTimer(
            timer.getId().getTag("uri"),
            timer.getId().getTag("method"),
            timer.getId().getTag("status"),
            timer.count(),
            timer.max(TimeUnit.MILLISECONDS),
            timer.mean(TimeUnit.MILLISECONDS),
            timer.totalTime(TimeUnit.MILLISECONDS)
        );
    }
}
