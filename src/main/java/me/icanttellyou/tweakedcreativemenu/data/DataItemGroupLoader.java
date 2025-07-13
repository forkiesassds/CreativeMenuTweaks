package me.icanttellyou.tweakedcreativemenu.data;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DataItemGroupLoader {
    public static RegistryKey<Registry<DataItemGroup>> ITEM_GROUPS = RegistryKey.ofRegistry(Identifier.of("itemgroups"));

    public static void onDataPackContentSync(ServerPlayerEntity player, boolean joined) {
        if (joined) {
            DataItemGroupManager.sync(player);
        }
    }

    public static void dataSetup(MinecraftServer server) {
        // frankly i don't know whats goin on here
        DataItemGroupManager.setup(server.getReloadableRegistries());
        DataItemGroupManager.groupData.forEach((identifier, dataItemGroup) -> dataItemGroup.setupItems(server));
    }

    public static void afterDataPackReload(PlayerManager playerManager, MinecraftServer server) {
        dataSetup(server);
        playerManager.getPlayerList().forEach(DataItemGroupManager::sync);
    }
}
