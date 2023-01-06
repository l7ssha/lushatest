package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.storage.InventoryConfigMode;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.server.LushaTileEntityInventorySideUpdateConfigPacket;

import java.util.Map;

public class InventorySidedConfigWidget extends LushaGuiWidget {
    protected final int BUTTON_SIZE = 12;

    private final Map<Direction, InventoryConfigMode> inventoryConfig;

    private final Logger logger = LogUtils.getLogger();

    public InventorySidedConfigWidget(int x, int y, Map<Direction, InventoryConfigMode> inventoryConfig) {
        this.inventoryConfig = inventoryConfig;

        this.children.add(new InventoryConfigButton(x - 2 * BUTTON_SIZE, y, "U", Direction.UP));
        this.children.add(new InventoryConfigButton(x - 2 * BUTTON_SIZE, y + BUTTON_SIZE, "S", Direction.SOUTH));
        this.children.add(new InventoryConfigButton(x - 2 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "D", Direction.DOWN));
        this.children.add(new InventoryConfigButton(x - BUTTON_SIZE, y + BUTTON_SIZE, "E", Direction.EAST));
        this.children.add(new InventoryConfigButton(x - 3 * BUTTON_SIZE, y + BUTTON_SIZE, "W", Direction.WEST));
        this.children.add(new InventoryConfigButton(x - 3 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "N", Direction.NORTH));
    }

    protected void onButtonClick(Button button) {
        final var inventoryButton = (InventoryConfigButton) button;

        final var currentMode = this.inventoryConfig.getOrDefault(inventoryButton.direction, InventoryConfigMode.NONE);
        final var nextMode = currentMode.next();

        this.inventoryConfig.put(inventoryButton.direction, nextMode);

        LushaNetworkChannel.sendToServer(new LushaTileEntityInventorySideUpdateConfigPacket(inventoryButton.getDirection(), nextMode));

        this.logger.debug("Button clicked for side: %s".formatted(inventoryButton.getDirection().getName()));
    }

    protected class InventoryConfigButton extends Button {
        private final Direction direction;
        private final String tooltip;

        public InventoryConfigButton(int posX, int posY, String tooltip, Direction direction) {
            super(posX, posY, BUTTON_SIZE, BUTTON_SIZE, new TextComponent(InventorySidedConfigWidget.this.inventoryConfig.getOrDefault(direction, InventoryConfigMode.NONE).getShortLabel()), InventorySidedConfigWidget.this::onButtonClick);

            this.tooltip = tooltip;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
    }
}
