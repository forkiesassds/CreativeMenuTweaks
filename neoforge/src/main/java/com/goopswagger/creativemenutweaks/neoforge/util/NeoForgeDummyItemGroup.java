package com.goopswagger.creativemenutweaks.neoforge.util;

import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NeoForgeDummyItemGroup extends ItemGroup implements DummyItemGroup {
    private final Identifier identifier;

    public NeoForgeDummyItemGroup(Identifier identifier) {
        super(new ItemGroup.Builder(null, -1)
                .displayName(Text.of("itemgroup.name"))
                .icon(() -> new ItemStack(Items.AIR))
                .entries((displayContext, entries) -> {})
        );
        this.identifier = identifier;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
