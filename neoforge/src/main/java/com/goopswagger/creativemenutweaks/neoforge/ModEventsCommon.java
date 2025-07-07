package com.goopswagger.creativemenutweaks.neoforge;

import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupLoader;
import com.goopswagger.creativemenutweaks.neoforge.network.NetworkHelperImpl;
import com.goopswagger.creativemenutweaks.neoforge.util.NeoForgeDummyItemGroup;
import com.goopswagger.creativemenutweaks.networking.S2CPayloadHandlers;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import net.minecraft.client.MinecraftClient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = CreativeMenuTweaks.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        CreativeMenuTweaks.DUMMY_SUPPLIER = NeoForgeDummyItemGroup::new;
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DataItemGroupLoader.ITEM_GROUPS, DataItemGroup.CODEC);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        CreativeMenuTweaks.setNetworkHelper(new NetworkHelperImpl());

        PayloadRegistrar registrar = event.registrar("1").optional();
        MinecraftClient client = MinecraftClient.getInstance();

        registrar.playToClient(
                ClearDataGroupManagerPayload.ID,
                ClearDataGroupManagerPayload.CODEC,
                (payload, context) ->
                        S2CPayloadHandlers.onClearDataGroupManager(payload, client)
        );
        registrar.playToClient(
                SyncDataGroupCategoryPayload.ID,
                SyncDataGroupCategoryPayload.CODEC,
                (payload, context) ->
                        S2CPayloadHandlers.onSyncDataGroupCategory(payload, client)
        );
        registrar.playToClient(
                SyncDataGroupEntriesPayload.ID,
                SyncDataGroupEntriesPayload.CODEC,
                (payload, context) ->
                        S2CPayloadHandlers.onSyncDataGroupEntries(payload, client)
        );
    }
}
