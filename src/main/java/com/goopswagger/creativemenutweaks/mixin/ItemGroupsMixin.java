package com.goopswagger.creativemenutweaks.mixin;

import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {
    @Inject(method = {"getGroupsToDisplay", "getGroups"}, at = @At(value = "TAIL"), cancellable = true)
    private static void getGroups(CallbackInfoReturnable<List<ItemGroup>> cir) {
        cir.setReturnValue(ItemGroupUtil.addCustomItemGroups(cir.getReturnValue()));
    }

    @WrapOperation(method = "method_51316", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;iterator()Ljava/util/Iterator;"))
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
