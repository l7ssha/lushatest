package xyz.l7ssha.lushatest.component.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.ICapabilityComponent;
import xyz.l7ssha.lushatest.component.ICapabilityTicker;

public class EnergyCapabilityComponent<T extends BlockEntity> implements ICapabilityComponent<IEnergyStorage>, ICapabilityTicker<T> {
    protected final static String ENERGY_TAG = "energy_capability_energy_stored";

    protected final static int EXTRACT_MAX = Integer.MAX_VALUE / 256;

    private final IEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public EnergyCapabilityComponent(int energyStoreMax) {
        this.energyStorage = new EnergyStorage(energyStoreMax);
        this.energyStorageLazyOptional = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public Capability<IEnergyStorage> getType() {
        return ForgeCapabilities.ENERGY;
    }

    @Override
    public LazyOptional<IEnergyStorage> getCapability(@Nullable Direction side) {
        return energyStorageLazyOptional;
    }

    @Override
    public IEnergyStorage getComponent() {
        return energyStorage;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt(ENERGY_TAG, energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag tag) {
        this.energyStorage.receiveEnergy(tag.getInt(ENERGY_TAG), false);
    }

    @Override
    public void tick(Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull T tile) {
        for(final var direction: Direction.values()) {
            final var blockEntity = world.getBlockEntity(blockPos.relative(direction));
            if (blockEntity == null) {
                continue;
            }

            blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                if (!storage.canExtract()) {
                    return;
                }

                final var extracted = storage.extractEnergy(EXTRACT_MAX, false);
                EnergyCapabilityComponent.this.energyStorage.receiveEnergy(extracted, false);
            });
        }
    }
}
