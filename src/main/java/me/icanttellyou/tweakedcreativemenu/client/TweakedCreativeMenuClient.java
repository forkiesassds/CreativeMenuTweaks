package me.icanttellyou.tweakedcreativemenu.client;

import me.icanttellyou.tweakedcreativemenu.client.config.Config;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroupManager;
import net.minecraft.client.MinecraftClient;

public class TweakedCreativeMenuClient {
    public static Config config;

    public static void onDisconnect(MinecraftClient client) {
        client.execute(DataItemGroupManager::clear);
    }
}
