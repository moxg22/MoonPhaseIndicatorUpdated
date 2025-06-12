package com.moxg.moonphaseindicatoru.client;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;
import com.moxg.moonphaseindicatoru.config.ModConfig;
import com.moxg.moonphaseindicatoru.event.StatsHandler;
import com.moxg.moonphaseindicatoru.utils.ShowIndicatorUtils;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

public class MoonPhaseOverlay implements HudRenderCallback {
    private static final String MOON_PHASE_KEY_PREFIX = "text.autoconfig." + MoonPhaseIndicatorUpdated.MOD_ID + ".moonPhase.";

    private TextureIdentifierManager textureIdentifierManager = null;

    // scaled in compact mode
    private static final int MAX_TEXT_WIDTH = 82;
    private static final int FONT_GAP = 3;

    // not scaled in compact mode
    private static final int PADDING = 2;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientWorld world = null;
        if (client.world != null) {
            world = client.world;
        }

        if (client != null && world != null && config.indicatorVisibility) {
            if (ShowIndicatorUtils.showIndicatorInCurrentDimension(client)) {
                if (textureIdentifierManager == null) {
                    try {
                        textureIdentifierManager = new TextureIdentifierManager(client, config);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                TextRenderer textRenderer = client.textRenderer;
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                double scaleFactor = client.getWindow().getScaleFactor();
                float scale = (config.compactMode && scaleFactor % 2 == 0 ? 0.5f : 1f);

                int scaledTextHeight = (int) ((scale * ((textRenderer.fontHeight * 3) + (FONT_GAP * 2))));
                int scaledTextWidth = (int) (scale * (MAX_TEXT_WIDTH));
                int scaledIconSize = (int) (scale * config.iconSize.getSize());
                int scaledIconY = (scaledIconSize < scaledTextHeight ? (scaledTextHeight - scaledIconSize) / 2 : 0);
                int scaledTextY = (scaledTextHeight < scaledIconSize ? (scaledIconSize - scaledTextHeight) / 2 : 0);

                int scaledTotalWidth = (int) ((PADDING * 3) + scaledIconSize + scaledTextWidth);
                int scaledTotalHeight = (int) ((PADDING * 2) + Math.max(scaledTextHeight, scaledIconSize));

                int phase = world.getMoonPhase();
                String percentage = String.valueOf((int) (world.getMoonSize() * 100) + "%");

                long totalSecondsRemaining = (24000 - (world.getTimeOfDay() % 24000)) / 20;
                long minutesRemaining = totalSecondsRemaining / 60;
                long secondsRemaining = totalSecondsRemaining % 60;
                String timeRemaining = String.format("%02d:%02d", minutesRemaining, secondsRemaining);

                String direction = (phase < 4 ? "↓" : "↑");
                int directionColour = (phase < 4 ? 0xff0000 : 0x00ff00);
                int redValue = (int) ((Math.min((1 - world.getMoonSize()) * 2, 1)) * 255);
                int greenValue = (int) ((Math.min(world.getMoonSize() * 2, 1)) * 255);
                int percentageColour = (1 << 24) + (redValue << 16) + (greenValue << 8);
                boolean daytime = config.showSunInDay && (world.getTimeOfDay() % 24000) < 12000;

                float phantomPercentageDecimal = StatsHandler.getPhantomSpawnPercentage();
                String phantomPercentage = String.valueOf(Math.round(phantomPercentageDecimal * 100) + "%");
                int phantomRedValue = (int) ((Math.min((1 - phantomPercentageDecimal) * 2, 1)) * 255);
                int phantomGreenValue = (int) ((Math.min(phantomPercentageDecimal * 2, 1)) * 255);
                int phantomPercentageColour = (1 << 24) + (phantomRedValue << 16) + (phantomGreenValue << 8);

                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                if (daytime) {
                    RenderSystem.setShaderTexture(0, textureIdentifierManager.getSunTextureIdentifier());
                } else {
                    RenderSystem.setShaderTexture(0, textureIdentifierManager.getMoonPhasesTextureIdentifier());
                }

                matrixStack.push();

                int horizontalOffset = (int) (((width / 2) - (scaledTotalWidth / 2)) * (config.horizontalOffset / 50f));
                int verticalOffset = (int) (((height / 2) - (scaledTotalHeight / 2)) * (config.verticalOffset / 50f));

                switch (config.indicatorPosition) {
                    case TOP_RIGHT:
                        matrixStack.translate(width - scaledTotalWidth - horizontalOffset, verticalOffset, 0);
                        break;
                    case BOTTOM_LEFT:
                        matrixStack.translate(horizontalOffset, height - scaledTotalHeight - verticalOffset, 0);
                        break;
                    case BOTTOM_RIGHT:
                        matrixStack.translate(width - scaledTotalWidth - horizontalOffset, height - scaledTotalHeight - verticalOffset, 0);
                        break;
                    default:
                        // top left
                        matrixStack.translate(horizontalOffset, verticalOffset, 0);
                        break;
                }

                DrawableHelper.fill(matrixStack, 0, 0, scaledTotalWidth, scaledTotalHeight, (config.backgroundOpacity << 24) + config.backgroundColour);

                matrixStack.translate(PADDING, PADDING, 0);

                if (daytime) {
                    boolean blend = textureIdentifierManager.sunTextureShouldBeBlended();
                    DrawableHelper.fill(matrixStack, 0, scaledIconY, scaledIconSize, scaledIconY + scaledIconSize,
                        0xFF78A7FF);

                    if (blend) {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(SrcFactor.ONE, DstFactor.DST_ALPHA);
                    }

                    if (config.zoomIcon) {
                        DrawableHelper.drawTexture(matrixStack, 0, scaledIconY, scaledIconSize / 2, scaledIconSize / 2,
                                scaledIconSize, scaledIconSize, -scaledIconSize * 2, scaledIconSize * 2);
                    } else {
                        DrawableHelper.drawTexture(matrixStack, 0, scaledIconY, 0, 0, scaledIconSize, scaledIconSize,
                                -scaledIconSize, scaledIconSize);
                    }

                    if (blend) {
                        RenderSystem.defaultBlendFunc();
                        RenderSystem.disableBlend();
                    }
                } else {
                    DrawableHelper.fill(matrixStack, 0, scaledIconY, scaledIconSize, scaledIconY + scaledIconSize,
                        0xFF000000);
                    if (config.zoomIcon) {
                        DrawableHelper.drawTexture(matrixStack, 0, scaledIconY,
                                (((3 - phase) % 4) * scaledIconSize * 2) + (scaledIconSize / 2),
                                ((phase / 4) * scaledIconSize * 2) + (scaledIconSize / 2), scaledIconSize,
                                scaledIconSize,
                                -scaledIconSize * 4 * 2, scaledIconSize * 2 * 2);
                    } else {
                        DrawableHelper.drawTexture(matrixStack, 0, scaledIconY, ((3 - phase) % 4) * scaledIconSize,
                                (phase / 4) * scaledIconSize, scaledIconSize, scaledIconSize, -scaledIconSize * 4,
                                scaledIconSize * 2);
                    }
                }

                matrixStack.translate(scaledIconSize + PADDING, scaledTextY, 0);

                matrixStack.scale(scale, scale, scale);
                DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.translatable(MOON_PHASE_KEY_PREFIX + phase), 0, 0, 0xffffff);

                DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(percentage), 0,
                        textRenderer.fontHeight + FONT_GAP, percentageColour);
                if (config.showPhantomStats) {
                    DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(phantomPercentage), MAX_TEXT_WIDTH / 2,
                        textRenderer.fontHeight + FONT_GAP, phantomPercentageColour);
                }

                DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(direction), 0,
                        (textRenderer.fontHeight + FONT_GAP) * 2, directionColour);
                DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(timeRemaining), 9,
                        (textRenderer.fontHeight + FONT_GAP) * 2, 0xffffff);
                if (config.showPhantomStats) {
                    DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(StatsHandler.getTimeAwakeFormatted()), MAX_TEXT_WIDTH / 2,
                        (textRenderer.fontHeight + FONT_GAP) * 2, 0xffffff);
                }

                matrixStack.pop();
            }
        }
    }
}
