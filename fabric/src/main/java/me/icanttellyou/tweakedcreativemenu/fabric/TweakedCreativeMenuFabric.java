package me.icanttellyou.tweakedcreativemenu.fabric;

import me.icanttellyou.tweakedcreativemenu.TweakedCreativeMenu;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroup;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroupLoader;
import me.icanttellyou.tweakedcreativemenu.fabric.network.NetworkHelperImpl;
import me.icanttellyou.tweakedcreativemenu.fabric.util.FabricDummyItemGroup;
import me.icanttellyou.tweakedcreativemenu.networking.payload.ClearDataGroupManagerPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupCategoryPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class TweakedCreativeMenuFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DynamicRegistries.register(DataItemGroupLoader.ITEM_GROUPS, DataItemGroup.CODEC);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(DataItemGroupLoader::onDataPackContentSync);
        ServerLifecycleEvents.SERVER_STARTED.register(DataItemGroupLoader::dataSetup);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resource, success) ->
                DataItemGroupLoader.afterDataPackReload(server.getPlayerManager(), server));

        TweakedCreativeMenu.setNetworkHelper(new NetworkHelperImpl());
        TweakedCreativeMenu.DUMMY_SUPPLIER = FabricDummyItemGroup::new;

        PayloadTypeRegistry.playS2C().register(ClearDataGroupManagerPayload.ID, ClearDataGroupManagerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupCategoryPayload.ID, SyncDataGroupCategoryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupEntriesPayload.ID, SyncDataGroupEntriesPayload.CODEC);
    }
}