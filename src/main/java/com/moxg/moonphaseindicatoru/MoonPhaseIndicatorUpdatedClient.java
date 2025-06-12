package com.moxg.moonphaseindicatoru;

import com.moxg.moonphaseindicatoru.client.MoonPhaseOverlay;
import com.moxg.moonphaseindicatoru.config.ModConfig;
import com.moxg.moonphaseindicatoru.event.KeyInputHandler;
import com.moxg.moonphaseindicatoru.event.StatsHandler;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MoonPhaseIndicatorUpdatedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        HudRenderCallback.EVENT.register(new MoonPhaseOverlay());
        KeyInputHandler.register();
        StatsHandler.register();
    }
}
