package xyz.l7ssha.lushatest.network.packet.server;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.DirectionAccessConfiguration;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.client.TileInventoryConfigurationClientSyncPacket;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class TileInventoryConfigurationServerSyncPacket {
    private final Direction direction;
    private final AccessModeConfig mode;

    private final static Logger logger = LogUtils.getLogger();

    public TileInventoryConfigurationServerSyncPacket(Direction direction, AccessModeConfig mode) {
        this.direction = direction;
        this.mode = mode;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
    }

    public static TileInventoryConfigurationServerSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = AccessModeConfig.fromIndex(buf.readInt());

        return new TileInventoryConfigurationServerSyncPacket(direction, mode);
    }

    public static void handle(TileInventoryConfigurationServerSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            final var blockPos = ((TestBlockContainerMenu) context.getSender().containerMenu).getBlockEntity().getBlockPos();
            final var tileEntity = (TestTileEntity) context.getSender().getLevel().getBlockEntity(blockPos);

            if (tileEntity == null) {
                throw new RuntimeException("Missing block entity");
            }

            final var storageComponent = tileEntity.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER)
                    .orElseThrow(() -> new RuntimeException("Missing item handler component"));

            storageComponent.getSideConfiguration().getSideConfiguration().put(packet.direction, new DirectionAccessConfiguration(packet.mode));

            logger.debug("Handled LushaTileEntityInventorySideUpdateConfigPacket; (direction: %s, mode: %s)".formatted(packet.direction.getName(), packet.mode.getLabel()));
            LushaNetworkChannel.sendToAllNear(tileEntity, new TileInventoryConfigurationClientSyncPacket(packet.direction, packet.mode, blockPos));

            context.setPacketHandled(true);
        });
    }
}
