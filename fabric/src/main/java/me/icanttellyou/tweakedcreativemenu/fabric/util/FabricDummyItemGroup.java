package me.icanttellyou.tweakedcreativemenu.fabric.util;

import me.icanttellyou.tweakedcreativemenu.util.DummyItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FabricDummyItemGroup extends ItemGroup implements DummyItemGroup {
    private final Identifier identifier;

    public FabricDummyItemGroup(Identifier identifier) {
        super(null, -1, Type.CATEGORY, Text.of("itemgroup.name"), () -> new ItemStack(Items.AIR), (displayContext, entries) -> {});
        this.identifier = identifier;
    }

    @Override
    public boolean shouldDisplay() {
        return true;
    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }
}
