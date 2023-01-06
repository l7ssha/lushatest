package xyz.l7ssha.lushatest.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.network.packet.client.LushaTileEntityInventorySideGuiSyncPacket;
import xyz.l7ssha.lushatest.network.packet.server.LushaTileEntityInventorySideUpdateConfigPacket;

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

        CHANNEL_INSTANCE.messageBuilder(LushaTileEntityInventorySideUpdateConfigPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LushaTileEntityInventorySideUpdateConfigPacket::fromBytes)
                .encoder(LushaTileEntityInventorySideUpdateConfigPacket::toBytes)
                .consumer(LushaTileEntityInventorySideUpdateConfigPacket::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(LushaTileEntityInventorySideGuiSyncPacket.class, getNewPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LushaTileEntityInventorySideGuiSyncPacket::fromBytes)
                .encoder(LushaTileEntityInventorySideGuiSyncPacket::toBytes)
                .consumer(LushaTileEntityInventorySideGuiSyncPacket::handle)
                .add();
    }

    public static <T> void sendToServer(T message) {
        CHANNEL_INSTANCE.sendToServer(message);
    }

    public static <T> void sendToPlayer(ServerPlayer player, T message) {
        CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
