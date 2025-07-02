package com.goopswagger.creativemenutweaks.data;

import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DataItemGroup {
    public interface Entry {
        Codec<Entry> CODEC = Codec.xor(
                ItemEntry.CODEC,
                Codec.xor(
                        TagEntry.CODEC,
                        LootTableEntry.CODEC
                )
        ).xmap(Entry::fromEither, Entry::toEither);

        private static Entry fromEither(Either<ItemEntry, Either<TagEntry, LootTableEntry>> either) {
            return either.map(Function.identity(), Either::unwrap);
        }

        private static Either<ItemEntry, Either<TagEntry, LootTableEntry>> toEither(Entry o) {
            return o instanceof ItemEntry ?
                    Either.left((ItemEntry) o) :
                    Either.right(o instanceof TagEntry ?
                            Either.left((TagEntry) o) :
                            Either.right((LootTableEntry) o)
                    );
        }

        void addItems(List<ItemStack> items, MinecraftServer server);

        record ItemEntry(ItemStack item) implements Entry {
            public static final Codec<ItemEntry> CODEC = ItemStack.CODEC.xmap(ItemEntry::new, ItemEntry::item);

            @Override
            public void addItems(List<ItemStack> items, MinecraftServer server) {
                items.add(item);
            }
        }

        record TagEntry(TagKey<Item> tag) implements Entry {
            public static final Codec<TagEntry> CODEC = TagKey.codec(RegistryKeys.ITEM).fieldOf("tag").codec().xmap(TagEntry::new, TagEntry::tag);

            @Override
            public void addItems(List<ItemStack> items, MinecraftServer server) {
                ReloadableRegistries.Lookup registries = server.getReloadableRegistries();
                Registry<Item> itemRegistry = registries.getRegistryManager().get(RegistryKeys.ITEM);

                Registries.ITEM.streamTagsAndEntries().forEach(pair -> {
                    if (pair.getFirst() == tag) {
                        for (RegistryEntry<Item> r : pair.getSecond()) {
                            Identifier id = r.getKey().orElseThrow().getValue();
                            Item item = itemRegistry.get(id);

                            assert item != null;
                            items.add(item.getDefaultStack());
                        }
                    }
                });
            }
        }

        record LootTableEntry(RegistryKey<LootTable> lootTableId) implements Entry {
            public static final Codec<LootTableEntry> CODEC = RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).fieldOf("loot_table")
                    .codec().xmap(LootTableEntry::new, LootTableEntry::lootTableId);

            @Override
            public void addItems(List<ItemStack> items, MinecraftServer server) {
                ReloadableRegistries.Lookup registries = server.getReloadableRegistries();
                LootContextParameterSet context = new LootContextParameterSet.Builder(server.getWorld(World.OVERWORLD)).build(LootContextTypes.EMPTY);

                LootTable lootTable = registries.getLootTable(lootTableId);
                assert lootTable != null;
                items.addAll(lootTable.generateLoot(context));
            }
        }
    }

    public static final Codec<DataItemGroup> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            Identifier.CODEC.fieldOf("id").forGetter(DataItemGroup::id),
                            Codec.STRING.optionalFieldOf("name").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.name())),
                            ItemStack.CODEC.optionalFieldOf("icon").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.icon)),
                            Codec.BOOL.optionalFieldOf("replace").forGetter(dataItemGroup -> Optional.of(dataItemGroup.replace)),
                            Entry.CODEC.listOf().optionalFieldOf("entries").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.entries)))
                    .apply(instance, DataItemGroup::new));

    public static final PacketCodec<RegistryByteBuf, DataItemGroup> PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, DataItemGroup::id,
            PacketCodecs.STRING.collect(PacketCodecs::optional), dataItemGroup -> Optional.ofNullable(dataItemGroup.name()),
            ItemStack.PACKET_CODEC.collect(PacketCodecs::optional), dataItemGroup -> Optional.ofNullable(dataItemGroup.icon),
            PacketCodecs.BOOL.collect(PacketCodecs::optional), dataItemGroup -> Optional.of(dataItemGroup.replace),
            DataItemGroup::new
    );

    public final Identifier id;
    public final String name;
    public final ItemStack icon;
    public final boolean replace;
    private final List<Entry> entries;

    public final List<ItemStack> items = new ArrayList<>();

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public DataItemGroup(Identifier id, Optional<String> name, Optional<ItemStack> icon, Optional<Boolean> replace, Optional<List<Entry>> entries) {
        this.id = id;
        this.name = name.orElse(null);
        this.icon = icon.orElse(null);
        this.replace = replace.orElse(false);
        this.entries = new ArrayList<>();
        entries.ifPresent(this.entries::addAll);

        makeDummyGroup(id);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public DataItemGroup(Identifier id, Optional<String> name, Optional<ItemStack> icon, Optional<Boolean> replace) {
        this(id, name, icon, replace, Optional.empty());
    }

    public void setupItems(MinecraftServer server) {
        for (Entry entry : entries) {
            entry.addItems(this.items, server);
        }
    }

    private DummyItemGroup dummyItemGroup;

    private void makeDummyGroup(Identifier id) {
        dummyItemGroup = new DummyItemGroup(id);
    }

    public DummyItemGroup getDummyItemGroup() {
        return this.dummyItemGroup;
    }

    public Identifier id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Optional<String> optionalName() {
        return Optional.ofNullable(name);
    }

    public ItemStack icon() {
        return icon;
    }

    public Optional<ItemStack> optionalIcon() {
        return Optional.ofNullable(icon);
    }

    public boolean replace() {
        return replace;
    }

    public List<ItemStack> items() {
        return items;
    }

    public List<Entry> entries() {
        return entries;
    }

    public void sync(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new SyncDataGroupCategoryPayload(this.id, this));

        int size = 16;
        for (int start = 0; start < items.size(); start += size) {
            int end = Math.min(start + size, items.size());
            List<ItemStack> sublist = items.subList(start, end);
            ServerPlayNetworking.send(player, new SyncDataGroupEntriesPayload(this.id, sublist));
        }
    }
}
