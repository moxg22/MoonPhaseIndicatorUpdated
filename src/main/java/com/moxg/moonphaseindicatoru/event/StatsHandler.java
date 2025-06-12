package com.moxg.moonphaseindicatoru.event;

import com.moxg.moonphaseindicatoru.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.util.ActionResult;

public class StatsHandler {
    private static int timeAwake = 0;
    private static long lastUpdate = 0;

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {
            timeAwake = 0;
            lastUpdate = 0;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            if (config.showPhantomStats) {
                ClientPlayerEntity player = client.player;
                ClientPlayNetworkHandler connection = client.getNetworkHandler();
                ClientWorld world = client.world;
                if (player != null && connection != null && world != null) {
                    long gameTime = world.getTime();
                    if (gameTime > lastUpdate + (config.statsPollIntervalSeconds * 20)) {
                        connection.sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS));
                        lastUpdate = gameTime;
                    }
                }
            } else {
                timeAwake = 0;
                lastUpdate = 0;
            }
        });

        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((manager, data) -> {
            if (data.showPhantomStats) {
                MinecraftClient client = MinecraftClient.getInstance();
                ClientWorld world = client.world;
                if (client != null && world != null) {
                    ClientPlayNetworkHandler connection = client.getNetworkHandler();
                    if (connection != null) {
                        long gameTime = world.getTime();
                        connection.sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS));
                        lastUpdate = gameTime;
                    }
                }
            } else {
                timeAwake = 0;
                lastUpdate = 0;
            }

            return ActionResult.SUCCESS;
        });
    }

    public static void setTimeAwake(int timeAwake) {
        StatsHandler.timeAwake = timeAwake;
    }

    private static int getTimeAwakeSeconds() {
        return timeAwake / 20;
    }

    public static String getTimeAwakeFormatted() {
        float gameDaysAwake = getTimeAwakeSeconds() / 1200f;
        if (gameDaysAwake < 100) {
            return String.format("%.2f", gameDaysAwake);
        }
        return "99.99";
    }

    public static float getPhantomSpawnPercentage() {
        float timeAwakeSeconds = getTimeAwakeSeconds();
        if (timeAwakeSeconds <= (72000 / 20)) {
            return 0;
        }

        return ((timeAwakeSeconds - (72000f / 20f)) / timeAwakeSeconds);
    }
}
