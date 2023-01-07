package xyz.l7ssha.lushatest.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;

@JeiPlugin
public class LushaTestJeiPlugin implements IModPlugin {
    final static RecipeType<TestTileEntityRecipe> TEST_TILE_ENTITY_RECIPE_RECIPE_TYPE = new RecipeType<>(
            TestTileRecipeCategory.CATEGORY_ID, TestTileEntityRecipe.class
    );

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LushaTestMod.MOD_ID, "test_lusha_jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new TestTileRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        final var recipesTestTile = recipeManager.getAllRecipesFor(TestTileEntityRecipe.Type.INSTANCE);
        registration.addRecipes(TEST_TILE_ENTITY_RECIPE_RECIPE_TYPE, recipesTestTile);
    }
}
