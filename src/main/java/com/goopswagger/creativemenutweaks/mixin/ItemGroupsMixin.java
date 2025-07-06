package com.goopswagger.creativemenutweaks.mixin;

import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {
    @Inject(method = {"getGroupsToDisplay", "getGroups"}, at = @At(value = "TAIL"), cancellable = true)
    private static void getGroups(CallbackInfoReturnable<List<ItemGroup>> cir) {
        cir.setReturnValue(ItemGroupUtil.addCustomItemGroups(cir.getReturnValue()));
    }
}
