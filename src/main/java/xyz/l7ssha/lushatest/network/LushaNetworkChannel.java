package xyz.l7ssha.lushatest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.network.packet.client.TileEnergyConfigurationClientSyncPacket;
import xyz.l7ssha.lushatest.network.packet.client.TileInventoryConfigurationClientSyncPacket;
import xyz.l7ssha.lushatest.network.packet.server.TileEnergyConfigurationServerSyncPacket;
import xyz.l7ssha.lushatest.network.packet.server.TileInventoryConfigurationServerSyncPacket;

public class LushaNetworkChannel {
    private static SimpleChannel CHANNEL_INSTANCE;

    private static int lastPacketId = 0;

    private static int getNewPacketId() {
        return lastPacketId++;
    }

    public static void register() {
        CHANNEL_INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(LushaTestMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL_INSTANCE.messageBuilder(TileInventoryConfigurationServerSyncPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TileInventoryConfigurationServerSyncPacket::fromBytes)
                .encoder(TileInventoryConfigurationServerSyncPacket::toBytes)
                .consumerMainThread(TileInventoryConfigurationServerSyncPacket::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(TileInventoryConfigurationClientSyncPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TileInventoryConfigurationClientSyncPacket::fromBytes)
                .encoder(TileInventoryConfigurationClientSyncPacket::toBytes)
                .consumerMainThread(TileInventoryConfigurationClientSyncPacket::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(TileEnergyConfigurationServerSyncPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TileEnergyConfigurationServerSyncPacket::fromBytes)
                .encoder(TileEnergyConfigurationServerSyncPacket::toBytes)
                .consumerMainThread(TileEnergyConfigurationServerSyncPacket::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(TileEnergyConfigurationClientSyncPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TileEnergyConfigurationClientSyncPacket::fromBytes)
                .encoder(TileEnergyConfigurationClientSyncPacket::toBytes)
                .consumerMainThread(TileEnergyConfigurationClientSyncPacket::handle)
                .add();
    }

    public static <T> void sendToServer(T message) {
        CHANNEL_INSTANCE.sendToServer(message);
    }

    public static <T> void sendToPlayer(ServerPlayer player, T message) {
        CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <T> void sendToAllNear(BlockPos blockPos, ResourceKey<Level> dimension, int radius, T message) {
        CHANNEL_INSTANCE.send(
                PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(
                        blockPos.getX(),
                        blockPos.getY(),
                        blockPos.getZ(),
                        radius,
                        dimension
                )),
                message
        );
    }

    public static <T> void sendToAllNearInChunkRadius(BlockEntity entity, T message) {
        sendToAllNear(entity.getBlockPos(), entity.getLevel().dimension(), 16, message);
    }
}
