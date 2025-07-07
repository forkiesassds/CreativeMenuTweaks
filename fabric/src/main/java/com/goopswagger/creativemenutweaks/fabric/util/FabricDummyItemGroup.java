package com.goopswagger.creativemenutweaks.fabric.util;

import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FabricDummyItemGroup extends ItemGroup implements FabricItemGroupImpl, DummyItemGroup {
    private static final int TABS_PER_PAGE = 10;
    private final Identifier identifier;

    protected int page;

    public FabricDummyItemGroup(Identifier identifier) {
        super(null, -1, Type.CATEGORY, Text.of("itemgroup.name"), () -> new ItemStack(Items.AIR), (displayContext, entries) -> {});
        this.identifier = identifier;
    }

    @Override
    public int adjust(List<ItemGroup> list, int i) {
        final List<ItemGroup> sortedItemGroups = list.stream()
                .filter(group -> group.getType() == Type.CATEGORY && !group.isSpecial())
                .filter(ItemGroup::shouldDisplay)
                .toList();

        int count = sortedItemGroups.size() + i;
        this.page = ((count / TABS_PER_PAGE));
        int pageIndex = count % TABS_PER_PAGE;
        ItemGroup.Row row = pageIndex < (TABS_PER_PAGE / 2) ? ItemGroup.Row.TOP : ItemGroup.Row.BOTTOM;
        this.row = row;
        this.column = (row == ItemGroup.Row.TOP ? pageIndex % TABS_PER_PAGE : (pageIndex - TABS_PER_PAGE / 2) % (TABS_PER_PAGE));

        return count;
    }

    @Override
    public boolean shouldDisplay() {
        return true;
    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public int fabric_getPage() {
        return page;
    }

    @Override
    public void fabric_setPage(int page) {
        this.page = page;
    }
}
