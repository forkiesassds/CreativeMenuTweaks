package me.icanttellyou.tweakedcreativemenu.networking.payload;

import me.icanttellyou.tweakedcreativemenu.networking.TweakedCreativeMenuPayloads;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ClearDataGroupManagerPayload implements CustomPayload {
    public static final CustomPayload.Id<ClearDataGroupManagerPayload> ID = new CustomPayload.Id<>(TweakedCreativeMenuPayloads.CLEAR_DATAGROUP_MANAGER_ID);
    public static final PacketCodec<RegistryByteBuf, ClearDataGroupManagerPayload> CODEC =
            PacketCodec.of((value, buf) -> {}, buf -> new ClearDataGroupManagerPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
