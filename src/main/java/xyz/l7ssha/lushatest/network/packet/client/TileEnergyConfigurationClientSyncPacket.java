package xyz.l7ssha.lushatest.network.packet.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.DirectionAccessConfiguration;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class TileEnergyConfigurationClientSyncPacket {
    private final Direction direction;

    private final AccessModeConfig mode;

    private final BlockPos blockPos;

    public TileEnergyConfigurationClientSyncPacket(Direction direction, AccessModeConfig mode, BlockPos blockPos) {
        this.direction = direction;
        this.mode = mode;
        this.blockPos = blockPos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
        buf.writeBlockPos(this.blockPos);
    }

    public static TileEnergyConfigurationClientSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = AccessModeConfig.fromIndex(buf.readInt());
        final var blockPos = buf.readBlockPos();

        return new TileEnergyConfigurationClientSyncPacket(direction, mode, blockPos);
    }

    public static void handle(TileEnergyConfigurationClientSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().level.getBlockEntity(packet.blockPos) instanceof TestTileEntity entity) {
                final var configuration = entity.<EnergyCapabilityComponent<?>>getComponent(ForgeCapabilities.ENERGY).orElseThrow();

                configuration.getSideAccessConfiguration().getSideConfiguration().put(packet.direction, new DirectionAccessConfiguration(packet.mode));
            }

            context.setPacketHandled(true);
        }));
    }
}
