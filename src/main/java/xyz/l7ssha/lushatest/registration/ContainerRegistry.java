package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.container.TestBlockContainer;

public class ContainerRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, LushaTestMod.MOD_ID);

    public static final RegistryObject<MenuType<TestBlockContainer>> TEST_BLOCK_CONTAINER = CONTAINER_REGISTRY.register("test_block_container", () -> new MenuType<>(TestBlockContainer::new));

    public static void setUp(IEventBus modEventBus) {
        CONTAINER_REGISTRY.register(modEventBus);
    }
}
