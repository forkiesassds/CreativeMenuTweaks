package com.goopswagger.creativemenutweaks;

import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CreativeMenuTweaksClient implements ClientModInitializer {
    @SuppressWarnings("resource")
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClearDataGroupManagerPayload.ID, (payload, context) ->
                context.client().execute(DataItemGroupManager::clear));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupCategoryPayload.ID, (payload, context) ->
                context.client().execute(() -> DataItemGroupManager.groupData.put(payload.id(), payload.group())));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupEntriesPayload.ID, (payload, context) ->
                context.client().execute(() -> DataItemGroupManager.groupData.get(payload.id()).items.addAll(payload.stackList())));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(DataItemGroupManager::clear);
        });
    }
}
