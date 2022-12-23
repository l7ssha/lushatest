package xyz.l7ssha.lushatest.item;

import net.minecraft.world.item.Item;
import xyz.l7ssha.lushatest.LushaTestMod;

public class TestItem extends Item {
    public TestItem() {
        super(new Item.Properties().tab(LushaTestMod.ITEM_GROUP));
    }
}
