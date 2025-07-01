package com.goopswagger.creativemenutweaks.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

public class DataItemGroupLoader {
    public static RegistryKey<Registry<DataItemGroup>> ITEM_GROUPS = RegistryKey.ofRegistry(Identifier.of("itemgroups"));


    public static void init() {
        DynamicRegistries.register(ITEM_GROUPS, DataItemGroup.CODEC);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            if (joined) {
                DataItemGroupManager.sync(player);
            }
        });

        // frankly i don't know whats goin on here
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            DataItemGroupManager.setup(server.getReloadableRegistries());
            DataItemGroupManager.groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.parseLootTable(server));
        });

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resource, success) -> {
            DataItemGroupManager.setup(server.getReloadableRegistries());
            DataItemGroupManager.groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.parseLootTable(server));
            server.getPlayerManager().getPlayerList().forEach(DataItemGroupManager::sync);
        });
    }
}
