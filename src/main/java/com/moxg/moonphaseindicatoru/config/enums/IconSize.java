package com.moxg.moonphaseindicatoru.config.enums;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;

public enum IconSize {
    SMALL("small", 16),
    MEDIUM("medium", 32),
    LARGE("large", 48),
    LARGEST("largest", 64);

    private final String label;
    private final int size;
    private final String KEY_PREFIX = "text.autoconfig." + MoonPhaseIndicatorUpdated.MOD_ID + ".option.iconSize.";

    private IconSize(String label, int size) {
        this.label = KEY_PREFIX + label;
        this.size = size;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public int getSize() {
        return this.size;
    }
}
