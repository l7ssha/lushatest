package xyz.l7ssha.lushatest.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class LushaTestContainerMenu extends AbstractContainerMenu {
    protected final int slotSizePlus2 = 18, startX = 8, startY = 86, hotbarY = 144, inventoryY = 18;

    protected LushaTestContainerMenu(@Nullable MenuType<?> menuType, int p_38852_) {
        super(menuType, p_38852_);
    }

    protected void addPlayerInv(@NotNull Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + column, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, startX + column * slotSizePlus2, hotbarY));
        }
    }
}
