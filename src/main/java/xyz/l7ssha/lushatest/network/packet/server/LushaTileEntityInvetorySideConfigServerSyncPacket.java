package xyz.l7ssha.lushatest.network.packet.server;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.AccessModeConfig;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageComponentStackHandlerBuilder;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.client.LushaTileEntityInventorySideConfigClientSyncPacket;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class LushaTileEntityInvetorySideConfigServerSyncPacket {
    private final Direction direction;
    private final AccessModeConfig mode;

    private final static Logger logger = LogUtils.getLogger();

    public LushaTileEntityInvetorySideConfigServerSyncPacket(Direction direction, AccessModeConfig mode) {
        this.direction = direction;
        this.mode = mode;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
    }

    public static LushaTileEntityInvetorySideConfigServerSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = AccessModeConfig.fromIndex(buf.readInt());

        return new LushaTileEntityInvetorySideConfigServerSyncPacket(direction, mode);
    }

    public static void handle(LushaTileEntityInvetorySideConfigServerSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            final var blockPos = ((TestBlockContainerMenu) context.getSender().containerMenu).getBlockEntity().getBlockPos();
            final var tileEntity = (TestTileEntity) context.getSender().getLevel().getBlockEntity(blockPos);

            if (tileEntity == null) {
                throw new RuntimeException("Missing block entity");
            }

            final var storageComponent = tileEntity.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER)
                    .orElseThrow(() -> new RuntimeException("Missing item handler component"));

            storageComponent.getStackHandlerProvider().setStackHandlerConfiguration(
                    StorageComponentStackHandlerBuilder.fromConfig(storageComponent.getStackHandlerProvider().getStackHandlerConfiguration())
                            .addSideConfig(packet.direction, new StorageComponentStackHandlerBuilder.SideConfigBuilder(packet.mode))
                            .build()
            );

            logger.debug("Handled LushaTileEntityInventorySideUpdateConfigPacket; (direction: %s, mode: %s)".formatted(packet.direction.getName(), packet.mode.getLabel()));
            LushaNetworkChannel.sendToAllNear(tileEntity, new LushaTileEntityInventorySideConfigClientSyncPacket(packet.direction, packet.mode, blockPos));

            context.setPacketHandled(true);
        });
    }
}
