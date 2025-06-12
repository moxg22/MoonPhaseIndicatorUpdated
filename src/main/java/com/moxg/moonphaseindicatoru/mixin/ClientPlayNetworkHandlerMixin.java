package com.moxg.moonphaseindicatoru.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.moxg.moonphaseindicatoru.event.StatsHandler;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.stat.Stats;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onStatistics(Lnet/minecraft/network/packet/s2c/play/StatisticsS2CPacket;)V", at = @At("HEAD"))
    private void onStatistics(net.minecraft.network.packet.s2c.play.StatisticsS2CPacket packet, CallbackInfo info) {
        if (packet != null) {
            Integer integer = packet.getStatMap().get(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
            if (integer != null) {
                StatsHandler.setTimeAwake(integer.intValue());
            }
        }
    }
}

