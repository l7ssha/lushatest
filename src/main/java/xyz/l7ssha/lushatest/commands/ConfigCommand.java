package xyz.l7ssha.lushatest.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.core.LushaComponentBlockEntity;
import xyz.l7ssha.lushatest.utils.StorageComponentStackHandlerBuilder;

import java.util.Arrays;
import java.util.Collection;

public class ConfigCommand {

    public static class IntegerArgument implements ArgumentType<Integer> {

        @Override
        public Integer parse(StringReader reader) throws CommandSyntaxException {
            return reader.readInt();
        }

        @Override
        public Collection<String> getExamples() {
            return Arrays.asList("1", "2");
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final var command = Commands.literal("lushatest")
                .then(
                        Commands.literal("slotConfig")
                                .then(
                                        Commands.argument("slot", new IntegerArgument())
                                                .then(
                                                        Commands.argument("stackLimit", new IntegerArgument())
                                                                .executes(ConfigCommand::slotConfig)
                                                )
                                )
                );

        dispatcher.register(command);
    }

    public static int slotConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = (Player) context.getSource().getEntityOrException();
        final var rayHit = rayTraceFromPlayer(player);
        final var blockEntity = player.getLevel().getBlockEntity(rayHit.getBlockPos());

        if (!(blockEntity instanceof final LushaComponentBlockEntity testTileEntity)) {
            context.getSource().sendFailure(new TextComponent("Not looking at compatible block or too far away!"));
            return -1;
        }

        final var itemHandlerComponent = testTileEntity.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (itemHandlerComponent.isEmpty()) {
            context.getSource().sendFailure(new TextComponent("Block cannot be configured since it doesn't have inventory!"));
            return -1;
        }

        final var slot = context.getArgument("slot", Integer.class);
        final var stackLimit = context.getArgument("stackLimit", Integer.class);

        ((StorageCapabilityComponent)itemHandlerComponent.get()).getStackHandlerProvider().setStackHandlerConfiguration(
                new StorageComponentStackHandlerBuilder()
                        .setSize(2)
                        .addSlot(0, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(1).setAllowExtract(false).setAllowInsert(false))
                        .addSlot(1, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(stackLimit).setAllowExtract(true).setAllowInsert(false))
                        .build()
        );

        context.getSource().sendSuccess(new TextComponent("Block pos: (%s, %s, %s); Provided slot: %s, Provided slot stack limit: %s".formatted(rayHit.getBlockPos().getX(), rayHit.getBlockPos().getY(), rayHit.getBlockPos().getZ(), slot, stackLimit)), true);

        return 1;
    }

    @NotNull
    private static BlockHitResult rayTraceFromPlayer(Player player) {
        double rayLength = 10f;
        final var playerRotation = player.getViewVector(0);
        final var rayPath = playerRotation.scale(rayLength);

        final var from = player.getEyePosition(0);
        final var to = from.add(rayPath);

        final var rayCtx = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null);
        return (player.getLevel()).clip(rayCtx);
    }
}
