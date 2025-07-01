package com.goopswagger.creativemenutweaks.networking.payload;

import com.goopswagger.creativemenutweaks.networking.CreativeMenuTweaksPackets;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record SyncDataGroupEntriesPayload(Identifier id, List<ItemStack> stackList) implements CustomPayload {
    public static final CustomPayload.Id<SyncDataGroupEntriesPayload> ID = new CustomPayload.Id<>(CreativeMenuTweaksPackets.SYNC_DATAGROUP_ENTRIES_ID);
    public static final PacketCodec<RegistryByteBuf, SyncDataGroupEntriesPayload> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, SyncDataGroupEntriesPayload::id,
            ItemStack.LIST_PACKET_CODEC, SyncDataGroupEntriesPayload::stackList,
            SyncDataGroupEntriesPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
