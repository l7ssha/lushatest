package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.SideAccessConfiguration;
import xyz.l7ssha.lushatest.component.configuration.slot.SlotsConfiguration;

public class WrappedItemStackHandler extends ItemStackHandler {
    private final SlotsConfiguration slotsConfiguration;
    private final SideAccessConfiguration sideAccessConfiguration;
    private final ItemStackHandler stackHandler;
    private final Direction direction;

    public WrappedItemStackHandler(Direction direction, SlotsConfiguration slotsConfiguration, SideAccessConfiguration sideAccessConfiguration, ItemStackHandler stackHandler) {
        super(slotsConfiguration.getSize());

        this.direction = direction;
        this.slotsConfiguration = slotsConfiguration;
        this.stackHandler = stackHandler;
        this.sideAccessConfiguration = sideAccessConfiguration;
    }

    public boolean isNoneMode() {
        return sideAccessConfiguration.getSideConfiguration(this.direction).getMode() == AccessModeConfig.NONE;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        final var slotConfiguration = slotsConfiguration.getSlotAccessConfiguration(slot);
        if (!slotConfiguration.getMode().isAllowInsert()) {
            return ItemStack.EMPTY;
        }

        final var sideConfiguration = this.sideAccessConfiguration.getSideConfiguration(this.direction).getMode();
        if (sideConfiguration != null && !sideConfiguration.isAllowInsert()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        final var slotConfiguration = slotsConfiguration.getSlotAccessConfiguration(slot);
        if (!slotConfiguration.getMode().isAllowExtract()) {
            return ItemStack.EMPTY;
        }

        final var sideConfiguration = this.sideAccessConfiguration.getSideConfiguration(this.direction).getMode();
        if (sideConfiguration != null && !sideConfiguration.isAllowExtract()) {
            return ItemStack.EMPTY;
        }

        return this.stackHandler.extractItem(slot, amount, simulate);
    }
}
