package xyz.l7ssha.lushatest.component.energy;

import net.minecraft.core.Direction;
import net.minecraftforge.energy.EnergyStorage;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.SideAccessConfiguration;

public class WrappedEnergyStorage extends EnergyStorage {
    private final SideAccessConfiguration sideAccessConfiguration;
    private final Direction direction;

    public WrappedEnergyStorage(int capacity, int maxTransfer, Direction direction, SideAccessConfiguration sideAccessConfiguration) {
        super(capacity, maxTransfer);

        this.sideAccessConfiguration = sideAccessConfiguration;
        this.direction = direction;
    }

    public boolean isNoneMode() {
        return sideAccessConfiguration.getSideConfiguration(this.direction).getMode() == AccessModeConfig.NONE;
    }

    @Override
    public boolean canExtract() {
        final var configuration = this.sideAccessConfiguration.getSideConfiguration(this.direction);
        return configuration.getMode().isAllowExtract();
    }

    @Override
    public boolean canReceive() {
        final var configuration = this.sideAccessConfiguration.getSideConfiguration(this.direction);
        return configuration.getMode().isAllowInsert();
    }
}
