package com.goopswagger.creativemenutweaks.fabric.util;

import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Stream;

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
    public int adjust(Stream<ItemGroup> stream, int offset, int prevResult) {
        final List<ItemGroup> sortedItemGroups = stream
                .filter(group -> group.getType() == Type.CATEGORY && !group.isSpecial())
                .filter(ItemGroup::shouldDisplay)
                .toList();

        int count = 0;
        boolean foundVoid = false;

        if (prevResult < sortedItemGroups.size()) {
            int pages = sortedItemGroups.size() / TABS_PER_PAGE;
            int resume = prevResult == -1 ? 0 : (prevResult + 1) / TABS_PER_PAGE;
            for (int page = resume; page <= pages; page++) {
                int limit = page == pages ? sortedItemGroups.size() % TABS_PER_PAGE : TABS_PER_PAGE;
                int resumePage = page == resume ? (prevResult + 1) % TABS_PER_PAGE : 0;

                for (int group = resumePage; group < limit; group++) {
                    int index = page * TABS_PER_PAGE + group;

                    if (((FabricItemGroupImpl) sortedItemGroups.get(index)).fabric_getPage() != page) {
                        count = index;
                        foundVoid = true;
                        break;
                    }
                }

                if (foundVoid)
                    break;
            }
        }

        if (!foundVoid)
            count = sortedItemGroups.size() + offset;

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
