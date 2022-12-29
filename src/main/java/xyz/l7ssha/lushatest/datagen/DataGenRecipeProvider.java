package xyz.l7ssha.lushatest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.datagen.recipe.TestTileRecipeBuilder;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.utils.RegistryUtils;

import java.util.Map;
import java.util.function.Consumer;

public class DataGenRecipeProvider extends RecipeProvider {
    protected final Map<Item, Integer> testTileEntityRecipeMap = Map.ofEntries(
            Map.entry(Items.COAL, Integer.MAX_VALUE / 512),
            Map.entry(Items.IRON_INGOT, Integer.MAX_VALUE / 128),
            Map.entry(Items.GOLD_INGOT, Integer.MAX_VALUE / 96),
            Map.entry(Items.DIAMOND, Integer.MAX_VALUE / 64),
            Map.entry(Items.EMERALD, Integer.MAX_VALUE / 32),
            Map.entry(Items.NETHERITE_SCRAP, Integer.MAX_VALUE / 4)
    );

    public DataGenRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        for(final var recipeEntry: testTileEntityRecipeMap.entrySet()) {
            (new TestTileRecipeBuilder(recipeEntry.getKey(), recipeEntry.getValue())).save(recipeConsumer);
        }

        ShapedRecipeBuilder.shaped(BlockRegistry.TEST_BLOCK.get())
                .pattern("CAC")
                .pattern("ANA")
                .pattern("NAN")
                .define('C', RegistryUtils.getItem("mekanism:ultimate_control_circuit"))
                .define('A', RegistryUtils.getItem("mekanism:pellet_antimatter"))
                .define('N', RegistryUtils.getBlock("mekanism:antiprotonic_nucleosynthesizer"))
                .unlockedBy("has_antimatter", has(RegistryUtils.getItem("mekanism:pellet_antimatter")))
                .save(recipeConsumer);
    }
}
