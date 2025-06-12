package com.moxg.moonphaseindicatoru.config.enums;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;

public enum IndicatorPosition {
    TOP_LEFT("topLeft"),
    TOP_RIGHT("topRight"),
    BOTTOM_LEFT("bottomLeft"),
    BOTTOM_RIGHT("bottomRight");

    private final String label;
    private final String KEY_PREFIX = "text.autoconfig." + MoonPhaseIndicatorUpdated.MOD_ID + ".option.indicatorPosition.";

    private IndicatorPosition(String label) {
        this.label = KEY_PREFIX + label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
