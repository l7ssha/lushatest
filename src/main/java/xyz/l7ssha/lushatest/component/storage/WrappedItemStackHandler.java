package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class WrappedItemStackHandler extends ItemStackHandler {
    private final StackHandlerConfiguration configuration;
    private final ItemStackHandler stackHandler;

    public WrappedItemStackHandler(StackHandlerConfiguration configuration, ItemStackHandler stackHandler) {
        super(configuration.getSize());

        this.configuration = configuration;
        this.stackHandler = stackHandler;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        final var slotConfiguration = this.configuration
                .getSlotConfiguration()
                .get(slot);

        if (slotConfiguration != null && !slotConfiguration.isAllowInsert()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        final var slotConfiguration = this.configuration
                .getSlotConfiguration()
                .get(slot);

        if (slotConfiguration != null && !slotConfiguration.isAllowExtract()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.extractItem(slot, amount, simulate);
    }
}
