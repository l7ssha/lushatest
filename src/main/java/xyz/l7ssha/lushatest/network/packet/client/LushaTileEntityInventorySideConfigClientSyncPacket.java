package xyz.l7ssha.lushatest.network.packet.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.component.storage.StackHandlerConfiguration;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.function.Supplier;

public class LushaTileEntityInventorySideConfigClientSyncPacket {
    private final Direction direction;

    private final InventoryConfigMode mode;

    private final BlockPos blockPos;

    public LushaTileEntityInventorySideConfigClientSyncPacket(Direction direction, InventoryConfigMode mode, BlockPos blockPos) {
        this.direction = direction;
        this.mode = mode;
        this.blockPos = blockPos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
        buf.writeBlockPos(this.blockPos);
    }

    public static LushaTileEntityInventorySideConfigClientSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = InventoryConfigMode.fromIndex(buf.readInt());
        final var blockPos = buf.readBlockPos();

        return new LushaTileEntityInventorySideConfigClientSyncPacket(direction, mode, blockPos);
    }

    public static void handle(LushaTileEntityInventorySideConfigClientSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().level.getBlockEntity(packet.blockPos) instanceof TestTileEntity entity) {
                final var configuration = entity.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow().getStackHandlerProvider().getStackHandlerConfiguration();

                configuration.getSideConfiguration().put(packet.direction, new StackHandlerConfiguration.SideConfiguration(packet.mode));
            }

            context.setPacketHandled(true);
        }));
    }
}
