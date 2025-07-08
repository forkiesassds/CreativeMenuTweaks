package com.goopswagger.creativemenutweaks.client;

import com.goopswagger.creativemenutweaks.client.config.Config;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import net.minecraft.client.MinecraftClient;

public class CreativeMenuTweaksClient {
    public static Config config;

    public static void onDisconnect(MinecraftClient client) {
        client.execute(DataItemGroupManager::clear);
    }
}
