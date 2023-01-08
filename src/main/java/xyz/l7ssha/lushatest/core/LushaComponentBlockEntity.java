package xyz.l7ssha.lushatest.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import java.util.Optional;

public class LushaComponentBlockEntity extends LushaTestBlockEntity {
    protected Map<Capability<?>, ICapabilityComponent<?>> components;

    public LushaComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.components = new HashMap<>();
    }

    public <T> Optional<ICapabilityComponent<T>> getRawComponent(Capability<T> cap) {
        return Optional.ofNullable((ICapabilityComponent<T>) components.get(cap));
    }

    public <T extends ICapabilityComponent<?>> Optional<T> getComponent(Capability<?> cap) {
        return Optional.ofNullable((T) components.get(cap));
    }

    public Collection<ICapabilityComponent<?>> getComponents() {
        return this.components.values();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        final var component = this.components.get(cap);

        if (component != null) {
            return component.getCapability(side).cast();
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

        for (final var component : this.getComponents()) {
            component.saveAdditional(tag);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        for (final var component : this.getComponents()) {
            component.load(tag);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        final var nbt = serializeNBT();
        this.saveAdditional(nbt);

        return nbt;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);

        for (final var component : this.getComponents()) {
            component.load(pkt.getTag());
        }
    }

    protected void addComponent(ICapabilityComponent<?> component) {
        this.components.put(component.getType(), component);
    }
}
