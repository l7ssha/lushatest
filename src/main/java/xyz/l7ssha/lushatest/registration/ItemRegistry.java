package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.item.TestItem;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, LushaTestMod.MOD_ID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS_REGISTRY.register("test_item", TestItem::new);

    public static void setUp(IEventBus modEventBus) {
        ITEMS_REGISTRY.register(modEventBus);
    }
}
