package xyz.l7ssha.lushatest.component;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface ICapabilityTicker<T extends BlockEntity> {
    public void tick(Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull T tile);
}
