package xyz.l7ssha.lushatest.component.configuration.slot;

import net.minecraft.nbt.CompoundTag;
import xyz.l7ssha.lushatest.component.ICompoundTaggable;

import java.util.HashMap;
import java.util.Map;

public class SlotsConfiguration implements ICompoundTaggable {
    private final static String SLOTS_CONFIGURATION_KEY = "slots_configuration";
    private final static String SLOT_N_PATTERN = "slot[%s]";
    private final static String SIZE_KEY = "size";

    private final Map<Integer, SlotAccessConfiguration> slotAccessConfiguration;
    private int size;

    public SlotsConfiguration(int size, Map<Integer, SlotAccessConfiguration> slotAccessConfiguration) {
        this.size = size;
        this.slotAccessConfiguration = slotAccessConfiguration;
    }

    public SlotsConfiguration(CompoundTag tag) {
        slotAccessConfiguration = new HashMap<>();

        load(tag);
    }

    public Map<Integer, SlotAccessConfiguration> getSlotAccessConfiguration() {
        return slotAccessConfiguration;
    }

    public SlotAccessConfiguration getSlotAccessConfiguration(int id) {
        return this.getSlotAccessConfiguration().get(id);
    }

    public int getSize() {
        return size;
    }

    @Override
    public CompoundTag save() {
        final var slotConfiguration = new CompoundTag();
        slotConfiguration.putInt(SIZE_KEY, this.size);

        for (final var slotEntry : this.slotAccessConfiguration.entrySet()) {
            slotConfiguration.put(SLOT_N_PATTERN.formatted(slotEntry.getKey()), slotEntry.getValue().save());
        }

        final var outputTag = new CompoundTag();
        outputTag.put(SLOTS_CONFIGURATION_KEY, slotConfiguration);

        return outputTag;
    }

    @Override
    public void load(CompoundTag tag) {
        final var slotTag = tag.getCompound(SLOTS_CONFIGURATION_KEY);

        this.size = slotTag.getInt(SIZE_KEY);

        for (var i = 0; i < this.size; i++) {
            this.slotAccessConfiguration.put(i, new SlotAccessConfiguration(slotTag.getCompound(SLOT_N_PATTERN.formatted(i))));
        }
    }
}
