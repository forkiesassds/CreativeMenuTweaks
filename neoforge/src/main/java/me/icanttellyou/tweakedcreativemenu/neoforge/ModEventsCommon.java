package me.icanttellyou.tweakedcreativemenu.neoforge;

import me.icanttellyou.tweakedcreativemenu.TweakedCreativeMenu;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroup;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroupLoader;
import me.icanttellyou.tweakedcreativemenu.neoforge.network.NetworkHelperImpl;
import me.icanttellyou.tweakedcreativemenu.neoforge.util.NeoForgeDummyItemGroup;
import me.icanttellyou.tweakedcreativemenu.networking.S2CPayloadHandlers;
import me.icanttellyou.tweakedcreativemenu.networking.payload.ClearDataGroupManagerPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupCategoryPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupEntriesPayload;
import net.minecraft.client.MinecraftClient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = TweakedCreativeMenu.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        TweakedCreativeMenu.DUMMY_SUPPLIER = NeoForgeDummyItemGroup::new;
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DataItemGroupLoader.ITEM_GROUPS, DataItemGroup.CODEC);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        TweakedCreativeMenu.setNetworkHelper(new NetworkHelperImpl());

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
