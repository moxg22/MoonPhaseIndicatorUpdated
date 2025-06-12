package com.moxg.moonphaseindicatoru.config.enums;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;

public enum IconStyle {
    RESOURCE_PACK("resourcePack"),
    DEFAULT("default"),
    EMOJI("emoji");

    private final String label;
    private final String KEY_PREFIX = "text.autoconfig." + MoonPhaseIndicatorUpdated.MOD_ID + ".option.iconStyle.";

    private IconStyle(String label) {
        this.label = KEY_PREFIX + label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
