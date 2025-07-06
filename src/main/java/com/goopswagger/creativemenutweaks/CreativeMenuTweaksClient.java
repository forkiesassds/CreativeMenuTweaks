package com.goopswagger.creativemenutweaks;

import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import net.minecraft.client.MinecraftClient;

public class CreativeMenuTweaksClient {
    public static void onDisconnect(MinecraftClient client) {
        client.execute(DataItemGroupManager::clear);
    }
}
