package xyz.l7ssha.lushatest.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;

import java.util.concurrent.CompletableFuture;

public class InventorySlotModeArgument implements ArgumentType<String> {
    @Override
    public String parse(StringReader reader) {
        final var text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (final var inventoryConfig : InventoryConfigMode.values()) {
            builder.suggest(inventoryConfig.getLabel());
        }

        return builder.buildFuture();
    }
}
