package xyz.l7ssha.lushatest.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LushaTestMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<TestTileEntity>> TEST_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register("test_block",
            () -> BlockEntityType.Builder.of(TestTileEntity::new, BlockRegistry.TEST_BLOCK.get()).build(null));

    public static void setUp(IEventBus modEventBus) {
        BLOCK_ENTITY_REGISTRY.register(modEventBus);
    }
}
