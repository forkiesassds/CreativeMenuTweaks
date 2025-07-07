package com.goopswagger.creativemenutweaks.mixin.client;

import com.goopswagger.creativemenutweaks.client.CreativeMenuTweaksClient;
import com.goopswagger.creativemenutweaks.client.Config;
import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CreativeInventoryScreen.class, priority = 1002)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
	@Shadow private static ItemGroup selectedTab;
	@Unique private static ItemGroup knownSelectedTab, hoveredTab;

	public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Shadow protected abstract void setSelectedTab(ItemGroup group);
	@Shadow protected abstract int getTabX(ItemGroup group);
	@Shadow protected abstract int getTabY(ItemGroup group);

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractInventoryScreen;init()V"))
	private void setupKnownSelectedTab(CallbackInfo ci) {
		knownSelectedTab = selectedTab;
	}

	@Inject(method = "init", at = @At(value = "TAIL"))
	private void init(CallbackInfo ci) {
		if (DataItemGroupManager.update) {
			if (knownSelectedTab instanceof DummyItemGroup dummy) {
				Identifier id = dummy.getIdentifier();

				ItemGroup group = ItemGroups.getDefaultTab();
				if (DataItemGroupManager.groupData.containsKey(id)) {
					DataItemGroup dataItemGroup = DataItemGroupManager.groupData.get(id);
					group = (ItemGroup) dataItemGroup.getDummyItemGroup();
				}

				selectedTab = group;
				setSelectedTab(group);
			}

			DataItemGroupManager.update = false;
		}
	}

	@Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;setSelectedTab(Lnet/minecraft/item/ItemGroup;)V", shift = At.Shift.AFTER))
	private void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (!CreativeMenuTweaksClient.config.tabSounds)
			return;

        assert client != null;
        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	@Inject(method = "renderTabTooltipIfHovered", at = @At(value = "HEAD"))
	private void renderTabTooltipIfHovered(DrawContext context, ItemGroup group, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir) {
		hoveredTab = this.isPointWithinBounds(this.getTabX(group) + 3, this.getTabY(group) + 3, 21, 27, mouseX, mouseY) ? group : null;
	}

	@WrapOperation(method = "renderTabTooltipIfHovered", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;II)V"))
	private void changeTooltipDrawing(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, Operation<Void> original, @Local ItemGroup group) {
		if (CreativeMenuTweaksClient.config.tooltipMode == Config.TooltipMode.FLOATING) {
			original.call(instance, textRenderer, text, x, y);
			return;
		}

		int x2 = this.getTabX(group);
		int y2 = this.getTabY(group);
		int offset = 0;
		if (group == selectedTab)
			offset = group.getRow() == ItemGroup.Row.TOP ? -1 : 3;
		instance.drawTooltip(this.textRenderer, text, this.x + x2 - (this.textRenderer.getWidth(text) / 2), this.y + y2 + (group.getRow() == ItemGroup.Row.TOP ? 2 : 32 + 12) + offset);
	}

	@WrapOperation(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
	private void popoutTooltipIcon(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original, @Local ItemGroup group) {
		int offset = 0;
		if (CreativeMenuTweaksClient.config.tabPopout && group != selectedTab && group == hoveredTab) {
			offset = group.getRow() == ItemGroup.Row.TOP ? -1 : 1;
		}
//		instance.drawTexture(texture, x, y + offset, u, v, width, height - offset);
		original.call(instance, texture, x, y + offset, width, height);
	}

	@Inject(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIcon()Lnet/minecraft/item/ItemStack;"))
	private void adjustTabRenderY(DrawContext context, ItemGroup group, CallbackInfo ci, @Local(ordinal = 2) LocalIntRef y) {
		if (!CreativeMenuTweaksClient.config.tabPopout)
			return;

		int offset = 0;
		if (group == selectedTab) {
			offset = group.getRow() == ItemGroup.Row.TOP ? -2 : 4;
		} else if (group == hoveredTab) {
			offset = group.getRow() == ItemGroup.Row.TOP ? -1 : 1;
		}
		y.set(y.get() + offset);
	}
}