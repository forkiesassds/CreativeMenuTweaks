package com.goopswagger.creativemenutweaks.neoforge.mixin;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.minecraft.item.ItemGroup;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(CreativeModeTabRegistry.class)
public abstract class CreativeModeTabRegistryMixin {
    @Inject(method = "getSortedCreativeModeTabs", at = @At(value = "TAIL"), cancellable = true)
    private static void getDisplayStacks(CallbackInfoReturnable<List<ItemGroup>> cir) {
        List<ItemGroup> original = cir.getReturnValue();
        List<ItemGroup> groups = new ArrayList<>(original);
        int offset = 0;
        for (DataItemGroup data : DataItemGroupManager.getCustomGroups().values()) {
            DummyItemGroup group = data.getDummyItemGroup();
            //noinspection SuspiciousMethodCalls
            if (!groups.contains(group)) {
                int index = group.adjust(original.stream(), offset);
                groups.add(index, (ItemGroup) group);
                offset++;
            }
        }
        cir.setReturnValue(Collections.unmodifiableList(groups));
    }
}
