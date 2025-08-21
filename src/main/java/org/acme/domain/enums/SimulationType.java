package org.acme.domain.enums;

import org.acme.domain.models.amortization.AmortizationSystem;
import org.acme.domain.models.amortization.Price;
import org.acme.domain.models.amortization.Sac;

public enum SimulationType {
    SAC,
    PRICE;

    public AmortizationSystem getAmortizationSystem() {
        return switch (this) {
            case SAC -> new Sac();
            case PRICE -> new Price();
            default -> new Sac();
        };
    }
}
