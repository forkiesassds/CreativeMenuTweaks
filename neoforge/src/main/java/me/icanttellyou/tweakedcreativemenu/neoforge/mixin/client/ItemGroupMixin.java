package me.icanttellyou.tweakedcreativemenu.neoforge.mixin.client;

import me.icanttellyou.tweakedcreativemenu.client.imixin.PaginatedItemGroup;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements PaginatedItemGroup {
    @Unique private int tweaked_creative_menu$page = -1;

    @Override
    public int tweaked_creative_menu$getPage() {
        if (this.tweaked_creative_menu$page < 0)
            throw new IllegalStateException("Item group has no page.");

        return this.tweaked_creative_menu$page;
    }

    @Override
    public void tweaked_creative_menu$setPage(int page) {
        this.tweaked_creative_menu$page = page;
    }
}
