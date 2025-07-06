package com.goopswagger.creativemenutweaks.neoforge.mixin;

import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ItemGroups.class)
public class ItemGroupsMixin {
    //HACK: Since Architectury Loom is a pile of garbage, and doesn't remap targets for methods within lambdas whatsoever,
    //  we have to do this ugly mess, where we target the Mojmap name rather than the one that our mappings use.
    @SuppressWarnings({"UnresolvedMixinReference", "InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @WrapOperation(method = "method_51316", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;iterator()Ljava/util/Iterator;", remap = false))
    private static Iterator<ItemGroup> addCustomItemGroupsToSearch(Registry<ItemGroup> instance, Operation<Iterator<ItemGroup>> original) {
        List<ItemGroup> groups = new ArrayList<>();
        Iterator<ItemGroup> it = original.call(instance);

        while (it.hasNext()) {
            ItemGroup group = it.next();
            groups.add(group);
        }

        List<ItemGroup> modified = ItemGroupUtil.addCustomItemGroups(groups);
        return modified.iterator();
    }
}
