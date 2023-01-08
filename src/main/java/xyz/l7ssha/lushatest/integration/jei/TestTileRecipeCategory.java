package xyz.l7ssha.lushatest.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.utils.Utils;

import java.util.List;

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
        return new RecipeType<>(CATEGORY_ID, TestTileEntityRecipe.class);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("lushatest.blockduplicator");
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
    public @NotNull List<Component> getTooltipStrings(TestTileEntityRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        final var requiredEnergy = recipe.getRecipeCost();

        if (mouseX > 136 + 4 && mouseX < 146 + 25 + 4 && mouseY > 10 + 4 && mouseY < 36 + 36 + 4) {
            return List.of(Component.literal(Utils.getStoredEnergyText(requiredEnergy)));
        }

        return List.of();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TestTileEntityRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 32).addIngredients(recipe.getInputItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 32).addIngredients(recipe.getInputItem());
    }
}
