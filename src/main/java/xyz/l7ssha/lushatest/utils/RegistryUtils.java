package xyz.l7ssha.lushatest.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryUtils {
    public static Block getBlock(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
    }

    public static Item getItem(String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
    }
}
