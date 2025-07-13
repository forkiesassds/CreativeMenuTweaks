package me.icanttellyou.tweakedcreativemenu.fabric.client;

import me.icanttellyou.tweakedcreativemenu.client.TweakedCreativeMenuClient;
import me.icanttellyou.tweakedcreativemenu.client.config.Config;
import me.icanttellyou.tweakedcreativemenu.networking.S2CPayloadHandlers;
import me.icanttellyou.tweakedcreativemenu.networking.payload.ClearDataGroupManagerPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupCategoryPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class TweakedCreativeMenuFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TweakedCreativeMenuClient.config = Config.readConfig(FabricLoader.getInstance().getConfigDir());

        ClientPlayNetworking.registerGlobalReceiver(ClearDataGroupManagerPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onClearDataGroupManager(payload, context.client()));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupCategoryPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onSyncDataGroupCategory(payload, context.client()));
        ClientPlayNetworking.registerGlobalReceiver(SyncDataGroupEntriesPayload.ID, (payload, context) ->
                S2CPayloadHandlers.onSyncDataGroupEntries(payload, context.client()));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                TweakedCreativeMenuClient.onDisconnect(client));
    }
}
