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
import xyz.l7ssha.lushatest.registration.ItemRegistry;
import xyz.l7ssha.lushatest.utils.RegistryUtils;

import java.util.Map;
import java.util.function.Consumer;

public class DataGenRecipeProvider extends RecipeProvider {
    public static Integer TICKS_PER_SECOND = 20;

    protected final Map<Item, Integer> testTileEntityRecipeMap = Map.ofEntries( // TODO: Better balanced recipes
            Map.entry(Items.COAL, 3 * TICKS_PER_SECOND),
            Map.entry(Items.IRON_INGOT, 30 * TICKS_PER_SECOND),
            Map.entry(Items.GOLD_INGOT, 48 * TICKS_PER_SECOND),
            Map.entry(Items.DIAMOND, 120 * TICKS_PER_SECOND),
            Map.entry(Items.EMERALD, 180 * TICKS_PER_SECOND),
            Map.entry(Items.NETHERITE_SCRAP, 300 * TICKS_PER_SECOND)
    );

    public DataGenRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        for (final var recipeEntry : testTileEntityRecipeMap.entrySet()) {
            (new TestTileRecipeBuilder(recipeEntry.getKey(), recipeEntry.getValue())).save(recipeConsumer);
        }

        ShapedRecipeBuilder.shaped(ItemRegistry.TEST_ITEM.get())
                .pattern("CAC")
                .pattern("ANA")
                .pattern("CAC")
                .define('C', RegistryUtils.getItem("mekanism:ultimate_control_circuit"))
                .define('A', RegistryUtils.getItem("mekanism:pellet_antimatter"))
                .define('N', RegistryUtils.getBlock("mekanism:antiprotonic_nucleosynthesizer"))
                .unlockedBy("has_antimatter", has(RegistryUtils.getItem("mekanism:pellet_antimatter")))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.TEST_BLOCK.get())
                .pattern("CAC")
                .pattern("IMI")
                .pattern("CIC")
                .define('C', RegistryUtils.getItem("mekanism:ultimate_control_circuit"))
                .define('A', RegistryUtils.getItem("mekanism:pellet_antimatter"))
                .define('M', RegistryUtils.getBlock("mekanism:steel_casing"))
                .define('I', ItemRegistry.TEST_ITEM.get())
                .unlockedBy("has_antimatter", has(ItemRegistry.TEST_ITEM.get()))
                .save(recipeConsumer);
    }
}
