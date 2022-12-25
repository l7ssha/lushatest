package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.blocks.TestBlock;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, LushaTestMod.MOD_ID);

    public static final RegistryObject<Block> TEST_BLOCK = registerBlock("test_block", TestBlock::new, LushaTestMod.ITEM_GROUP);

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS_REGISTRY.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        var registeredBlock = BLOCKS_REGISTRY.register(name, block);
        registerBlockItem(name, registeredBlock, tab);
        return registeredBlock;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ItemRegistry.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void setUp(IEventBus modEventBus) {
        BLOCKS_REGISTRY.register(modEventBus);
    }
}
