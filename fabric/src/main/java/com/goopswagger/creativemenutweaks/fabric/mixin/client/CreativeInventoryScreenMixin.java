package com.goopswagger.creativemenutweaks.fabric.mixin.client;

import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import net.fabricmc.fabric.api.client.itemgroup.v1.FabricCreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreativeInventoryScreen.class, priority = 1001)
public abstract class CreativeInventoryScreenMixin {
    @Inject(method = "init", at = @At(value = "TAIL"))
    private void init(CallbackInfo ci) {
        if (DataItemGroupManager.update) {
            while (((FabricCreativeInventoryScreen) this).getCurrentPage() > 0)
                ((FabricCreativeInventoryScreen) this).switchToPreviousPage();
        }
    }
}
