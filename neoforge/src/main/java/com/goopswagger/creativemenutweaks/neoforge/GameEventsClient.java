package com.goopswagger.creativemenutweaks.neoforge;

import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.goopswagger.creativemenutweaks.CreativeMenuTweaksClient;
import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = CreativeMenuTweaks.MOD_ID, value = Dist.CLIENT)
public class GameEventsClient {
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        CreativeMenuTweaksClient.onDisconnect(MinecraftClient.getInstance());
    }
}
