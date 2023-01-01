package xyz.l7ssha.lushatest.component.storage;

import java.util.HashMap;
import java.util.Map;

public final class StorageComponentStackHandlerBuilder {

    private final Map<Integer, SlotConfigBuilder> slots = new HashMap<>();

    private int size = 1;

    public static class SlotConfigBuilder {
        private int slotLimit;

        private InventoryConfigMode mode;

        public SlotConfigBuilder() {
            this.slotLimit = 64;
            this.mode = InventoryConfigMode.NONE;
        }

        public SlotConfigBuilder(int slotLimit, InventoryConfigMode mode) {
            this.slotLimit = slotLimit;
            this.mode = mode;
        }

        public int getSlotLimit() {
            return slotLimit;
        }

        public SlotConfigBuilder setSlotLimit(int slotLimit) {
            this.slotLimit = slotLimit;
            return this;
        }

        public InventoryConfigMode getMode() {
            return mode;
        }

        public SlotConfigBuilder setMode(InventoryConfigMode mode) {
            this.mode = mode;
            return this;
        }
    }

    public static StorageComponentStackHandlerBuilder fromConfig(StackHandlerConfiguration configuration) {
        final var builder = new StorageComponentStackHandlerBuilder();

        builder.setSize(configuration.getSize());
        for (final var slotConfigEntry: configuration.getSlotConfiguration().entrySet()) {
            builder.addSlot(
                    slotConfigEntry.getKey(),
                    new StorageComponentStackHandlerBuilder.SlotConfigBuilder()
                            .setSlotLimit(slotConfigEntry.getValue().getSlotLimit())
                            .setMode(slotConfigEntry.getValue().getMode())
            );
        }

        return builder;
    }

    public Map<Integer, SlotConfigBuilder> getSlots() {
        return slots;
    }

    public int getSize() {
        return size;
    }

    public StorageComponentStackHandlerBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public StorageComponentStackHandlerBuilder addSlot(int id, SlotConfigBuilder slotConfigBuilder) {
        this.slots.put(id, slotConfigBuilder);

        return this;
    }

    public StackHandlerConfiguration build() {
        return new StackHandlerConfiguration(this);
    }
}
