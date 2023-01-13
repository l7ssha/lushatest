package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.server.LushaTileEntityInvetorySideConfigServerSyncPacket;

import java.util.Map;

public class InventorySidedConfigWidget extends SidedConfigWidget {
    private final Map<Direction, AccessModeConfig> inventoryConfig;
    private final Logger logger = LogUtils.getLogger();

    public InventorySidedConfigWidget(int x, int y, Map<Direction, AccessModeConfig> inventoryConfig) {
        super(x, y);

        this.inventoryConfig = inventoryConfig;
    }

    @Override
    protected void onSidedButtonClick(SidedConfigWidget.SidedConfigButton button) {
        final var currentMode = this.inventoryConfig.getOrDefault(button.getDirection(), AccessModeConfig.NONE);
        final var nextMode = currentMode.next();

        this.inventoryConfig.put(button.getDirection(), nextMode);

        LushaNetworkChannel.sendToServer(new LushaTileEntityInvetorySideConfigServerSyncPacket(button.getDirection(), nextMode));

        this.logger.debug("Button clicked for side: %s".formatted(button.getDirection().getName()));
    }

    @Override
    protected Component getValueForDirection(Direction direction) {
        return Component.literal(InventorySidedConfigWidget.this.inventoryConfig.getOrDefault(direction, AccessModeConfig.NONE).getShortLabel());
    }
}
