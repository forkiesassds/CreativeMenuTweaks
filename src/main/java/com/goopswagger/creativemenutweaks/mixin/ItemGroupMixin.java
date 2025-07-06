package com.goopswagger.creativemenutweaks.mixin;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.data.DataItemGroupManager;
import com.goopswagger.creativemenutweaks.util.ItemGroupUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {
    @Inject(method = "getDisplayStacks", at = @At(value = "TAIL"), cancellable = true)
    private void getDisplayStacks(CallbackInfoReturnable<Collection<ItemStack>> cir) {
        Set<ItemStack> original = (Set<ItemStack>) cir.getReturnValue();
        Collection<ItemStack> newList = modifyStackLists(original, false);

        if (newList != original)
            cir.setReturnValue(newList);
    }

    @Inject(method = "getSearchTabStacks", at = @At(value = "TAIL"), cancellable = true)
    private void getSearchTabStacks(CallbackInfoReturnable<Collection<ItemStack>> cir) {
        Set<ItemStack> original = (Set<ItemStack>) cir.getReturnValue();
        Collection<ItemStack> newList = modifyStackLists(original, true);

        if (newList != original)
            cir.setReturnValue(newList);
    }

    @WrapOperation(
        method = "contains",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/ItemGroup;searchTabStacks:Ljava/util/Set;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Set<ItemStack> hijackContains(ItemGroup instance, Operation<Set<ItemStack>> original) {
        return (Set<ItemStack>) modifyStackLists(original.call(instance), false);
    }

    @WrapOperation(
        method = "hasStacks",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/ItemGroup;displayStacks:Ljava/util/Collection;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Collection<ItemStack> hijackHasStacks(ItemGroup instance, Operation<Collection<ItemStack>> original) {
        return modifyStackLists(original.call(instance), false);
    }

    @Inject(method = "getDisplayName", at = @At(value = "RETURN"), cancellable = true)
    private void getDisplayName(CallbackInfoReturnable<Text> cir) {
        DataItemGroup data = getItemGroupData();

        if (data != null)
            data.optionalName().ifPresent(name -> cir.setReturnValue(Text.translatable(name)));
    }

    @Inject(method = "getIcon", at = @At(value = "RETURN"), cancellable = true)
    private void getIcon(CallbackInfoReturnable<ItemStack> cir) {
        DataItemGroup data = getItemGroupData();

        if (data != null)
            data.optionalIcon().ifPresent(cir::setReturnValue);
    }

    @Unique
    private Collection<ItemStack> modifyStackLists(Collection<ItemStack> original, boolean limitedSize) {
        ItemGroup group = (((ItemGroup) (Object) this));
        Identifier identifier = ItemGroupUtil.getGroupIdentifier(group);
        if (DataItemGroupManager.groupData.containsKey(identifier)) {
            DataItemGroup dataItemGroup = DataItemGroupManager.groupData.get(identifier);
            List<ItemStack> items = dataItemGroup.items();
            if (limitedSize)
                items = items.stream().map(i -> i.getCount() != 1 ? i.copyWithCount(1) : i).toList();

            if (dataItemGroup.replace())
                return new HashSet<>(items);

            original.addAll(items);
        }

        return original;
    }

    @Unique
    private DataItemGroup getItemGroupData() {
        ItemGroup group = (((ItemGroup) (Object) this));
        Identifier identifier = ItemGroupUtil.getGroupIdentifier(group);
        if (DataItemGroupManager.groupData.containsKey(identifier))
            return DataItemGroupManager.groupData.get(identifier);

        return null;
    }
}
