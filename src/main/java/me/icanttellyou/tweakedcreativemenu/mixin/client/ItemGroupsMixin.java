package me.icanttellyou.tweakedcreativemenu.mixin.client;

import me.icanttellyou.tweakedcreativemenu.client.CreativeMenuConstants;
import me.icanttellyou.tweakedcreativemenu.client.TweakedCreativeMenuClient;
import me.icanttellyou.tweakedcreativemenu.client.imixin.PaginatedItemGroup;
import me.icanttellyou.tweakedcreativemenu.util.ItemGroupUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.icanttellyou.tweakedcreativemenu.client.CreativeMenuConstants.TABS_PER_PAGE;

@Mixin(value = ItemGroups.class, priority = 999)
public abstract class ItemGroupsMixin {
    @Inject(method = {"getGroupsToDisplay", "getGroups"}, at = @At(value = "TAIL"), cancellable = true)
    private static void getGroups(CallbackInfoReturnable<List<ItemGroup>> cir) {
        cir.setReturnValue(ItemGroupUtil.addCustomItemGroups(cir.getReturnValue()));
    }

    @Inject(method = "updateEntries", at = @At("TAIL"), cancellable = true)
    private static void adjustGroupOrder(ItemGroup.DisplayContext displayContext, CallbackInfo ci) {
        int count = 0;

        for (ItemGroup group : TweakedCreativeMenuClient.ITEM_GROUP_GETTER.get()) {
            final PaginatedItemGroup paginatedItemGroup = (PaginatedItemGroup) group;

            if (CreativeMenuConstants.COMMON_GROUPS.contains(paginatedItemGroup)) {
                paginatedItemGroup.tweaked_creative_menu$setPage(0);
                continue;
            }

            paginatedItemGroup.tweaked_creative_menu$setPage(count / TABS_PER_PAGE);
            int index = count % TABS_PER_PAGE;
            final ItemGroupAccessor accessor = (ItemGroupAccessor) group;
            ItemGroup.Row row = index < TABS_PER_PAGE / 2 ? ItemGroup.Row.TOP : ItemGroup.Row.BOTTOM;
            accessor.setRow(row);
            accessor.setColumn(row == ItemGroup.Row.TOP ? index % TABS_PER_PAGE : (index - TABS_PER_PAGE / 2) % TABS_PER_PAGE);

            count++;
        }

        ci.cancel();
    }

    /**
     * @author icanttellyou
     * @reason Cannot use other type of mixin, due to an Architectury Loom bug.
     */
    @Overwrite
    private static void method_51316(Registry<ItemGroup> registry, ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        Set<ItemStack> set = ItemStackSet.create();
        List<ItemGroup> groups = new ArrayList<>();
        registry.iterator().forEachRemaining(groups::add);

        List<ItemGroup> modified = ItemGroupUtil.addCustomItemGroups(groups);
        for (ItemGroup itemGroup : modified) {
            if (itemGroup.getType() != ItemGroup.Type.SEARCH) {
                set.addAll(itemGroup.getSearchTabStacks());
            }
        }

        entries.addAll(set);
    }
}
