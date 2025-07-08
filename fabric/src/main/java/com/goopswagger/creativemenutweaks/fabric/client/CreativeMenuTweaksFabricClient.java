package com.goopswagger.creativemenutweaks.fabric.client;

import com.goopswagger.creativemenutweaks.client.CreativeMenuTweaksClient;
import com.goopswagger.creativemenutweaks.client.config.Config;
import com.goopswagger.creativemenutweaks.networking.S2CPayloadHandlers;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class CreativeMenuTweaksFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CreativeMenuTweaksClient.config = Config.readConfig(FabricLoader.getInstance().getConfigDir());

        ClientPlayNetworking.registerGlobalReceiver(ClearDataGroupManagerPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onClearDataGroupManager(payload, context.client()));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupCategoryPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onSyncDataGroupCategory(payload, context.client()));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupEntriesPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onSyncDataGroupEntries(payload, context.client()));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                CreativeMenuTweaksClient.onDisconnect(client));
    }
}
