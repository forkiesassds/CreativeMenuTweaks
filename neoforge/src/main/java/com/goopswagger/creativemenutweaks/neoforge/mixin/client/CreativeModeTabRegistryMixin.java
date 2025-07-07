package com.goopswagger.creativemenutweaks.neoforge.mixin.client;

import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import net.minecraft.item.ItemGroup;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(CreativeModeTabRegistry.class)
public abstract class CreativeModeTabRegistryMixin {
    @Inject(method = "getSortedCreativeModeTabs", at = @At(value = "TAIL"), cancellable = true)
    private static void addCustomTabs(CallbackInfoReturnable<List<ItemGroup>> cir) {
        List<ItemGroup> original = cir.getReturnValue();
        List<ItemGroup> modified = ItemGroupUtil.addCustomItemGroups(original);

        int offset = 0;
        for (ItemGroup group : modified) {
            if (group.getType() != ItemGroup.Type.CATEGORY || group.isSpecial())
                continue;

            group.row = offset < 5 ? ItemGroup.Row.TOP : ItemGroup.Row.BOTTOM;
            offset = ++offset % 10;
        }

        cir.setReturnValue(Collections.unmodifiableList(modified));
    }
}
