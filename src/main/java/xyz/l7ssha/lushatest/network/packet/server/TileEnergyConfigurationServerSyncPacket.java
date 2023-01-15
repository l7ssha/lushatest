package xyz.l7ssha.lushatest.network.packet.server;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.DirectionAccessConfiguration;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.client.TileEnergyConfigurationClientSyncPacket;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class TileEnergyConfigurationServerSyncPacket {
    private final Direction direction;
    private final AccessModeConfig mode;

    private final static Logger logger = LogUtils.getLogger();

    public TileEnergyConfigurationServerSyncPacket(Direction direction, AccessModeConfig mode) {
        this.direction = direction;
        this.mode = mode;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
    }

    public static TileEnergyConfigurationServerSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = AccessModeConfig.fromIndex(buf.readInt());

        return new TileEnergyConfigurationServerSyncPacket(direction, mode);
    }

    public static void handle(TileEnergyConfigurationServerSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            final var blockPos = ((TestBlockContainerMenu) context.getSender().containerMenu).getBlockEntity().getBlockPos();
            final var tileEntity = (TestTileEntity) context.getSender().getLevel().getBlockEntity(blockPos);

            if (tileEntity == null) {
                throw new RuntimeException("Missing block entity");
            }

            final var storageComponent = tileEntity.<EnergyCapabilityComponent<?>>getComponent(ForgeCapabilities.ENERGY)
                    .orElseThrow(() -> new RuntimeException("Missing item handler component"));

            storageComponent.getSideAccessConfiguration().getSideConfiguration().put(packet.direction, new DirectionAccessConfiguration(packet.mode));

            logger.debug("Handled TileEnergyConfigurationServerSyncPacket; (direction: %s, mode: %s)".formatted(packet.direction.getName(), packet.mode.getLabel()));
            LushaNetworkChannel.sendToAllNear(tileEntity, new TileEnergyConfigurationClientSyncPacket(packet.direction, packet.mode, blockPos));

            context.setPacketHandled(true);
        });
    }
}
