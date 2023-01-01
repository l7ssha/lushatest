package xyz.l7ssha.lushatest.component.storage;

import java.util.HashMap;
import java.util.Map;

public class StackHandlerConfiguration {

    private final Map<Integer, StackHandlerConfiguration.SlotConfiguration> slotConfiguration;

    private final int size;

    public static class SlotConfiguration {
        private final int slotLimit;

        private final InventoryConfigMode mode;

        private SlotConfiguration(int slotLimit, InventoryConfigMode mode) {
            this.slotLimit = slotLimit;
            this.mode = mode;
        }

        public int getSlotLimit() {
            return slotLimit;
        }

        public boolean isAllowInsert() {
            return switch (mode) {
                case INPUT, INPUT_OUTPUT -> true;
                default -> false;
            };
        }

        public boolean isAllowExtract() {
            return switch (mode) {
                case OUTPUT, INPUT_OUTPUT -> true;
                default -> false;
            };
        }

        public InventoryConfigMode getMode() {
            return mode;
        }
    }

    public StackHandlerConfiguration(StorageComponentStackHandlerBuilder builder) {
        this.slotConfiguration = new HashMap<>();
        this.size = builder.getSize();

        for(final var entry: builder.getSlots().entrySet()) {
            slotConfiguration.put(
                    entry.getKey(),
                    new SlotConfiguration(entry.getValue().getSlotLimit(), entry.getValue().getMode())
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
