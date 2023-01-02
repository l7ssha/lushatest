package xyz.l7ssha.lushatest.screen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.LushaTileEntityInventorySideUpdateConfigPacket;

public class InventorySidedConfigWidget extends LushaGuiWidget {
    protected final int BUTTON_SIZE = 12;

    public InventorySidedConfigWidget(int x, int y) {
        this.children.add(
                new InventoryConfigButton(x - 2 * BUTTON_SIZE, y, "U", Direction.UP)
        );
        this.children.add(
                new InventoryConfigButton(x - 2 * BUTTON_SIZE, y + BUTTON_SIZE, "S", Direction.SOUTH)
        );
        this.children.add(
                new InventoryConfigButton(x - 2 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "D", Direction.DOWN)
        );
        this.children.add(
                new InventoryConfigButton(x - BUTTON_SIZE, y + BUTTON_SIZE, "E", Direction.EAST)
        );
        this.children.add(
                new InventoryConfigButton(x - 3 * BUTTON_SIZE, y + BUTTON_SIZE, "W", Direction.WEST)
        );
        this.children.add(
                new InventoryConfigButton(x - 3 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "N", Direction.NORTH)
        );
    }

    protected void onButtonClick(Button button) {
        final var inventoryButton = (InventoryConfigButton) button;

        System.out.printf("Button clicked for side: %s\n", inventoryButton.getDirection().getName());
        LushaNetworkChannel.sendToServer(
                new LushaTileEntityInventorySideUpdateConfigPacket(inventoryButton.getDirection(), InventoryConfigMode.INPUT)
        );
    }

    protected class InventoryConfigButton extends Button {
        private final Direction direction;

        public InventoryConfigButton(int posX, int posY, String label, Direction direction) {
            super(posX, posY, BUTTON_SIZE, BUTTON_SIZE, new TextComponent(label), InventorySidedConfigWidget.this::onButtonClick);

            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
    }
}
