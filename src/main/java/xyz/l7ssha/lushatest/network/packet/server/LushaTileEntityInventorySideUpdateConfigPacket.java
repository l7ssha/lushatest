package xyz.l7ssha.lushatest.network.packet.server;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageComponentStackHandlerBuilder;
import xyz.l7ssha.lushatest.container.TestBlockContainer;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.client.LushaTileEntityInventorySideGuiSyncPacket;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class LushaTileEntityInventorySideUpdateConfigPacket {
    private final Direction direction;
    private final InventoryConfigMode mode;

    private final static Logger logger = LogUtils.getLogger();

    public LushaTileEntityInventorySideUpdateConfigPacket(Direction direction, InventoryConfigMode mode) {
        this.direction = direction;
        this.mode = mode;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
    }

    public static LushaTileEntityInventorySideUpdateConfigPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = InventoryConfigMode.fromIndex(buf.readInt());

        return new LushaTileEntityInventorySideUpdateConfigPacket(direction, mode);
    }

    public static void handle(LushaTileEntityInventorySideUpdateConfigPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            final var blockPos = ((TestBlockContainer) context.getSender().containerMenu).getBlockPos();
            final var tileEntity = (TestTileEntity) context.getSender().getLevel().getBlockEntity(blockPos);

            if (tileEntity == null) {
                throw new RuntimeException("Missing block entity");
            }

            final var storageComponent = (StorageCapabilityComponent) tileEntity.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .orElseThrow(() -> new RuntimeException("Missing item handler component"));

            storageComponent.getStackHandlerProvider().setStackHandlerConfiguration(
                    StorageComponentStackHandlerBuilder.fromConfig(storageComponent.getStackHandlerProvider().getStackHandlerConfiguration())
                            .addSideConfig(packet.direction, new StorageComponentStackHandlerBuilder.SideConfigBuilder(packet.mode))
                            .build()
            );

            logger.debug("Handled LushaTileEntityInventorySideUpdateConfigPacket; (direction: %s, mode: %s)".formatted(packet.direction.getName(), packet.mode.getLabel()));
            LushaNetworkChannel.sendToPlayer(context.getSender(), new LushaTileEntityInventorySideGuiSyncPacket(packet.direction, packet.mode));

            context.setPacketHandled(true);
        });
    }
}
