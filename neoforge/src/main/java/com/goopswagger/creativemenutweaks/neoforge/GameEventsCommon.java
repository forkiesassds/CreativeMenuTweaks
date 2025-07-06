package com.goopswagger.creativemenutweaks.neoforge;

import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.goopswagger.creativemenutweaks.data.DataItemGroupLoader;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = CreativeMenuTweaks.MOD_ID)
public class GameEventsCommon {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        if (player != null) {
            DataItemGroupLoader.onDataPackContentSync(player, true);
            return;
        }

        PlayerManager list = event.getPlayerList();
        DataItemGroupLoader.afterDataPackReload(list, list.getServer());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        DataItemGroupLoader.dataSetup(event.getServer());
    }
}
