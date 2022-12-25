package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

import java.util.Map;

public class StorageCapabilityComponent implements ICapabilityComponent<IItemHandler>, ICapabilityTileEntityComponent<TestTileEntity> {
    protected final static String INVENTORY_TAG = "inventory_capability_component_inventory";

    protected final ItemStackHandler stackHandler;

    protected final LazyOptional<ItemStackHandler> stackHandlerLazyOptional;

    protected final TestTileEntity tileEntity;

    public StorageCapabilityComponent(int size, TestTileEntity tileEntity) {
        this.stackHandler = createStackHandler(Map.of(0, 1));
        this.stackHandler.setSize(size);
        this.stackHandlerLazyOptional = LazyOptional.of(() -> stackHandler);

        this.tileEntity = tileEntity;
    }

    @Override
    public Capability<IItemHandler> getType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public LazyOptional<IItemHandler> getCapability(@Nullable Direction side) {
        return LazyOptional.of(() -> new ItemStackHandler(2) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return StorageCapabilityComponent.this.stackHandler.insertItem(slot, stack, simulate);
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot == 0) {
                    return ItemStack.EMPTY;
                }

                return StorageCapabilityComponent.this.stackHandler.extractItem(slot, amount, simulate);
            }
        }).cast();
    }

    @Override
    public ItemStackHandler getComponent() {
        return stackHandler;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        tag.put(INVENTORY_TAG, stackHandler.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        this.stackHandler.deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    @Override
    public TestTileEntity getBlockEntity() {
        return this.tileEntity;
    }

    protected ItemStackHandler createStackHandler(final Map<Integer, Integer> slotLimit) {
        return new ItemStackHandler() {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                var copiedStack = stack.copy();

                var returnStack = super.insertItem(slot, copiedStack, simulate);
                stack.shrink(stack.getCount() - returnStack.getCount());

                StorageCapabilityComponent.this.getBlockEntity().updateBlockEntity();
                return returnStack;
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                var returnStack = super.extractItem(slot, amount, simulate);
                StorageCapabilityComponent.this.getBlockEntity().updateBlockEntity();
                return returnStack;
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                final var slotLimitForSlot = slotLimit.getOrDefault(slot, stack.getMaxStackSize());

                return Math.min(slotLimitForSlot, stack.getMaxStackSize());
            }
        };
    }
}
