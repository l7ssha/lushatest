package xyz.l7ssha.lushatest.screen.widget;

import net.minecraft.network.chat.Component;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;

public class ConfigurationWidget extends PanelWidget {
    public ConfigurationWidget(int x, int y, final TestBlockContainerMenu menu) {
        super(x, y, Component.literal("IO Conf"));

        this.children.add(new InventorySidedConfigWidget(this.x - 6, this.y + 18, menu.getInventoryConfig()).init());
    }
}
