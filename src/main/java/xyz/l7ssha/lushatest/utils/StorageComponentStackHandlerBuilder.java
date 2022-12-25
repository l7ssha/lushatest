package xyz.l7ssha.lushatest.utils;

import xyz.l7ssha.lushatest.component.storage.StackHandlerConfiguration;

import java.util.HashMap;
import java.util.Map;

public final class StorageComponentStackHandlerBuilder {

    private Map<Integer, SlotConfigBuilder> slots = new HashMap<>();

    private int size = 1;

    public static class SlotConfigBuilder {
        private int slotLimit;

        private boolean allowInsert;

        private boolean allowExtract;

        public SlotConfigBuilder() {
            this.slotLimit = 64;
            this.allowInsert = true;
            this.allowExtract = true;
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

    public StorageComponentStackHandlerBuilder setSlots(Map<Integer, SlotConfigBuilder> slots) {
        this.slots = slots;
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
