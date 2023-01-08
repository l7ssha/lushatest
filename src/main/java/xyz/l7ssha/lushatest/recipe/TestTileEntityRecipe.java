package xyz.l7ssha.lushatest.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TestTileEntityRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Ingredient inputItem;
    private final int recipeCost;

    public TestTileEntityRecipe(ResourceLocation id, Ingredient inputItem, int recipeCost) {
        this.id = id;
        this.inputItem = inputItem;
        this.recipeCost = recipeCost;
    }

    @Override
    public boolean matches(SimpleContainer container, @NotNull Level level) {
        return inputItem.test(container.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        final var resultItem = inputItem.getItems()[0].copy();

        resultItem.setCount(1);

        return resultItem;
    }

    public int getRecipeCost() {
        return recipeCost;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TestTileEntityRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "test_tile";
    }

    public static class Serializer implements RecipeSerializer<TestTileEntityRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull TestTileEntityRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            final var input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            final var recipeCost = GsonHelper.getAsInt(json, "processingCost");

            return new TestTileEntityRecipe(id, input, recipeCost);
        }

        @Override
        public TestTileEntityRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            final var input = Ingredient.fromNetwork(buf);
            final var recipeCost = buf.readInt();
            return new TestTileEntityRecipe(id, input, recipeCost);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, TestTileEntityRecipe recipe) {
            recipe.getInputItem().toNetwork(buf);
            buf.writeInt(recipe.getRecipeCost());
        }
    }
}
