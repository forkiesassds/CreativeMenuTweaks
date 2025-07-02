package com.goopswagger.creativemenutweaks.data;

import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupCategoryPayload;
import com.goopswagger.creativemenutweaks.networking.payload.SyncDataGroupEntriesPayload;
import com.goopswagger.creativemenutweaks.util.DummyItemGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataItemGroup {
    public static final Codec<DataItemGroup> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            Identifier.CODEC.fieldOf("id").forGetter(DataItemGroup::id),
                            Codec.STRING.optionalFieldOf("name").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.name())),
                            ItemStack.CODEC.optionalFieldOf("icon").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.icon)),
                            Codec.BOOL.optionalFieldOf("replace").forGetter(dataItemGroup -> Optional.of(dataItemGroup.replace)),
                            ItemStack.CODEC.listOf().optionalFieldOf("entries").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.entries)),
                            RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).listOf().optionalFieldOf("loot_tables").forGetter(dataItemGroup -> Optional.ofNullable(dataItemGroup.lootTables)))
                    .apply(instance, DataItemGroup::new));

    @SuppressWarnings("unchecked")
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
    public final List<ItemStack> entries;
    public final List<RegistryKey<LootTable>> lootTables;

    public DataItemGroup(Identifier id, Optional<String> name, Optional<ItemStack> icon, Optional<Boolean> replace, Optional<List<ItemStack>> entries, Optional<List<RegistryKey<LootTable>>> lootTables) {
        this.id = id;
        this.name = name.orElse(null);
        this.icon = icon.orElse(null);
        this.replace = replace.orElse(false);
        this.entries = new ArrayList<>();
        entries.ifPresent(this.entries::addAll);
        this.lootTables = lootTables.orElse(new ArrayList<>());

        makeDummyGroup(id);
    }

    public DataItemGroup(Identifier id, Optional<String> name, Optional<ItemStack> icon, Optional<Boolean> replace) {
        this(id, name, icon, replace, Optional.empty(), Optional.empty());
    }

    public void parseLootTable(MinecraftServer server) {
        List<ItemStack> lootTables = new ArrayList<>();

        LootContextParameterSet context = new LootContextParameterSet.Builder(server.getWorld(World.OVERWORLD)).build(LootContextTypes.EMPTY);
        ReloadableRegistries.Lookup registries = server.getReloadableRegistries();

        for (RegistryKey<LootTable> lootTableId : this.lootTables) {
            LootTable lootTable = registries.getLootTable(lootTableId);
            assert lootTable != null;

            lootTables.addAll(lootTable.generateLoot(context));
        }
        this.entries.addAll(lootTables);
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

    public List<ItemStack> entries() {
        return entries;
    }

    public List<RegistryKey<LootTable>> lootTables() {
        return lootTables;
    }

    public void sync(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new SyncDataGroupCategoryPayload(this.id, this.strip()));

        int size = 16;
        for (int start = 0; start < entries.size(); start += size) {
            int end = Math.min(start + size, entries.size());
            List<ItemStack> sublist = entries.subList(start, end);
            ServerPlayNetworking.send(player, new SyncDataGroupEntriesPayload(this.id, sublist));
        }
    }

    private DataItemGroup strip() {
        return new DataItemGroup(this.id, optionalName(), optionalIcon(), Optional.of(replace()), Optional.empty(), Optional.empty());
    }
}
