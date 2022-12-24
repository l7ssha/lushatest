package xyz.l7ssha.lushatest.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.core.LushaInventoryBlockEntity;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;

import java.util.Map;

public class TestTileEntity extends LushaInventoryBlockEntity implements BlockEntityTicker<TestTileEntity> {

    private final IEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public final static int ENERGY_STORAGE_MAX = 2_147_483_647; // Integer.MAX_VALUE;

    public final static Map<Item, Integer> processingMap = Map.of(
            Items.DIAMOND, Integer.MAX_VALUE / 128
    );

    public TestTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TEST_BLOCK_ENTITY.get(), pos, state, 2);

        this.energyStorage = new EnergyStorage(ENERGY_STORAGE_MAX);
        this.energyStorageLazyOptional = LazyOptional.of(() -> this.energyStorage);
    }

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energyStorageLazyOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void tick(Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull TestTileEntity tile) {
        if (world.isClientSide()) {
            return;
        }

        for(final var direction: Direction.values()) {
            final var blockEntity = world.getBlockEntity(this.worldPosition.relative(direction));
            if (blockEntity == null) {
                continue;
            }

            blockEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                if(!storage.canExtract()) {
                    return;
                }

                final var extracted = storage.extractEnergy(Integer.MAX_VALUE / 256, false);
                TestTileEntity.this.energyStorage.receiveEnergy(extracted, false);
            });
        }

        final var inputSlotStack = stackHandler.getStackInSlot(0);
        final var outputSlotStack = stackHandler.getStackInSlot(1);

        final var processingCost = processingMap.get(inputSlotStack.getItem());
        if (processingCost == null) {
            return;
        }

        if (this.canProcess(inputSlotStack, outputSlotStack, processingCost)) {
            this.getEnergyStorage().extractEnergy(processingCost, false);

            var stackToInsert = this.getStackHandler().getStackInSlot(0).copy();
            stackToInsert.setCount(1);

            this.getStackHandler().insertItem(1, stackToInsert, false);
        }
    }

    protected boolean canProcess(ItemStack inputSlotStack, ItemStack outputSlotStack, int processingCost) {
        final var energyStorage = this.getEnergyStorage();

        return energyStorage.getEnergyStored() > processingCost
                && (inputSlotStack.getItem().equals(outputSlotStack.getItem()) || outputSlotStack.getItem().equals(Items.AIR))
                && inputSlotStack.getCount() > 0
                && outputSlotStack.getCount() < 64;
    }
}
