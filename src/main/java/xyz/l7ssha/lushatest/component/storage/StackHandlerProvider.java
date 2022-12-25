package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;

public class StackHandlerProvider<T extends LushaTestBlockEntity> {

    private StackHandlerConfiguration stackHandlerConfiguration;

    private final ItemStackHandler stackHandler;

    private final T blockEntity;

    public StackHandlerProvider(StackHandlerConfiguration stackHandlerConfiguration, T blockEntity) {
        this.stackHandlerConfiguration = stackHandlerConfiguration;
        this.blockEntity = blockEntity;

        this.stackHandler = createMainHandler();
    }

    public StackHandlerConfiguration getStackHandlerConfiguration() {
        return stackHandlerConfiguration;
    }

    public StackHandlerProvider<T> setStackHandlerConfiguration(StackHandlerConfiguration stackHandlerConfiguration) {
        this.stackHandlerConfiguration = stackHandlerConfiguration;
        return this;
    }

    public LazyOptional<IItemHandler> getHandlerForSide(Direction direction) {
        return LazyOptional.of(() -> new ItemStackHandler(this.getStackHandlerConfiguration().getSize()) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                final var slotConfiguration = StackHandlerProvider.this.stackHandlerConfiguration
                        .getSlotConfiguration()
                        .get(slot);

                if (slotConfiguration != null && !slotConfiguration.isAllowInsert()) {
                    return ItemStack.EMPTY;
                }

                return StackHandlerProvider.this.stackHandler.insertItem(slot, stack, simulate);
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                final var slotConfiguration = StackHandlerProvider.this.stackHandlerConfiguration
                        .getSlotConfiguration()
                        .get(slot);

                if (slotConfiguration != null && !slotConfiguration.isAllowExtract()) {
                    return ItemStack.EMPTY;
                }

                return StackHandlerProvider.this.stackHandler.extractItem(slot, amount, simulate);
            }
        }).cast();
    }

    public ItemStackHandler getMainHandler() {
        return this.stackHandler;
    }

    protected ItemStackHandler createMainHandler() {
        return new ItemStackHandler(this.getStackHandlerConfiguration().getSize()) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                var copiedStack = stack.copy();

                var returnStack = super.insertItem(slot, copiedStack, simulate);
                stack.shrink(stack.getCount() - returnStack.getCount());

                StackHandlerProvider.this.blockEntity.updateBlockEntity();
                return returnStack;
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                var returnStack = super.extractItem(slot, amount, simulate);
                StackHandlerProvider.this.blockEntity.updateBlockEntity();
                return returnStack;
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                final var slotConfiguration = StackHandlerProvider.this.stackHandlerConfiguration
                        .getSlotConfiguration()
                        .get(slot);

                if (slotConfiguration == null) {
                    return stack.getMaxStackSize();
                }

                return Math.min(slotConfiguration.getSlotLimit(), stack.getMaxStackSize());
            }
        };
    }
}
