package com.moxg.moonphaseindicatoru;

import com.moxg.moonphaseindicatoru.config.ModConfig;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
        };
    }
}
