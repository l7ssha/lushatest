package xyz.l7ssha.lushatest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.container.TestBlockContainer;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

final public class TestBlock extends Block implements EntityBlock, IForgeBlock {
    public TestBlock() {
        super(Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return BlockEntityRegistry.TEST_BLOCK_ENTITY.get().create(pos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        final var blockEntity = (TestTileEntity) level.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return InteractionResult.FAIL;
        }

        final var container = new SimpleMenuProvider(TestBlockContainer.getServerContainer(blockEntity, blockPos), new TranslatableComponent("lushatest.blockduplicator"));
        NetworkHooks.openGui((ServerPlayer) player, container, blockPos);

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        if (world.isClientSide()) {
            return null;
        }

        return (level0, pos, state0, blockEntity) -> ((TestTileEntity) blockEntity).tick(level0, pos, state0, (TestTileEntity) blockEntity);
    }
}
