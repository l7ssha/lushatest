package xyz.l7ssha.lushatest.component.configuration.slot;

import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SlotsConfigurationBuilder {
    private final Map<Integer, SlotAccessConfigurationBuilder> slotAccessConfiguration;

    private int size;

    public SlotsConfigurationBuilder(Map<Integer, SlotAccessConfigurationBuilder> slotAccessConfiguration, int size) {
        this.slotAccessConfiguration = slotAccessConfiguration;
        this.size = size;
    }

    public SlotsConfigurationBuilder() {
        this(new HashMap<>(), 0);
    }

    public SlotsConfigurationBuilder setSize(int size) {
        this.size = size;

        return this;
    }

    public SlotsConfigurationBuilder addSlotConfiguration(int slot, SlotAccessConfigurationBuilder configuration) {
        this.slotAccessConfiguration.put(slot, configuration);

        return this;
    }

    public SlotsConfiguration build() {
        return new SlotsConfiguration(
                size,
                slotAccessConfiguration.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build()))
        );
    }

    public static class SlotAccessConfigurationBuilder {
        private final int slotLimit;
        private final AccessModeConfig mode;

        public SlotAccessConfigurationBuilder(int slotLimit, AccessModeConfig mode) {
            this.slotLimit = slotLimit;
            this.mode = mode;
        }

        public SlotAccessConfiguration build() {
            return new SlotAccessConfiguration(slotLimit, mode);
        }
    }
}
