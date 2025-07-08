package com.goopswagger.creativemenutweaks.neoforge.mixin.client;

import com.goopswagger.creativemenutweaks.client.imixin.PaginatedItemGroup;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements PaginatedItemGroup {
    @Unique private int page = -1;

    @Override
    public int getPage() {
        if (this.page < 0)
            throw new IllegalStateException("Item group has no page.");

        return this.page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
