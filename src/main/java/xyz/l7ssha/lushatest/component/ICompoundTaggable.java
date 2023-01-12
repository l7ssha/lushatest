package xyz.l7ssha.lushatest.component;

import net.minecraft.nbt.CompoundTag;

public interface ICompoundTaggable {
    CompoundTag save();

    void load(CompoundTag tag);
}
