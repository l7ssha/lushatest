package xyz.l7ssha.lushatest.component.configuration.side;

import net.minecraft.nbt.CompoundTag;
import xyz.l7ssha.lushatest.component.ICompoundTaggable;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;

public class DirectionAccessConfiguration implements ICompoundTaggable {
    private final static String MODE_KEY = "mode";

    private AccessModeConfig mode;

    public DirectionAccessConfiguration(AccessModeConfig mode) {
        this.mode = mode;
    }

    public DirectionAccessConfiguration(CompoundTag tag) {
        load(tag);
    }

    public AccessModeConfig getMode() {
        return mode;
    }

    @Override
    public CompoundTag save() {
        final var tag = new CompoundTag();
        tag.putInt(MODE_KEY, mode.getIndex());

        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        mode = AccessModeConfig.fromIndex(tag.getInt(MODE_KEY));
    }
}
