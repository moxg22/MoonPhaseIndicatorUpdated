package com.moxg.moonphaseindicatoru.client;

import com.moxg.moonphaseindicatoru.MoonPhaseIndicatorUpdated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DynamicTextureIdentifierProvider {
    private static Identifier getTextureIdentifier(Class<?> c, String basePath, String idPrefix, MinecraftClient client, String path) throws IOException, URISyntaxException {
        if (client == null) {
            throw new NullPointerException("client is null");
        }
        InputStream is = null;
        try {
            TextureManager textureManager = client.getTextureManager();
            String fullPath = basePath + path;
            byte[] bytes = Files.readAllBytes(Paths.get(c.getResource(fullPath).toURI()));
            is = new ByteArrayInputStream(bytes);
            NativeImage ni = NativeImage.read(is);
            return textureManager.registerDynamicTexture(MoonPhaseIndicatorUpdated.MOD_ID + "/" + idPrefix + fullPath, new NativeImageBackedTexture(ni));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static Identifier getDefaultTextureIdentifier(String path) throws IOException, URISyntaxException {
        return getDefaultTextureIdentifier(MinecraftClient.getInstance(), path);
    }

    public static Identifier getDefaultTextureIdentifier(MinecraftClient client, String path) throws IOException, URISyntaxException {
        return getTextureIdentifier(MinecraftClient.class, "/assets/minecraft/", "default", client, path);
    }
    
    public static Identifier getModTextureIdentifier(String path) throws IOException, URISyntaxException {
        return getModTextureIdentifier(MinecraftClient.getInstance(), path);
    }

    public static Identifier getModTextureIdentifier(MinecraftClient client, String path) throws IOException, URISyntaxException {
        return getTextureIdentifier(MoonPhaseIndicatorUpdated.class, "/assets/moonphaseindicator/", MoonPhaseIndicatorUpdated.MOD_ID, client, path);
    }
}
