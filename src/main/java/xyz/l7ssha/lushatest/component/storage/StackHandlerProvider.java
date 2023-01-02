package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;

public class StackHandlerProvider<T extends LushaTestBlockEntity> {
    protected final static String INVENTORY_TAG = "inventory_capability_component_inventory";
    protected final static String CONFIG_SIZE = "size";
    protected final static String CONFIG_SLOTS_NUMBER = "slotsNumber";
    protected final static String CONFIG_STACK_LIMIT = "stackLimit";
    protected final static String CONFIG_MODE = "mode";
    protected final static String CONFIG_SLOT_N_PATTERN = "slot[%s]";
    protected final static String CONFIG_HANDLER_CONFIGURATION_STACK = "stack_handler_configuration";
    protected static final String CONFIG_SIDE_PATTERN = "side[%s]";

    private StackHandlerConfiguration stackHandlerConfiguration;

    private final ItemStackHandler stackHandler;

    private final T blockEntity;

    public StackHandlerProvider(StackHandlerConfiguration stackHandlerConfiguration, T blockEntity) {
        this.stackHandlerConfiguration = stackHandlerConfiguration;
        this.blockEntity = blockEntity;

        this.stackHandler = createMainHandler(stackHandlerConfiguration);
    }

    public StackHandlerConfiguration getStackHandlerConfiguration() {
        return stackHandlerConfiguration;
    }

    public void setStackHandlerConfiguration(StackHandlerConfiguration stackHandlerConfiguration) {
        this.stackHandlerConfiguration = stackHandlerConfiguration;
        this.blockEntity.updateBlockEntity();
    }

    public LazyOptional<IItemHandler> getHandlerForSide(Direction direction) {
        final var wrapperHandler = new WrappedItemStackHandler(direction, this.getStackHandlerConfiguration(), this.getMainHandler());

        if (wrapperHandler.isNoneMode()) {
            return LazyOptional.empty();
        }

        return LazyOptional.of(() -> wrapperHandler).cast();
    }

    void saveAdditional(CompoundTag tag) {
        final var configurationTag = new CompoundTag();
        configurationTag.putInt(CONFIG_SIZE, this.getStackHandlerConfiguration().getSize());
        configurationTag.putInt(CONFIG_SLOTS_NUMBER, this.getStackHandlerConfiguration().getSideConfiguration().size());

        for (final var slotConfig : this.getStackHandlerConfiguration().getSlotConfiguration().entrySet()) {
            final var slotConfigTag = new CompoundTag();

            slotConfigTag.putInt(CONFIG_STACK_LIMIT, slotConfig.getValue().getSlotLimit());
            slotConfigTag.putInt(CONFIG_MODE, slotConfig.getValue().getMode().getIndex());

            configurationTag.put(CONFIG_SLOT_N_PATTERN.formatted(slotConfig.getKey()), slotConfigTag);
        }

        for (final var sideConfig : this.getStackHandlerConfiguration().getSideConfiguration().entrySet()) {
            final var sideConfigTag = new CompoundTag();

            sideConfigTag.putInt(CONFIG_MODE, sideConfig.getValue().getMode().getIndex());
            configurationTag.put(CONFIG_SIDE_PATTERN.formatted(sideConfig.getKey().getName()), sideConfigTag);
        }

        tag.put(CONFIG_HANDLER_CONFIGURATION_STACK, configurationTag);

        tag.put(INVENTORY_TAG, getMainHandler().serializeNBT());
    }

    void load(CompoundTag tag) {
        final var configurationTag = tag.getCompound(CONFIG_HANDLER_CONFIGURATION_STACK);

        final var configBuilder = new StorageComponentStackHandlerBuilder();
        configBuilder.setSize(configurationTag.getInt(CONFIG_SIZE));

        final var slotsNumber = configurationTag.getInt(CONFIG_SLOTS_NUMBER);

        for (var i = 0; i < slotsNumber; i++) {
            final var slotTag = configurationTag.getCompound(CONFIG_SLOT_N_PATTERN.formatted(i));

            configBuilder.addSlotConfig(i, new StorageComponentStackHandlerBuilder.SlotConfigBuilder(
                    slotTag.getInt(CONFIG_STACK_LIMIT),
                    InventoryConfigMode.fromIndex(slotTag.getInt(CONFIG_MODE))
            ));
        }

        for (final var side : Direction.values()) {
            final var sideTag = configurationTag.getCompound(CONFIG_SIDE_PATTERN.formatted(side.getName()));

            configBuilder.addSideConfig(side, new StorageComponentStackHandlerBuilder.SideConfigBuilder(
                    InventoryConfigMode.fromIndex(sideTag.getInt(CONFIG_MODE))
            ));
        }

        this.setStackHandlerConfiguration(configBuilder.build());

        this.getMainHandler().deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    public ItemStackHandler getMainHandler() {
        return this.stackHandler;
    }

    protected ItemStackHandler createMainHandler(StackHandlerConfiguration configuration) {
        return new ItemStackHandler(configuration.getSize()) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                final var slotConfiguration = configuration.getSlotConfiguration().get(slot);
                if (!slotConfiguration.getMode().isAllowInsert()) {
                    return ItemStack.EMPTY;
                }

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
                final var slotConfiguration = configuration.getSlotConfiguration().get(slot);
                if (!slotConfiguration.getMode().isAllowExtract()) {
                    return ItemStack.EMPTY;
                }

                var returnStack = super.extractItem(slot, amount, simulate);

                if (!simulate) {
                    StackHandlerProvider.this.blockEntity.updateBlockEntity();
                }

                return returnStack;
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                final var slotConfiguration = configuration.getSlotConfiguration().get(slot);
                if (slotConfiguration == null) {
                    return stack.getMaxStackSize();
                }

                return Math.min(slotConfiguration.getSlotLimit(), stack.getMaxStackSize());
            }
        };
    }
}
