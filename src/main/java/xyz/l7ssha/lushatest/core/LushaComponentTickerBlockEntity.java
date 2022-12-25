package xyz.l7ssha.lushatest.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.l7ssha.lushatest.component.ICapabilityTicker;

public class LushaComponentTickerBlockEntity<T extends BlockEntity> extends LushaComponentBlockEntity implements BlockEntityTicker<T> {
    public LushaComponentTickerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick(Level world, BlockPos blockPos, BlockState blockState, T blockEntity) {
        if (world.isClientSide()) {
            return;
        }

        for(final var component: this.getComponents()) {
            if (component instanceof ICapabilityTicker<?>) {
                ((ICapabilityTicker<T>) component).tick(world, blockPos, blockState, blockEntity);
            }
        }
    }
}
