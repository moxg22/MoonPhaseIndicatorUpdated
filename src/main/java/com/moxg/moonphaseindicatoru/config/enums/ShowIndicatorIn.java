package com.moxg.moonphaseindicatoru.config.enums;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;

public enum ShowIndicatorIn {
    ALL_DIMENSIONS("allDimensions"),
    OVERWORLD("overworld"),
    NETHER_AND_END("netherAndEnd");

    private final String label;
    private final String message;
    private final String KEY_PREFIX = "text.autoconfig." + MoonPhaseIndicatorUpdated.MOD_ID + ".option.showIndicatorIn.";

    private ShowIndicatorIn(String label) {
        this.label = KEY_PREFIX + label;
        this.message = this.label + ".message";
    }

    @Override
    public String toString() {
        return this.label;
    }

    public String toMessage() {
        return this.message;
    }
}
