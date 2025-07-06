package com.goopswagger.creativemenutweaks.neoforge.mixin;

import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin {
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;", remap = false))
    private List<ItemGroup> getGroups(Stream<ItemGroup> instance, Operation<List<ItemGroup>> original) {
        List<ItemGroup> groups = original.call(instance);
        return ItemGroupUtil.addCustomItemGroups(groups);
    }
}
