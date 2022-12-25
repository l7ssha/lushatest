package xyz.l7ssha.lushatest.container.slot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemRestrictedSlot extends SlotItemHandler {
    private final List<Item> allowedItems;

    public ItemRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, List<Item> allowedItems) {
        super(itemHandler, index, xPosition, yPosition);

        this.allowedItems = allowedItems;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.allowedItems.contains(stack.getItem());
    }
}
