package xyz.l7ssha.lushatest.component;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public interface ICapabilityComponent<T> {
    Capability<T> getType();

    LazyOptional<T> getCapability(@Nullable Direction side);

    T getComponent();

    void saveAdditional(CompoundTag tag);

    void load(CompoundTag tag);
}
