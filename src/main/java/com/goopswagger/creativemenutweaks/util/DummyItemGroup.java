package com.goopswagger.creativemenutweaks.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import java.util.List;

public interface DummyItemGroup {
    int adjust(List<ItemGroup> list, int i);
    Identifier getIdentifier();
}
