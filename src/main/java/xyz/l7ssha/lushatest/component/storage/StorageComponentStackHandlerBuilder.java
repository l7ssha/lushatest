package xyz.l7ssha.lushatest.component.storage;

import java.util.HashMap;
import java.util.Map;

public final class StorageComponentStackHandlerBuilder {

    private final Map<Integer, SlotConfigBuilder> slots = new HashMap<>();

    private int size = 1;

    public static class SlotConfigBuilder {
        private int slotLimit;

        private boolean allowInsert;

        private boolean allowExtract;

        public SlotConfigBuilder() {
            this.slotLimit = 64;
            this.allowInsert = false;
            this.allowExtract = false;
        }

        public SlotConfigBuilder(int slotLimit, boolean allowInsert, boolean allowExtract) {
            this.slotLimit = slotLimit;
            this.allowInsert = allowInsert;
            this.allowExtract = allowExtract;
        }

        public int getSlotLimit() {
            return slotLimit;
        }

        public SlotConfigBuilder setSlotLimit(int slotLimit) {
            this.slotLimit = slotLimit;
            return this;
        }

        public boolean isAllowInsert() {
            return allowInsert;
        }

        public SlotConfigBuilder setAllowInsert(boolean allowInsert) {
            this.allowInsert = allowInsert;
            return this;
        }

        public boolean isAllowExtract() {
            return allowExtract;
        }

        public SlotConfigBuilder setAllowExtract(boolean allowExtract) {
            this.allowExtract = allowExtract;
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
                            .setAllowExtract(slotConfigEntry.getValue().isAllowExtract())
                            .setAllowInsert(slotConfigEntry.getValue().isAllowInsert())
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
