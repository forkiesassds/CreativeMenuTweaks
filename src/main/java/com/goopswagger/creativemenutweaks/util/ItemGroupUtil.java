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
        else if (Registries.ITEM_GROUP.getId(group) != null) {
            return Registries.ITEM_GROUP.getId(group);
        }
        return null;
    }

    public static List<ItemGroup> addCustomItemGroups(List<ItemGroup> original) {
        List<ItemGroup> groups = new ArrayList<>(original);
        int offset = 0;
        for (DataItemGroup data : DataItemGroupManager.getCustomGroups().values()) {
            DummyItemGroup group = data.getDummyItemGroup();
            //noinspection SuspiciousMethodCalls
            if (!groups.contains(group) && ((ItemGroup) group).hasStacks()) {
                int index = group.adjust(original, offset);
                groups.add(index, (ItemGroup) group);
                offset++;
            }
        }

        return groups;
    }
}
