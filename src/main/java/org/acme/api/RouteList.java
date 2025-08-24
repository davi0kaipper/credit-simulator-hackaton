package org.acme.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RouteList {
    public static final String SIMULATE_LOAN_PATH = "/loan/simulation";
    public static final String LIST_SIMULATIONS_PATH = "/loan/simulations";
    public static final String SHOW_APPLICATION_METRICS_PATH = "/metrics";
    public static final String SHOW_SIMULATIONS_METRICS_PATH = "/metrics/simulations";
}
