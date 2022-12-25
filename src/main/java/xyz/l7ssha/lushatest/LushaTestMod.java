package xyz.l7ssha.lushatest;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.registration.ContainerRegistry;
import xyz.l7ssha.lushatest.registration.ItemRegistry;
import xyz.l7ssha.lushatest.screen.TestBlockContainerScreen;

@Mod(LushaTestMod.MOD_ID)
public class LushaTestMod
{
    public static final String MOD_ID = "lushatest";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MOD_ID + "_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.TEST_BLOCK.get());
        }
    };

    private static final Logger LOGGER = LogUtils.getLogger();

    public LushaTestMod()
    {
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::clientSetup);

        ItemRegistry.setUp(modEventBus);
        BlockRegistry.setUp(modEventBus);
        BlockEntityRegistry.setUp(modEventBus);
        ContainerRegistry.setUp(modEventBus);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ContainerRegistry.TEST_BLOCK_CONTAINER.get(), TestBlockContainerScreen::new);
    }
}
