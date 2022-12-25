package xyz.l7ssha.lushatest.component;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public interface ICapabilityComponent<T> {
    public Capability<T> getType();

    public LazyOptional<T> getCapability(@Nullable Direction side);

    public T getComponent();

    public void saveAdditional(CompoundTag tag);

    public void load(CompoundTag tag);
}
