package me.icanttellyou.tweakedcreativemenu.mixin.client;

import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemGroup.class)
public interface ItemGroupAccessor {
    @Accessor("row") @Mutable void setRow(ItemGroup.Row row);
    @Accessor("column") @Mutable void setColumn(int column);
}
