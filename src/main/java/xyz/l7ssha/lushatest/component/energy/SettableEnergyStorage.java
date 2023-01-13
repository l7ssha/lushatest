package xyz.l7ssha.lushatest.component.energy;

import net.minecraftforge.energy.EnergyStorage;

public class SettableEnergyStorage extends EnergyStorage {
    public SettableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
