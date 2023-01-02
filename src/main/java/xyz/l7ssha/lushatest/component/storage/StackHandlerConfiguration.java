package xyz.l7ssha.lushatest.component.storage;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class StackHandlerConfiguration {

    private final Map<Integer, StackHandlerConfiguration.SlotConfiguration> slotConfiguration;

    private final Map<Direction, SideConfiguration> sideConfiguration;

    private final int size;

    public static class SlotConfiguration {
        private final int slotLimit;

        private final InventoryConfigMode mode;

        public SlotConfiguration(int slotLimit, InventoryConfigMode mode) {
            this.slotLimit = slotLimit;
            this.mode = mode;
        }

        public InventoryConfigMode getMode() {
            return mode;
        }

        public int getSlotLimit() {
            return slotLimit;
        }
    }

    public static class SideConfiguration {
        private final InventoryConfigMode mode;

        public SideConfiguration(InventoryConfigMode mode) {
            this.mode = mode;
        }

        public InventoryConfigMode getMode() {
            return mode;
        }
    }

    public StackHandlerConfiguration(StorageComponentStackHandlerBuilder builder) {
        this.slotConfiguration = new HashMap<>();
        this.sideConfiguration = new HashMap<>();
        this.size = builder.getSize();

        for (final var entry : builder.getSlots().entrySet()) {
            slotConfiguration.put(
                    entry.getKey(),
                    new SlotConfiguration(entry.getValue().getSlotLimit(), entry.getValue().getMode())
            );
        }

        for (final var entry : builder.getSides().entrySet()) {
            sideConfiguration.put(
                    entry.getKey(),
                    new SideConfiguration(entry.getValue().getMode())
            );
        }
    }

    public Map<Integer, SlotConfiguration> getSlotConfiguration() {
        return slotConfiguration;
    }

    public Map<Direction, SideConfiguration> getSideConfiguration() {
        return sideConfiguration;
    }

    public int getSize() {
        return size;
    }
}
