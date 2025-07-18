package me.icanttellyou.tweakedcreativemenu.client;

import me.icanttellyou.tweakedcreativemenu.client.config.Config;
import me.icanttellyou.tweakedcreativemenu.data.DataItemGroupManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;

import java.util.List;
import java.util.function.Supplier;

public class TweakedCreativeMenuClient {
    public static Supplier<List<ItemGroup>> ITEM_GROUP_GETTER = ItemGroups::getGroupsToDisplay;
    public static Config config;

    public static void onDisconnect(MinecraftClient client) {
        client.execute(DataItemGroupManager::clear);
    }
}
