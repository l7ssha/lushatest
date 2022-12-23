package xyz.l7ssha.lushatest.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.container.data.TestBlockContainerData;
import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.registration.ContainerRegistry;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class TestBlockContainer extends AbstractContainerMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final ContainerData containerData;

    public TestBlockContainer(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(2), BlockPos.ZERO, new SimpleContainerData(1));
    }

    public TestBlockContainer(int id, @NotNull Inventory playerInv, IItemHandler slots, BlockPos pos, ContainerData containerData) {
        super(ContainerRegistry.TEST_BLOCK_CONTAINER.get(), id);
        this.containerLevelAccess = ContainerLevelAccess.create(playerInv.player.level, pos);
        this.containerData = containerData;

        final int slotSizePlus2 = 18, startX = 8, startY = 86, hotbarY = 144, inventoryY = 18;

        addSlot(new SlotItemHandler(slots, 0, 44, 36));
        addSlot(new SlotItemHandler(slots, 1, 80, 36));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + column, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, startX + column * slotSizePlus2, hotbarY));
        }

        addDataSlot(DataSlot.forContainer(containerData, 0));
    }

    public ContainerData getContainerData() {
        return this.containerData;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.containerLevelAccess, player, BlockRegistry.TEST_BLOCK.get());
    }

    public static MenuConstructor getServerContainer(TestTileEntity testTileEntity, BlockPos pos) {
        return (id, playerInv, player) -> new TestBlockContainer(id, playerInv, testTileEntity.getStackHandler(), pos, new TestBlockContainerData(testTileEntity));
    }
}
