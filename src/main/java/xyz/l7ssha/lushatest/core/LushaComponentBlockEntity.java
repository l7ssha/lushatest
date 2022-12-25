package xyz.l7ssha.lushatest.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.ICapabilityComponent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LushaComponentBlockEntity extends LushaTestBlockEntity {
    protected Map<Capability<?>, ICapabilityComponent<?>> components;

    public LushaComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.components = new HashMap<>();
    }

    public <T> ICapabilityComponent<T> getComponent(Capability<T> cap) {
        return (ICapabilityComponent<T>) components.get(cap);
    }

    public Collection<ICapabilityComponent<?>> getComponents() {
        return this.components.values();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        for(final var component: this.getComponents()) {
            if (cap == component.getType()) {
                return component.getCapability(side).cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        for(final var component: this.getComponents()) {
            component.load(tag);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        for(final var component: this.getComponents()) {
            component.saveAdditional(tag);
        }
    }

    protected void addComponent(ICapabilityComponent<?> component) {
        this.components.put(component.getType(), component);
    }
}
