package xyz.l7ssha.lushatest.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public final class RayTraceUtils {
    @NotNull
    public static BlockHitResult rayTraceFromPlayer(@NotNull Player player, @NotNull Level world, double rayLength) {
        final var playerRotation = player.getViewVector(0);
        final var rayFrom = player.getEyePosition(0);

        final var rayPath = playerRotation.scale(rayLength);
        final var rayTo = rayFrom.add(rayPath);

        return world.clip(
                new ClipContext(rayFrom, rayTo, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null)
        );
    }
}
