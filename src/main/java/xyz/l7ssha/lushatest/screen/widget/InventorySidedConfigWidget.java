package xyz.l7ssha.lushatest.screen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class InventorySidedConfigWidget extends LushaGuiWidget {
    public InventorySidedConfigWidget(int x, int y) {
        this.children.add(
                new Button(x - 21, y, 10, 10, new TextComponent("T"), button -> System.out.println("Top button Clicked"))
        );
        this.children.add(
                new Button(x - 21, y + 11, 10, 10, new TextComponent("F"), button -> System.out.println("Front button clicked"))
        );
        this.children.add(
                new Button(x - 21, y + 21, 10, 10, new TextComponent("B"), button -> System.out.println("Bottom button clicked"))
        );
        this.children.add(
                new Button(x - 11, y + 11, 10, 10, new TextComponent("R"), button -> System.out.println("Right button clicked"))
        );
        this.children.add(
                new Button(x - 31, y + 11, 10, 10, new TextComponent("L"), button -> System.out.println("Left button clicked"))
        );
        this.children.add(
                new Button(x - 31, y + 21, 10, 10, new TextComponent("Ba"), button -> System.out.println("Back button clicked"))
        );
    }
}
