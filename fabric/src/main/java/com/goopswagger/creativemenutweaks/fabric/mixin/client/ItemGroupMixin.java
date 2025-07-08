package com.goopswagger.creativemenutweaks.fabric.mixin.client;

import com.goopswagger.creativemenutweaks.client.imixin.PaginatedItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements PaginatedItemGroup, FabricItemGroupImpl {
    @Override
    public int getPage() {
        return fabric_getPage();
    }

    @Override
    public void setPage(int page) {
        fabric_setPage(page);
    }
}
