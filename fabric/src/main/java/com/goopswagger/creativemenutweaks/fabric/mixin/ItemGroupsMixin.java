package com.goopswagger.creativemenutweaks.fabric.mixin;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {
    @Shadow
    private static Stream<ItemGroup> stream() {
        throw new RuntimeException("Mixin failed.");
    }

    @Inject(method = {"getGroupsToDisplay", "getGroups"}, at = @At(value = "TAIL"), cancellable = true)
    private static void getGroups(CallbackInfoReturnable<List<ItemGroup>> cir) {
        List<ItemGroup> groups = new ArrayList<>(cir.getReturnValue());
        int offset = 0;
        for (DataItemGroup data : DataItemGroupManager.getCustomGroups().values()) {
            DummyItemGroup group = data.getDummyItemGroup();
            //noinspection SuspiciousMethodCalls
            if (!groups.contains(group)) {
                int index = group.adjust(stream(), offset);
                groups.add(index, (ItemGroup) group);
                offset++;
            }
        }
        cir.setReturnValue(groups);
    }
}
