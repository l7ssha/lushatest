package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.AccessModeConfig;

public class WrappedItemStackHandler extends ItemStackHandler {
    private final StackHandlerConfiguration configuration;
    private final ItemStackHandler stackHandler;
    private final Direction direction;

    public WrappedItemStackHandler(Direction direction, StackHandlerConfiguration configuration, ItemStackHandler stackHandler) {
        super(configuration.getSize());

        this.direction = direction;
        this.configuration = configuration;
        this.stackHandler = stackHandler;
    }

    public boolean isNoneMode() {
        return configuration.getSideConfiguration().get(this.direction).getMode() == AccessModeConfig.NONE;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        final var slotConfiguration = configuration.getSlotConfiguration().get(slot);
        if (!slotConfiguration.getMode().isAllowInsert()) {
            return ItemStack.EMPTY;
        }

        final var sideConfiguration = this.configuration.getSideConfiguration().get(this.direction);
        if (sideConfiguration != null && !sideConfiguration.getMode().isAllowInsert()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        final var slotConfiguration = configuration.getSlotConfiguration().get(slot);
        if (!slotConfiguration.getMode().isAllowExtract()) {
            return ItemStack.EMPTY;
        }

        final var sideConfiguration = this.configuration.getSideConfiguration().get(this.direction);
        if (sideConfiguration != null && !sideConfiguration.getMode().isAllowExtract()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.extractItem(slot, amount, simulate);
    }
}
