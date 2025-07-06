package com.goopswagger.creativemenutweaks.neoforge.util;

import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Stream;

public class NeoForgeDummyItemGroup extends ItemGroup implements DummyItemGroup {
    private static final int TABS_PER_PAGE = 10;
    private final Identifier identifier;

    public NeoForgeDummyItemGroup(Identifier identifier) {
        super(new ItemGroup.Builder(null, -1)
                .displayName(Text.of("itemgroup.name"))
                .icon(() -> new ItemStack(Items.AIR))
                .entries((displayContext, entries) -> {})
        );
        this.identifier = identifier;
    }

    @Override
    public int adjust(Stream<ItemGroup> stream, int i) {
        final List<ItemGroup> sortedItemGroups = stream
                .filter(group -> group.getType() == Type.CATEGORY && !group.isSpecial())
                .filter(ItemGroup::shouldDisplay)
                .toList();

        int count = sortedItemGroups.size() + i;
        int pageIndex = count % TABS_PER_PAGE;
        ItemGroup.Row row = pageIndex < (TABS_PER_PAGE / 2) ? ItemGroup.Row.TOP : ItemGroup.Row.BOTTOM;
        this.row = row;
        this.column = (row == ItemGroup.Row.TOP ? pageIndex % TABS_PER_PAGE : (pageIndex - TABS_PER_PAGE / 2) % (TABS_PER_PAGE));

        return count;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
