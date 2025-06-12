package com.moxg.moonphaseindicatoru.event;


import com.moxg.moonphaseindicatoru.config.ModConfig;
import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;
import com.moxg.moonphaseindicatoru.utils.ShowIndicatorUtils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static final String KEY_CATEGORY = "key.category." + MoonPhaseIndicatorUpdated.MOD_ID;
    private static final String KEY_TOGGLE_INDICATOR = "key." + MoonPhaseIndicatorUpdated.MOD_ID + ".toggleIndicator";
    private static final String KEY_SHOW_CONFIG_SCREEN = "key." + MoonPhaseIndicatorUpdated.MOD_ID + ".showConfigScreen";

    private static KeyBinding toggleIndicatorKey;
    private static KeyBinding showConfigScreenKey;

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if (toggleIndicatorKey.wasPressed() && player != null) {
                ConfigHolder<ModConfig> configHolder = AutoConfig.getConfigHolder(ModConfig.class);
                ModConfig config = configHolder.getConfig();
                if (!ShowIndicatorUtils.showIndicatorInCurrentDimension(client)) {
                    player.sendMessage(Text.translatable(config.showIndicatorIn.toMessage()), true);
                    if (!config.indicatorVisibility) {
                        config.indicatorVisibility = true;
                        configHolder.save();
                    }
                } else {
                    config.indicatorVisibility = !config.indicatorVisibility;
                    configHolder.save();
                }
            }
            if (showConfigScreenKey.wasPressed()) {
                client.setScreen(AutoConfig.getConfigScreen(ModConfig.class, client.currentScreen).get());
            }
        });
    }

    public static void register() {
        toggleIndicatorKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(KEY_TOGGLE_INDICATOR, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, KEY_CATEGORY));
        showConfigScreenKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(KEY_SHOW_CONFIG_SCREEN, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, KEY_CATEGORY));
        registerKeyInputs();
    }
}
