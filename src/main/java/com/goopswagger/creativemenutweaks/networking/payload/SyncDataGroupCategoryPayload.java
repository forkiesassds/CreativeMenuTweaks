package com.goopswagger.creativemenutweaks.networking.payload;

import com.goopswagger.creativemenutweaks.data.DataItemGroup;
import com.goopswagger.creativemenutweaks.networking.CreativeMenuTweaksPackets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncDataGroupCategoryPayload(Identifier id, DataItemGroup group) implements CustomPayload {
    public static final CustomPayload.Id<SyncDataGroupCategoryPayload> ID = new CustomPayload.Id<>(CreativeMenuTweaksPackets.SYNC_DATAGROUP_CATEGORY_ID);
    public static final PacketCodec<RegistryByteBuf, SyncDataGroupCategoryPayload> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, SyncDataGroupCategoryPayload::id,
            DataItemGroup.PACKET_CODEC, SyncDataGroupCategoryPayload::group,
            SyncDataGroupCategoryPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
