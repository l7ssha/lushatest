package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.ICapabilityComponent;
import xyz.l7ssha.lushatest.component.ICapabilityTileEntityComponent;
import xyz.l7ssha.lushatest.component.configuration.side.SideAccessConfiguration;
import xyz.l7ssha.lushatest.component.configuration.slot.SlotsConfiguration;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class StorageCapabilityComponent implements ICapabilityComponent<IItemHandler>, ICapabilityTileEntityComponent<TestTileEntity> {
    protected final LazyOptional<ItemStackHandler> stackHandlerLazyOptional;

    protected final StackHandlerProvider<TestTileEntity> stackHandlerProvider;

    protected final TestTileEntity tileEntity;

    public StorageCapabilityComponent(SlotsConfiguration slotsConfiguration, SideAccessConfiguration sideAccessConfiguration, TestTileEntity tileEntity) {
        this.tileEntity = tileEntity;

        this.stackHandlerProvider = new StackHandlerProvider<>(slotsConfiguration, sideAccessConfiguration, tileEntity);
        this.stackHandlerLazyOptional = LazyOptional.of(stackHandlerProvider::getMainHandler);
    }

    public SlotsConfiguration getSlotsConfiguration() {
        return this.getStackHandlerProvider().getSlotsConfiguration();
    }

    public SideAccessConfiguration getSideConfiguration() {
        return this.getStackHandlerProvider().getSideAccessConfiguration();
    }

    public SimpleContainer getAsSimpleContainer() {
        final var container = new SimpleContainer(this.getComponent().getSlots());

        for (var i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, this.getComponent().getStackInSlot(i));
        }

        return container;
    }

    public StackHandlerProvider<TestTileEntity> getStackHandlerProvider() {
        return stackHandlerProvider;
    }

    @Override
    public Capability<IItemHandler> getType() {
        return ForgeCapabilities.ITEM_HANDLER;
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
        this.getStackHandlerProvider().saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        this.getStackHandlerProvider().load(tag, this.tileEntity);
    }

    @Override
    public TestTileEntity getBlockEntity() {
        return this.tileEntity;
    }
}
