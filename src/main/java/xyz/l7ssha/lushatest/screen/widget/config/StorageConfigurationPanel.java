package xyz.l7ssha.lushatest.screen.widget.config;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.network.LushaNetworkChannel;
import xyz.l7ssha.lushatest.network.packet.server.TileInventoryConfigurationServerSyncPacket;
import xyz.l7ssha.lushatest.screen.LushaContainerScreen;
import xyz.l7ssha.lushatest.screen.widget.ConfigPanelWidget;

public class StorageConfigurationPanel extends ConfigPanelWidget {

    private final Logger logger = LogUtils.getLogger();

    public StorageConfigurationPanel(int x, int y, LushaContainerScreen<?> screen, final TestBlockContainerMenu menu) {
        super(x, y, screen, Component.literal("IO Conf"), menu.getInventoryConfig());
    }

    @Override
    protected void onSidedButtonClick(ConfigPanelWidget.SidedConfigButton button, boolean rightClicked) {
        final var currentMode = this.config.getOrDefault(button.getDirection(), AccessModeConfig.NONE);
        final var nextMode = rightClicked ? currentMode.previous() : currentMode.next();

        this.config.put(button.getDirection(), nextMode);

        LushaNetworkChannel.sendToServer(new TileInventoryConfigurationServerSyncPacket(button.getDirection(), nextMode));

        this.logger.debug("Button clicked for side: %s".formatted(button.getDirection().getName()));
    }
}
