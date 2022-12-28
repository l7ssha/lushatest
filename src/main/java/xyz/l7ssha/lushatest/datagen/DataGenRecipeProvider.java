package xyz.l7ssha.lushatest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.datagen.recipe.TestTileRecipeBuilder;

import java.util.function.Consumer;

public class DataGenRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public DataGenRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        (new TestTileRecipeBuilder(Items.DIAMOND, Integer.MAX_VALUE / 128)).save(recipeConsumer);
        (new TestTileRecipeBuilder(Items.EMERALD, Integer.MAX_VALUE / 64)).save(recipeConsumer);
    }
}
