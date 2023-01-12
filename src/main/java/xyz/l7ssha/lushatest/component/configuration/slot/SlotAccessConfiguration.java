package xyz.l7ssha.lushatest.component.configuration.slot;

import net.minecraft.nbt.CompoundTag;
import xyz.l7ssha.lushatest.component.ICompoundTaggable;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;

public class SlotAccessConfiguration implements ICompoundTaggable {
    private final static String MODE_KEY = "mode";
    private final static String SLOT_LIMIT_KEY = "slot_limit";

    private int slotLimit;
    private AccessModeConfig mode;

    public SlotAccessConfiguration(int slotLimit, AccessModeConfig mode) {
        this.slotLimit = slotLimit;
        this.mode = mode;
    }

    public SlotAccessConfiguration(CompoundTag tag) {
        load(tag);
    }

    public AccessModeConfig getMode() {
        return mode;
    }

    public int getSlotLimit() {
        return slotLimit;
    }

    @Override
    public CompoundTag save() {
        final var tag = new CompoundTag();
        tag.putInt(MODE_KEY, mode.getIndex());
        tag.putInt(SLOT_LIMIT_KEY, slotLimit);

        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        mode = AccessModeConfig.fromIndex(tag.getInt(MODE_KEY));
        slotLimit = tag.getInt(SLOT_LIMIT_KEY);
    }
}
