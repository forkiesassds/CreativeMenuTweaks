package com.goopswagger.creativemenutweaks.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public interface DummyItemGroup {
    int adjust(Stream<ItemGroup> stream, int offset, int prevResult);
    Identifier getIdentifier();
}
