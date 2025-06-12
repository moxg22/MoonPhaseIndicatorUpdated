package com.moxg.moonphaseindicatoru.client;

import com.moxg.moonphaseindicatoru.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.net.URISyntaxException;

public class TextureIdentifierManager {
    private ModConfig config = null;

    private Identifier MOON_PHASES_RP = Identifier.of("minecraft", "textures/environment/moon_phases.png");
    private Identifier SUN_RP = Identifier.of("minecraft", "textures/environment/sun.png");
    private Identifier MOON_PHASES_DEFAULT;
    private Identifier SUN_DEFAULT;
    private Identifier MOON_PHASES_EMOJI;
    private Identifier SUN_EMOJI;

    public TextureIdentifierManager(MinecraftClient client, ModConfig config) throws IOException, URISyntaxException {
        this.config = config;
        MOON_PHASES_DEFAULT = DynamicTextureIdentifierProvider.getDefaultTextureIdentifier(client, "textures/environment/moon_phases.png");
        SUN_DEFAULT = DynamicTextureIdentifierProvider.getDefaultTextureIdentifier(client, "textures/environment/sun.png");
        MOON_PHASES_EMOJI = getModMoonPhasesTextureIdentifier(client, Style.EMOJI);
        SUN_EMOJI = getModSunTextureIdentifier(client, Style.EMOJI);
    }

    public Identifier getMoonPhasesTextureIdentifier() {
        switch (config.iconStyle) {
            case RESOURCE_PACK:
                return MOON_PHASES_RP;
            case DEFAULT:
                return MOON_PHASES_DEFAULT;
            case EMOJI:
                return MOON_PHASES_EMOJI;
            default:
                return null;
        }
    }

    public Identifier getSunTextureIdentifier() {
        switch (config.iconStyle) {
            case RESOURCE_PACK:
                return SUN_RP;
            case DEFAULT:
                return SUN_DEFAULT;
            case EMOJI:
                return SUN_EMOJI;
            default:
                return null;
        }
    }

    public boolean sunTextureShouldBeBlended() {
        switch(config.iconStyle) {
            case RESOURCE_PACK:
            case DEFAULT:
                return true;
            default:
                return false;
        }
    }

    private Identifier getModMoonPhasesTextureIdentifier(MinecraftClient client, Style style) throws IOException, URISyntaxException {
        return DynamicTextureIdentifierProvider.getModTextureIdentifier(client, Style.EMOJI.moonPhasesTexture);
    }

    private Identifier getModSunTextureIdentifier(MinecraftClient client, Style style) throws IOException, URISyntaxException {
        return DynamicTextureIdentifierProvider.getModTextureIdentifier(client, Style.EMOJI.sunTexture);
    }

    public enum Style {
        EMOJI("mod/textures/emoji/moon_phases.png", "mod/textures/emoji/sun.png");

        public final String moonPhasesTexture;
        public final String sunTexture;

        private Style(String moonPhasesTexture, String sunTexture) {
            this.sunTexture = sunTexture;
            this.moonPhasesTexture = moonPhasesTexture;
        }
    }
}
