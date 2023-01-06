package xyz.l7ssha.lushatest.network.packet.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.screen.TestBlockContainerScreen;

import java.util.function.Supplier;

public class LushaTileEntityInventorySideGuiSyncPacket {
    private final Direction direction;

    private final InventoryConfigMode mode;

    public LushaTileEntityInventorySideGuiSyncPacket(Direction direction, InventoryConfigMode mode) {
        this.direction = direction;
        this.mode = mode;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.direction.getSerializedName());
        buf.writeInt(this.mode.getIndex());
    }

    public static LushaTileEntityInventorySideGuiSyncPacket fromBytes(FriendlyByteBuf buf) {
        final var direction = Direction.byName(buf.readUtf());
        final var mode = InventoryConfigMode.fromIndex(buf.readInt());

        return new LushaTileEntityInventorySideGuiSyncPacket(direction, mode);
    }

    public static void handle(LushaTileEntityInventorySideGuiSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().screen instanceof TestBlockContainerScreen screen) {
                screen.updateContainerConfig(packet.direction, packet.mode);
            }

            context.setPacketHandled(true);
        }));
    }
}
