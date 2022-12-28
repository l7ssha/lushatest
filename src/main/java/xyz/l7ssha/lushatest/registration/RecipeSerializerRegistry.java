package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LushaTestMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<TestTileEntityRecipe>> TEST_TILE_RECIPE_SERIALIZER =
            SERIALIZERS.register(TestTileEntityRecipe.Type.ID, () -> TestTileEntityRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
