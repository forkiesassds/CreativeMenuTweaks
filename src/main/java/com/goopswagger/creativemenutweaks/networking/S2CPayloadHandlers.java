package com.goopswagger.creativemenutweaks.networking;

import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
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
