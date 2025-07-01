package com.goopswagger.creativemenutweaks.data;

import com.google.common.collect.Maps;
import com.goopswagger.creativemenutweaks.networking.payload.ClearDataGroupManagerPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataItemGroupManager {
    public static final HashMap<Identifier, DataItemGroup> groupData = Maps.newHashMap();

    public static List<Map.Entry<Identifier, DataItemGroup>> getModifiedGroups() {
        return groupData.entrySet().stream().filter(dataItemGroup -> Registries.ITEM_GROUP.get(dataItemGroup.getKey()) != null).toList();
    }

    public static List<Map.Entry<Identifier, DataItemGroup>> getCustomGroups() {
         return groupData.entrySet().stream().filter(dataItemGroup -> Registries.ITEM_GROUP.get(dataItemGroup.getKey()) == null).toList();
    }

    public static boolean update = false;

    public static void clear() {
        groupData.clear();
        update = true;
    }

    public static void setup(ReloadableRegistries.Lookup lookup) {
        Registry<DataItemGroup> registry = lookup.getRegistryManager().get(DataItemGroupLoader.ITEM_GROUPS);
        List<DataItemGroup> list = registry.stream().toList();

        for (DataItemGroup groupOutput : list) {
            groupData.put(groupOutput.id, groupOutput);
        }
    }

    public static void sync(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new ClearDataGroupManagerPayload());
        groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.sync(player));
    }
}
