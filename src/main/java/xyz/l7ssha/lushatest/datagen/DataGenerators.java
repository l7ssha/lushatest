package xyz.l7ssha.lushatest.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.l7ssha.lushatest.LushaTestMod;

@Mod.EventBusSubscriber(modid = LushaTestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final var generator = event.getGenerator();

        generator.addProvider(true, new DataGenRecipeProvider(generator));
    }
}
