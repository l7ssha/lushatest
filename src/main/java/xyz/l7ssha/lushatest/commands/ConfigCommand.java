package xyz.l7ssha.lushatest.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageComponentStackHandlerBuilder;
import xyz.l7ssha.lushatest.core.LushaComponentBlockEntity;
import xyz.l7ssha.lushatest.utils.RayTraceUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ConfigCommand {
    public static class IntegerArgument implements ArgumentType<Integer> {

        @Override
        public Integer parse(StringReader reader) throws CommandSyntaxException {
            return reader.readInt();
        }

        @Override
        public Collection<String> getExamples() {
            return Arrays.asList("0", "1");
        }
    }

    public static class InventorySlotModeArgument implements ArgumentType<String> {
        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            return reader.readString();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (final var inventoryConfig: InventoryConfigMode.values()) {
                builder.suggest(inventoryConfig.getLabel());
            }

            return builder.buildFuture();
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final var command = Commands.literal("lushatest")
                .then(
                        Commands.literal("slotConfig")
                                .then(
                                        Commands.argument("mode", new InventorySlotModeArgument())
                                                .executes(ConfigCommand::slotConfig)
                                )
                );

        dispatcher.register(command);
    }

    public static int slotConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = (Player) context.getSource().getEntityOrException();

        final var rayHit = RayTraceUtils.rayTraceFromPlayer(player, player.getLevel(), 10);
        final var blockEntity = player.getLevel().getBlockEntity(rayHit.getBlockPos());

        if (!(blockEntity instanceof final LushaComponentBlockEntity testTileEntity)) {
            context.getSource().sendFailure(new TextComponent("Not looking at compatible block or too far away!"));
            return -1;
        }

        final var itemHandlerComponent = testTileEntity.<StorageCapabilityComponent>getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (itemHandlerComponent.isEmpty()) {
            context.getSource().sendFailure(new TextComponent("Block cannot be configured since it doesn't have inventory!"));
            return -1;
        }

        final var modeLabel = context.getArgument("mode", String.class);

        final var stackHandlerProvider = itemHandlerComponent.get().getStackHandlerProvider();

        final var configurationBuilder = StorageComponentStackHandlerBuilder.fromConfig(stackHandlerProvider.getStackHandlerConfiguration());
        configurationBuilder.addSideConfig(rayHit.getDirection(), new StorageComponentStackHandlerBuilder.SideConfigBuilder(InventoryConfigMode.fromLabel(modeLabel)));

        stackHandlerProvider.setStackHandlerConfiguration(configurationBuilder.build());

        context.getSource().sendSuccess(new TextComponent("Block pos: (%s, %s, %s); Provided mode: %s".formatted(rayHit.getBlockPos().getX(), rayHit.getBlockPos().getY(), rayHit.getBlockPos().getZ(), modeLabel)), true);

        return 1;
    }
}
