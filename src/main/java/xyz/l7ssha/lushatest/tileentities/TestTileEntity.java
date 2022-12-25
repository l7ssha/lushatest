package xyz.l7ssha.lushatest.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.core.LushaComponentTickerBlockEntity;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;
import xyz.l7ssha.lushatest.utils.StorageComponentStackHandlerBuilder;

import java.util.Map;

public class TestTileEntity extends LushaComponentTickerBlockEntity<TestTileEntity> {

    public final static int ENERGY_STORAGE_MAX = 2_147_483_647; // Integer.MAX_VALUE;

    public final static Map<Item, Integer> processingMap = Map.of(
            Items.DIAMOND, Integer.MAX_VALUE / 128
    );

    public TestTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TEST_BLOCK_ENTITY.get(), pos, state);

        this.addComponent(new EnergyCapabilityComponent<TestTileEntity>(ENERGY_STORAGE_MAX));
        this.addComponent(
                new StorageCapabilityComponent(
                        new StorageComponentStackHandlerBuilder()
                                .setSize(2)
                                .addSlot(0, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(1).setAllowExtract(false).setAllowInsert(false))
                                .addSlot(1, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(64).setAllowExtract(true).setAllowInsert(false))
                                .build(),
                        this
                )
        );
    }

    public IEnergyStorage getEnergyStorage() {
        return this.getComponent(CapabilityEnergy.ENERGY).getComponent();
    }

    public IItemHandler getStackHandler() {
        return this.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).getComponent();
    }

    @Override
    public void tick(@NotNull Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull TestTileEntity tile) {
        super.tick(world, blockPos, blockState, tile);

        if (world.isClientSide()) {
            return;
        }

        processMachine();
    }

    protected void processMachine() {
        final var inputSlotStack = this.getStackHandler().getStackInSlot(0);
        final var outputSlotStack = this.getStackHandler().getStackInSlot(1);

        final var processingCost = processingMap.get(inputSlotStack.getItem());
        if (processingCost == null) {
            return;
        }

        if (!this.canProcess(inputSlotStack, outputSlotStack, processingCost)) {
            return;
        }

        this.getEnergyStorage().extractEnergy(processingCost, false);

        var stackToInsert = this.getStackHandler().getStackInSlot(0).copy();
        stackToInsert.setCount(1);

        this.getStackHandler().insertItem(1, stackToInsert, false);
    }

    protected boolean canProcess(ItemStack inputSlotStack, ItemStack outputSlotStack, int processingCost) {
        final var energyStorage = this.getEnergyStorage();

        return energyStorage.getEnergyStored() > processingCost
                && (inputSlotStack.getItem().equals(outputSlotStack.getItem()) || outputSlotStack.getItem().equals(Items.AIR))
                && inputSlotStack.getCount() > 0
                && outputSlotStack.getCount() < 64;
    }

    public int getCurrentProgressPercentage() {
        var processingCost = processingMap.get(this.getStackHandler().getStackInSlot(0).getItem());
        if (processingCost == null) {
            return 0;
        }

        final var calculatedPercentage = (int) ((double) this.getEnergyStorage().getEnergyStored() / (double) processingCost * 100);
        return Math.min(100, calculatedPercentage);
    }
}
