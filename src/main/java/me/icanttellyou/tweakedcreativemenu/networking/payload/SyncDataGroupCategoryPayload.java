package me.icanttellyou.tweakedcreativemenu.networking.payload;

import me.icanttellyou.tweakedcreativemenu.data.DataItemGroup;
import me.icanttellyou.tweakedcreativemenu.networking.TweakedCreativeMenuPayloads;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncDataGroupCategoryPayload(Identifier id, DataItemGroup group) implements CustomPayload {
    public static final CustomPayload.Id<SyncDataGroupCategoryPayload> ID = new CustomPayload.Id<>(TweakedCreativeMenuPayloads.SYNC_DATAGROUP_CATEGORY_ID);
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
