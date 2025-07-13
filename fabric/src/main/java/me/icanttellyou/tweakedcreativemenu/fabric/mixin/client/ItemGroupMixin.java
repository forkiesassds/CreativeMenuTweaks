package me.icanttellyou.tweakedcreativemenu.fabric.mixin.client;

import me.icanttellyou.tweakedcreativemenu.client.imixin.PaginatedItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements PaginatedItemGroup, FabricItemGroupImpl {
    @Override
    public int tweaked_creative_menu$getPage() {
        return fabric_getPage();
    }

    @Override
    public void tweaked_creative_menu$setPage(int page) {
        fabric_setPage(page);
    }
}
