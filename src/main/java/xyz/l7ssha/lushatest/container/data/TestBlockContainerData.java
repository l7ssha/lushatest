package xyz.l7ssha.lushatest.container.data;

import net.minecraft.world.inventory.SimpleContainerData;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class TestBlockContainerData extends SimpleContainerData {
    private final TestTileEntity tileEntity;

    public TestBlockContainerData(TestTileEntity tileEntity) {
        super(2);
        this.tileEntity = tileEntity;
    }

    @Override
    public int get(int key) {
        if (key == 0) {
            return this.tileEntity.getEnergyStorage().getEnergyStored();
        }

        if (key == 1) {
            return this.tileEntity.getCurrentProgressPercentage();
        }

        throw new IllegalStateException("Key: " + key + " is invalid for TestBlockContainerData");
    }
}
