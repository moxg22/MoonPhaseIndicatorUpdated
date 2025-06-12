package com.moxg.moonphaseindicatoru.utils;

import com.moxg.moonphaseindicatoru.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class ShowIndicatorUtils {
    public static boolean showIndicatorInCurrentDimension(MinecraftClient client) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientWorld world = client.world;

        if(world == null) {
            return false;
        }

        boolean showIndicator = false;
        switch (config.showIndicatorIn) {
            case OVERWORLD:
                showIndicator = world.getDimensionKey().getValue().toString().equals("minecraft:overworld");
                break;
            case NETHER_AND_END:
                showIndicator = (world.getDimensionKey().getValue().toString().equals("minecraft:the_nether")
                        || world.getDimensionKey().getValue().toString().equals("minecraft:the_end"));
                break;
            default:
                showIndicator = true;
                break;
        }

        return showIndicator;
    }
}
