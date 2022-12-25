package xyz.l7ssha.lushatest.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.container.data.TestBlockContainerData;
import xyz.l7ssha.lushatest.container.slot.ItemRestrictedSlot;
import xyz.l7ssha.lushatest.container.slot.ReadonlySlot;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.registration.ContainerRegistry;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class TestBlockContainer extends LushaTestContainerMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final ContainerData containerData;

    public TestBlockContainer(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(2), BlockPos.ZERO, new SimpleContainerData(2));
    }

    public TestBlockContainer(int id, @NotNull Inventory playerInv, IItemHandler slots, BlockPos pos, ContainerData containerData) {
        super(ContainerRegistry.TEST_BLOCK_CONTAINER.get(), id);
        this.containerLevelAccess = ContainerLevelAccess.create(playerInv.player.level, pos);
        this.containerData = containerData;

        addSlot(new ItemRestrictedSlot(slots, 0, 26, 36, TestTileEntity.processingMap.keySet().stream().toList()));
        addSlot(new ReadonlySlot(slots, 1, 98, 36));

        addPlayerInv(playerInv);

        addDataSlot(DataSlot.forContainer(containerData, 0));
        addDataSlot(DataSlot.forContainer(containerData, 1));
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
