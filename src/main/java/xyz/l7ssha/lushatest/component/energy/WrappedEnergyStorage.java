package xyz.l7ssha.lushatest.component.energy;

import net.minecraftforge.energy.EnergyStorage;

public class WrappedEnergyStorage extends EnergyStorage {
    public WrappedEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }
}
