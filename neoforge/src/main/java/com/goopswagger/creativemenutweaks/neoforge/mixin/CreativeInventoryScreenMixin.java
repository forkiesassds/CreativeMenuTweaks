package com.goopswagger.creativemenutweaks.neoforge.mixin;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin {
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;", remap = false))
    private List<ItemGroup> getGroups(Stream<ItemGroup> instance, Operation<List<ItemGroup>> original) {
        List<ItemGroup> groups = new ArrayList<>(original.call(instance));
        int offset = 0;
        for (DataItemGroup data : DataItemGroupManager.getCustomGroups().values()) {
            DummyItemGroup group = data.getDummyItemGroup();
            //noinspection SuspiciousMethodCalls
            if (!groups.contains(group)) {
                group.adjust(Registries.ITEM_GROUP.stream(), offset);
                groups.add((ItemGroup) group);
                offset++;
            }
        }
        return groups;
    }
}
