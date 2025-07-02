package com.goopswagger.creativemenutweaks.data;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DataItemGroupLoader {
    public static RegistryKey<Registry<DataItemGroup>> ITEM_GROUPS = RegistryKey.ofRegistry(Identifier.of("itemgroups"));

    public static void init() {
        DynamicRegistries.register(ITEM_GROUPS, DataItemGroup.CODEC);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(DataItemGroupLoader::onDataPackContentSync);
        ServerLifecycleEvents.SERVER_STARTED.register(DataItemGroupLoader::onServerStarted);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(DataItemGroupLoader::afterDataPackReload);
    }

    public static void onDataPackContentSync(ServerPlayerEntity player, boolean joined) {
        if (joined) {
            DataItemGroupManager.sync(player);
        }
    }

    public static void onServerStarted(MinecraftServer server) {
        // frankly i don't know whats goin on here
        DataItemGroupManager.setup(server.getReloadableRegistries());
        DataItemGroupManager.groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.setupItems(server));
    }

    public static void afterDataPackReload(MinecraftServer server, LifecycledResourceManager resource, boolean success) {
        DataItemGroupManager.setup(server.getReloadableRegistries());
        DataItemGroupManager.groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.setupItems(server));
        server.getPlayerManager().getPlayerList().forEach(DataItemGroupManager::sync);
    }
}
