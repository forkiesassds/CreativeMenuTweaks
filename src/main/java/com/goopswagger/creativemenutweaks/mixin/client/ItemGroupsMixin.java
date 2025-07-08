package com.goopswagger.creativemenutweaks.mixin.client;

import com.goopswagger.creativemenutweaks.client.CreativeMenuConstants;
import com.goopswagger.creativemenutweaks.client.imixin.PaginatedItemGroup;
import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
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

import static com.goopswagger.creativemenutweaks.client.CreativeMenuConstants.TABS_PER_PAGE;

@Mixin(value = ItemGroups.class, priority = 999)
public abstract class ItemGroupsMixin {
    @Inject(method = {"getGroupsToDisplay", "getGroups"}, at = @At(value = "TAIL"), cancellable = true)
    private static void getGroups(CallbackInfoReturnable<List<ItemGroup>> cir) {
        cir.setReturnValue(ItemGroupUtil.addCustomItemGroups(cir.getReturnValue()));
    }

    @Inject(method = "updateEntries", at = @At("TAIL"), cancellable = true)
    private static void adjustGroupOrder(ItemGroup.DisplayContext displayContext, CallbackInfo ci) {
        int count = 0;

        for (ItemGroup group : ItemGroups.getGroupsToDisplay()) {
            final PaginatedItemGroup paginatedItemGroup = (PaginatedItemGroup) group;

            if (CreativeMenuConstants.COMMON_GROUPS.contains(paginatedItemGroup)) {
                paginatedItemGroup.setPage(0);
                continue;
            }

            paginatedItemGroup.setPage(count / TABS_PER_PAGE);
            int index = count % TABS_PER_PAGE;
            group.row = index < TABS_PER_PAGE / 2 ? ItemGroup.Row.TOP : ItemGroup.Row.BOTTOM;
            group.column = group.row == ItemGroup.Row.TOP ? index % TABS_PER_PAGE : (index - TABS_PER_PAGE / 2) % TABS_PER_PAGE;

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
