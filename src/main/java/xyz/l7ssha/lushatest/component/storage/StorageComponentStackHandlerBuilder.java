package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;
import xyz.l7ssha.lushatest.component.AccessModeConfig;

import java.util.HashMap;
import java.util.Map;

public final class StorageComponentStackHandlerBuilder {

    private final Map<Integer, SlotConfigBuilder> slots = new HashMap<>();

    private final Map<Direction, SideConfigBuilder> sides = new HashMap<>();

    private int size = 1;

    public static class SlotConfigBuilder {
        private final int slotLimit;

        private final AccessModeConfig mode;

        public SlotConfigBuilder() {
            this.slotLimit = 64;
            this.mode = AccessModeConfig.NONE;
        }

        public SlotConfigBuilder(int slotLimit, AccessModeConfig mode) {
            this.slotLimit = slotLimit;
            this.mode = mode;
        }

        public int getSlotLimit() {
            return slotLimit;
        }

        public AccessModeConfig getMode() {
            return mode;
        }
    }

    public static class SideConfigBuilder {
        private final AccessModeConfig mode;

        public SideConfigBuilder(AccessModeConfig mode) {
            this.mode = mode;
        }

        public SideConfigBuilder() {
            this(AccessModeConfig.NONE);
        }

        public AccessModeConfig getMode() {
            return mode;
        }
    }

    public static StorageComponentStackHandlerBuilder fromConfig(StackHandlerConfiguration configuration) {
        final var builder = new StorageComponentStackHandlerBuilder();

        builder.setSize(configuration.getSize());

        for (final var slotConfigEntry : configuration.getSlotConfiguration().entrySet()) {
            builder.addSlotConfig(
                    slotConfigEntry.getKey(),
                    new StorageComponentStackHandlerBuilder.SlotConfigBuilder(slotConfigEntry.getValue().getSlotLimit(), slotConfigEntry.getValue().getMode())
            );
        }

        for (final var sideConfigEntry : configuration.getSideConfiguration().entrySet()) {
            builder.addSideConfig(
                    sideConfigEntry.getKey(),
                    new StorageComponentStackHandlerBuilder.SideConfigBuilder(sideConfigEntry.getValue().getMode())
            );
        }

        return builder;
    }

    public Map<Integer, SlotConfigBuilder> getSlots() {
        return slots;
    }

    public Map<Direction, SideConfigBuilder> getSides() {
        return sides;
    }

    public int getSize() {
        return size;
    }

    public StorageComponentStackHandlerBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public StorageComponentStackHandlerBuilder addSlotConfig(int id, SlotConfigBuilder slotConfigBuilder) {
        this.slots.put(id, slotConfigBuilder);

        return this;
    }

    public StorageComponentStackHandlerBuilder addSideConfig(Direction direction, SideConfigBuilder sideConfigBuilder) {
        this.sides.put(direction, sideConfigBuilder);

        return this;
    }

    public StorageComponentStackHandlerBuilder setCommonSideConfig(SideConfigBuilder sideConfigBuilder) {
        for (final var side : Direction.values()) {
            this.addSideConfig(side, sideConfigBuilder);
        }

        return this;
    }

    public StackHandlerConfiguration build() {
        return new StackHandlerConfiguration(this);
    }
}
