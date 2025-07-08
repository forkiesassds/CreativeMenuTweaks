package com.goopswagger.creativemenutweaks.util;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupUtil {
    public static Identifier getGroupIdentifier(ItemGroup group) {
        if (group instanceof DummyItemGroup dummyGroup)
            return dummyGroup.getIdentifier();

        return Registries.ITEM_GROUP.getId(group);
    }

    public static List<ItemGroup> addCustomItemGroups(List<ItemGroup> original) {
        List<ItemGroup> groups = new ArrayList<>(original);
        for (DataItemGroup data : DataItemGroupManager.getCustomGroups().values()) {
            DummyItemGroup group = data.getDummyItemGroup();
            //noinspection SuspiciousMethodCalls
            if (!groups.contains(group) && ((ItemGroup) group).hasStacks()) {
                groups.add((ItemGroup) group);
            }
        }

        return groups;
    }
}
