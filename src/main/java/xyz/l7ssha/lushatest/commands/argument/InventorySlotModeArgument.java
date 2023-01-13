package xyz.l7ssha.lushatest.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;

import java.util.concurrent.CompletableFuture;

public class InventorySlotModeArgument implements ArgumentType<AccessModeConfig> {
    @Override
    public AccessModeConfig parse(StringReader reader) {
        final var text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        return AccessModeConfig.fromLabel(text);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (final var inventoryConfig : AccessModeConfig.values()) {
            builder.suggest(inventoryConfig.getLabel());
        }

        return builder.buildFuture();
    }
}
