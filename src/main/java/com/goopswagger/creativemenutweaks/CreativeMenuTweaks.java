package com.goopswagger.creativemenutweaks;

import com.goopswagger.creativemenutweaks.data.DataItemGroupLoader;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class CreativeMenuTweaks implements ModInitializer {
    @Override
    public void onInitialize() {
        DataItemGroupLoader.init();

        PayloadTypeRegistry.playS2C().register(ClearDataGroupManagerPayload.ID, ClearDataGroupManagerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupCategoryPayload.ID, SyncDataGroupCategoryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDataGroupEntriesPayload.ID, SyncDataGroupEntriesPayload.CODEC);
    }

    public static Identifier makeModID(String path) {
        return Identifier.of("creativemenutweaks", path);
    }
}










