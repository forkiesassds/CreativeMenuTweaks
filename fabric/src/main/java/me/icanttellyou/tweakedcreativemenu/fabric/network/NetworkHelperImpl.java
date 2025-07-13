package me.icanttellyou.tweakedcreativemenu.fabric.network;

import me.icanttellyou.tweakedcreativemenu.networking.INetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

public class NetworkHelperImpl implements INetworkHelper {
    @Override
    public void sendToServer(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, CustomPayload payload) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            this.sendToPlayer(player, payload);
        }
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, CustomPayload payload) {
        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            this.sendToPlayer(player, payload);
        }
    }
}
