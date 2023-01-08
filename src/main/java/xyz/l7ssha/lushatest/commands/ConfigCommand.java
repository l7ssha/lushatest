package xyz.l7ssha.lushatest.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.commands.argument.InventorySlotModeArgument;
import xyz.l7ssha.lushatest.component.AccessModeConfig;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageComponentStackHandlerBuilder;
import xyz.l7ssha.lushatest.core.LushaComponentBlockEntity;
import xyz.l7ssha.lushatest.tileentities.IActivableBlockEntity;
import xyz.l7ssha.lushatest.utils.RayTraceUtils;

public class ConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final var mainCommand = Commands.literal("lushatest");

        final var slotConfigCommand = mainCommand.then(
                Commands.literal("sideConfig")
                        .then(
                                Commands.argument("mode", new InventorySlotModeArgument())
                                        .executes(ConfigCommand::slotConfig)
                        )
        );
        final var activeConfigCommand = mainCommand.then(
                Commands.literal("activeConfig")
                        .executes(ConfigCommand::activeConfig)
        );

        dispatcher.register(slotConfigCommand);
        dispatcher.register(activeConfigCommand);
    }

    private static int activeConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var locateResult = locateTileEntity(context);
        if (locateResult.blockEntity == null) {
            context.getSource().sendFailure(Component.literal("Not looking at compatible block or too far away!"));
            return -1;
        }

        if (locateResult.blockEntity instanceof final IActivableBlockEntity activableBlockEntity) {
            activableBlockEntity.setActiveState(!activableBlockEntity.getActiveState());
            context.getSource().sendSuccess(Component.literal("Success! Current active state is: %s".formatted(activableBlockEntity.getActiveState())), true);
            return 0;
        }

        context.getSource().sendFailure(Component.literal("Not looking at compatible block or too far away!"));
        return -1;
    }

    public static int slotConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var locateResult = locateTileEntity(context);
        if (locateResult.blockEntity == null) {
            context.getSource().sendFailure(Component.literal("Not looking at compatible block or too far away!"));
            return -1;
        }

        final var itemHandlerComponent = locateResult.blockEntity.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER);
        if (itemHandlerComponent.isEmpty()) {
            context.getSource().sendFailure(Component.literal("Block cannot be configured since it doesn't have inventory!"));
            return -1;
        }

        var modeLabel = context.getArgument("mode", String.class);

        final var stackHandlerProvider = itemHandlerComponent.get().getStackHandlerProvider();

        final var configurationBuilder = StorageComponentStackHandlerBuilder.fromConfig(stackHandlerProvider.getStackHandlerConfiguration());
        configurationBuilder.addSideConfig(locateResult.blockHitResult.getDirection(), new StorageComponentStackHandlerBuilder.SideConfigBuilder(AccessModeConfig.fromLabel(modeLabel)));

        stackHandlerProvider.setStackHandlerConfiguration(configurationBuilder.build());

        context.getSource().sendSuccess(Component.literal("Block pos: (%s, %s, %s); Provided mode: %s".formatted(locateResult.blockHitResult.getBlockPos().getX(), locateResult.blockHitResult.getBlockPos().getY(), locateResult.blockHitResult.getBlockPos().getZ(), modeLabel)), true);
        return 1;
    }

    private static LocateBlockEntityResult locateTileEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = (Player) context.getSource().getEntityOrException();

        final var rayHit = RayTraceUtils.rayTraceFromPlayer(player, player.getLevel(), 10);
        final var blockEntity = player.getLevel().getBlockEntity(rayHit.getBlockPos());

        if (blockEntity instanceof LushaComponentBlockEntity) {
            return new LocateBlockEntityResult((LushaComponentBlockEntity) blockEntity, rayHit);
        }

        return new LocateBlockEntityResult(null, rayHit);
    }

    private static class LocateBlockEntityResult {
        public final @Nullable LushaComponentBlockEntity blockEntity;
        public final BlockHitResult blockHitResult;

        public LocateBlockEntityResult(@Nullable LushaComponentBlockEntity blockEntity, BlockHitResult blockHitResult) {
            this.blockEntity = blockEntity;
            this.blockHitResult = blockHitResult;
        }
    }
}
