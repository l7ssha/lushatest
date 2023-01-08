package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;

public class ContainerRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LushaTestMod.MOD_ID);

    public static final RegistryObject<MenuType<TestBlockContainerMenu>> TEST_BLOCK_CONTAINER = CONTAINER_REGISTRY.register("test_block_container", () -> IForgeMenuType.create(TestBlockContainerMenu::new));

    public static void setUp(IEventBus modEventBus) {
        CONTAINER_REGISTRY.register(modEventBus);
    }
}
