package xyz.l7ssha.lushatest.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.core.LushaInventoryBlockEntity;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;

public class TestTileEntity extends LushaInventoryBlockEntity implements BlockEntityTicker<TestTileEntity> {

    private final IEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public final static int ENERGY_STORAGE_MAX = 20_000_000;

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
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energyStorageLazyOptional.cast();
        }

        return super.getCapability(cap);
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

                final var extracted = storage.extractEnergy(100_000, false);
                TestTileEntity.this.energyStorage.receiveEnergy(extracted, false);
            });
        }

        if (this.getEnergyStorage().getEnergyStored() > ENERGY_STORAGE_MAX / 2 && this.getStackHandler().getStackInSlot(0).getCount() > 0) {
            this.getEnergyStorage().extractEnergy(ENERGY_STORAGE_MAX / 2, false);

            var stackToInsert = this.getStackHandler().getStackInSlot(0).copy();
            stackToInsert.setCount(1);

            this.getStackHandler().insertItem(1, stackToInsert, false);
        }
    }
}
