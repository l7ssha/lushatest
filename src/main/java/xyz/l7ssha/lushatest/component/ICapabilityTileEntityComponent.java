package xyz.l7ssha.lushatest.component;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICapabilityTileEntityComponent<S extends BlockEntity> {

    S getBlockEntity();
}
