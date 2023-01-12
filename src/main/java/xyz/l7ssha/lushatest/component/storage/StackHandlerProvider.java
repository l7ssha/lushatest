package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.configuration.side.SideAccessConfiguration;
import xyz.l7ssha.lushatest.component.configuration.slot.SlotsConfiguration;
import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;

public class StackHandlerProvider<T extends LushaTestBlockEntity> {
    protected final static String INVENTORY_TAG = "inventory_capability_component_inventory";
    protected final static String CONFIG_SLOTS_NUMBER = "slotsNumber";
    protected final static String CONFIG_STACK_LIMIT = "stackLimit";
    protected final static String CONFIG_MODE = "mode";
    protected final static String CONFIG_SLOT_N_PATTERN = "slot[%s]";
    protected final static String CONFIG_HANDLER_CONFIGURATION_STACK = "stack_handler_configuration";

    private SlotsConfiguration slotsConfiguration;

    private SideAccessConfiguration sideAccessConfiguration;

    private final ItemStackHandler stackHandler;

    private final T blockEntity;

    public StackHandlerProvider(SlotsConfiguration slotsConfiguration, SideAccessConfiguration sideAccessConfiguration, T blockEntity) {
        this.slotsConfiguration = slotsConfiguration;
        this.sideAccessConfiguration = sideAccessConfiguration;
        this.blockEntity = blockEntity;

        this.stackHandler = createMainHandler(slotsConfiguration);
    }

    public LazyOptional<IItemHandler> getHandlerForSide(Direction direction) {
        final var wrapperHandler = new WrappedItemStackHandler(direction, this.slotsConfiguration, this.sideAccessConfiguration, this.getMainHandler());

        if (wrapperHandler.isNoneMode()) {
            return LazyOptional.empty();
        }

        return LazyOptional.of(() -> wrapperHandler).cast();
    }

    public SlotsConfiguration getSlotsConfiguration() {
        return slotsConfiguration;
    }

    public SideAccessConfiguration getSideAccessConfiguration() {
        return sideAccessConfiguration;
    }

    void saveAdditional(CompoundTag tag) {
        final var configurationTag = new CompoundTag();

        configurationTag.merge(this.slotsConfiguration.save());
        configurationTag.merge(this.sideAccessConfiguration.save());

        tag.put(CONFIG_HANDLER_CONFIGURATION_STACK, configurationTag);
        tag.put(INVENTORY_TAG, getMainHandler().serializeNBT());
    }

    void load(CompoundTag tag) {
        final var configurationTag = tag.getCompound(CONFIG_HANDLER_CONFIGURATION_STACK);

        this.slotsConfiguration = new SlotsConfiguration(configurationTag);
        this.sideAccessConfiguration = new SideAccessConfiguration(configurationTag);

        this.getMainHandler().deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    public ItemStackHandler getMainHandler() {
        return this.stackHandler;
    }

    protected ItemStackHandler createMainHandler(SlotsConfiguration configuration) {
        return new ItemStackHandler(configuration.getSize()) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                var copiedStack = stack.copy();

                var returnStack = super.insertItem(slot, copiedStack, simulate);
                stack.shrink(stack.getCount() - returnStack.getCount());

                if (!simulate) {
                    StackHandlerProvider.this.blockEntity.updateBlockEntity();
                }

                return returnStack;
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                var returnStack = super.extractItem(slot, amount, simulate);

                if (!simulate) {
                    StackHandlerProvider.this.blockEntity.updateBlockEntity();
                }

                return returnStack;
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                final var slotConfiguration = configuration.getSlotAccessConfiguration(slot);
                if (slotConfiguration == null) {
                    return stack.getMaxStackSize();
                }

                return Math.min(slotConfiguration.getSlotLimit(), stack.getMaxStackSize());
            }
        };
    }
}
