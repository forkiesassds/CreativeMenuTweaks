package com.goopswagger.creativemenutweaks.fabric;

import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupLoader;
import com.goopswagger.creativemenutweaks.fabric.network.NetworkHelperImpl;
import com.goopswagger.creativemenutweaks.fabric.util.FabricDummyItemGroup;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class CreativeMenuTweaksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DynamicRegistries.register(DataItemGroupLoader.ITEM_GROUPS, DataItemGroup.CODEC);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(DataItemGroupLoader::onDataPackContentSync);
        ServerLifecycleEvents.SERVER_STARTED.register(DataItemGroupLoader::dataSetup);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resource, success) ->
                DataItemGroupLoader.afterDataPackReload(server.getPlayerManager(), server));

        CreativeMenuTweaks.setNetworkHelper(new NetworkHelperImpl());
        CreativeMenuTweaks.DUMMY_SUPPLIER = FabricDummyItemGroup::new;

        PayloadTypeRegistry.playS2C().register(ClearDataGroupManagerPayload.ID, ClearDataGroupManagerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupCategoryPayload.ID, SyncDataGroupCategoryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupEntriesPayload.ID, SyncDataGroupEntriesPayload.CODEC);
    }
}










