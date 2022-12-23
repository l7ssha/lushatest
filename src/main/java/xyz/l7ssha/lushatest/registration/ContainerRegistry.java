package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.container.TestBlockContainer;

public class ContainerRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, LushaTestMod.MOD_ID);

    public static final RegistryObject<MenuType<TestBlockContainer>> TEST_BLOCK_CONTAINER = CONTAINER_REGISTRY.register("test_block_container", () -> new MenuType<>(TestBlockContainer::new));

    public static void setUp() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CONTAINER_REGISTRY.register(modEventBus);
    }
}
