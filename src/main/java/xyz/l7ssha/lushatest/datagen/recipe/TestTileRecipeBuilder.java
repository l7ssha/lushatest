package xyz.l7ssha.lushatest.datagen.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;

import java.util.function.Consumer;

public class TestTileRecipeBuilder implements RecipeBuilder {
    private final Item input;

    private final int processingCost;

    public TestTileRecipeBuilder(Item input, int processingCost) {
        this.input = input;
        this.processingCost = processingCost;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance triggerInstance) {
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String name) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return input;
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull ResourceLocation id) {
        recipeConsumer.accept(
                new TestTileRecipeBuilder.Result(
                        this.input,
                        this.processingCost
                )
        );
    }

  protected static class Result implements FinishedRecipe {
      private final Item input;
      private final int processingCost;

      public Result(Item input, int processingCost) {
          this.input = input;
          this.processingCost = processingCost;
      }

      @Override
      public void serializeRecipeData(@NotNull JsonObject jsonObject) {
          final var inputJson = new JsonObject();
          inputJson.addProperty("item", Registry.ITEM.getKey(this.input).toString());

          jsonObject.add("input", inputJson);
          jsonObject.add("processingCost", new JsonPrimitive(this.processingCost));
      }

      @Override
      public @NotNull ResourceLocation getId() {
          return new ResourceLocation(LushaTestMod.MOD_ID,
                  Registry.ITEM.getKey(this.input).getPath() + "_testtile");
      }

      @Override
      public @NotNull RecipeSerializer<?> getType() {
          return TestTileEntityRecipe.Serializer.INSTANCE;
      }

      @Nullable
      @Override
      public JsonObject serializeAdvancement() {
          return null;
      }

      @Nullable
      @Override
      public ResourceLocation getAdvancementId() {
          return null;
      }
  }
}
