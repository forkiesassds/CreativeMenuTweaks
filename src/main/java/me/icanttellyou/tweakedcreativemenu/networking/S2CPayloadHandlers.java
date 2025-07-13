package me.icanttellyou.tweakedcreativemenu.networking;

import me.icanttellyou.tweakedcreativemenu.data.DataItemGroupManager;
import me.icanttellyou.tweakedcreativemenu.networking.payload.ClearDataGroupManagerPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupCategoryPayload;
import me.icanttellyou.tweakedcreativemenu.networking.payload.SyncDataGroupEntriesPayload;
import net.minecraft.client.MinecraftClient;

public class S2CPayloadHandlers {
    public static void onClearDataGroupManager(ClearDataGroupManagerPayload payload, MinecraftClient client) {
        client.execute(DataItemGroupManager::clear);
    }

    public static void onSyncDataGroupCategory(SyncDataGroupCategoryPayload payload, MinecraftClient client) {
        client.execute(() -> DataItemGroupManager.groupData.put(payload.id(), payload.group()));
    }

    public static void onSyncDataGroupEntries(SyncDataGroupEntriesPayload payload, MinecraftClient client) {
        client.execute(() -> DataItemGroupManager.groupData.get(payload.id()).items.addAll(payload.stackList()));
    }
}
