package xyz.l7ssha.lushatest.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;
import xyz.l7ssha.lushatest.registration.BlockRegistry;

public class TestTileRecipeCategory implements IRecipeCategory<TestTileEntityRecipe> {
    public final static ResourceLocation CATEGORY_ID = new ResourceLocation(LushaTestMod.MOD_ID, "test_tile_recipe_jei_category");
    private final static ResourceLocation TEXTURE = new ResourceLocation(LushaTestMod.MOD_ID, "textures/gui/test_block_container.png");

    private final IDrawable background;
    private final IDrawable icon;

    public TestTileRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 4, 4, 168, 74);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.TEST_BLOCK.get()));
    }

    @Override
    public @NotNull RecipeType<TestTileEntityRecipe> getRecipeType() {
        return IRecipeCategory.super.getRecipeType();
    }

    @Override
    public @NotNull Component getTitle() {
        return new TranslatableComponent("lushatest.blockduplicator");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public ResourceLocation getUid() {
        return CATEGORY_ID;
    }

    @Override
    public Class<? extends TestTileEntityRecipe> getRecipeClass() {
        return TestTileEntityRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TestTileEntityRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 32).addIngredients(recipe.getInputItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 32).addIngredients(recipe.getInputItem());
    }
}
