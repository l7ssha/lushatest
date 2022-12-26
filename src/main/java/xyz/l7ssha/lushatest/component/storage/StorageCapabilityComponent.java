package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.ICapabilityComponent;
import xyz.l7ssha.lushatest.component.ICapabilityTileEntityComponent;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class StorageCapabilityComponent implements ICapabilityComponent<IItemHandler>, ICapabilityTileEntityComponent<TestTileEntity> {
    protected final static String INVENTORY_TAG = "inventory_capability_component_inventory";

    protected final LazyOptional<ItemStackHandler> stackHandlerLazyOptional;

    protected final StackHandlerProvider<TestTileEntity> stackHandlerProvider;

    protected final TestTileEntity tileEntity;

    public StorageCapabilityComponent(StackHandlerConfiguration configuration, TestTileEntity tileEntity) {
        this.tileEntity = tileEntity;

        this.stackHandlerProvider = new StackHandlerProvider<>(configuration, tileEntity);
        this.stackHandlerLazyOptional = LazyOptional.of(stackHandlerProvider::getMainHandler);
    }

    public StackHandlerProvider<TestTileEntity> getStackHandlerProvider() {
        return stackHandlerProvider;
    }

    @Override
    public Capability<IItemHandler> getType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public LazyOptional<IItemHandler> getCapability(@Nullable Direction side) {
        return this.stackHandlerProvider.getHandlerForSide(side);
    }

    @Override
    public ItemStackHandler getComponent() {
        return stackHandlerProvider.getMainHandler();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        tag.put(INVENTORY_TAG, stackHandlerProvider.getMainHandler().serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        this.stackHandlerProvider.getMainHandler().deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    @Override
    public TestTileEntity getBlockEntity() {
        return this.tileEntity;
    }
}
