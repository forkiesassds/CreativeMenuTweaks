package me.icanttellyou.tweakedcreativemenu.client;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

import java.util.Set;
import java.util.stream.Collectors;

public class CreativeMenuConstants {
    public static Set<ItemGroup> COMMON_GROUPS = Set.of(ItemGroups.SEARCH, ItemGroups.INVENTORY, ItemGroups.HOTBAR, ItemGroups.OPERATOR)
            .stream().map(Registries.ITEM_GROUP::getOrThrow).collect(Collectors.toSet());
    public static final int TABS_PER_PAGE = 10;
}
