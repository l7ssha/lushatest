package xyz.l7ssha.lushatest.component.storage;

import java.util.HashMap;
import java.util.Map;

public class StackHandlerConfiguration {

    private final Map<Integer, StackHandlerConfiguration.SlotConfiguration> slotConfiguration;

    private final int size;

    public static class SlotConfiguration {
        private final int slotLimit;

        private final boolean allowInsert;

        private final boolean allowExtract;

        private SlotConfiguration(int slotLimit, boolean allowInsert, boolean allowExtract) {
            this.slotLimit = slotLimit;
            this.allowInsert = allowInsert;
            this.allowExtract = allowExtract;
        }

        public int getSlotLimit() {
            return slotLimit;
        }

        public boolean isAllowInsert() {
            return allowInsert;
        }

        public boolean isAllowExtract() {
            return allowExtract;
        }
    }

    public StackHandlerConfiguration(StorageComponentStackHandlerBuilder builder) {
        this.slotConfiguration = new HashMap<>();
        this.size = builder.getSize();

        for(final var entry: builder.getSlots().entrySet()) {
            slotConfiguration.put(
                    entry.getKey(),
                    new SlotConfiguration(entry.getValue().getSlotLimit(), entry.getValue().isAllowInsert(), entry.getValue().isAllowExtract())
            );
        }
    }

    public Map<Integer, SlotConfiguration> getSlotConfiguration() {
        return slotConfiguration;
    }

    public int getSize() {
        return size;
    }
}
