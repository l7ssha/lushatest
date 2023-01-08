package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

abstract public class SidedConfigWidget extends LushaGuiWidget {
    protected final int BUTTON_SIZE = 12;
    protected final int x;
    protected final int y;

    public SidedConfigWidget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected abstract void onSidedButtonClick(SidedConfigButton button);

    protected abstract Component getValueForDirection(Direction direction);

    public SidedConfigWidget init() {
        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y, "Up", Direction.UP));
        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y + BUTTON_SIZE, "South", Direction.SOUTH));
        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "Down", Direction.DOWN));
        this.children.add(new SidedConfigButton(x - BUTTON_SIZE, y + BUTTON_SIZE, "East", Direction.EAST));
        this.children.add(new SidedConfigButton(x - 3 * BUTTON_SIZE, y + BUTTON_SIZE, "West", Direction.WEST));
        this.children.add(new SidedConfigButton(x - 3 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "North", Direction.NORTH));

        return this;
    }

    protected void onButtonClick(Button button) {
        if (button instanceof SidedConfigButton sidedConfigButton) {
            onSidedButtonClick(sidedConfigButton);
        }
    }

    protected class SidedConfigButton extends Button {
        private final Direction direction;
        private final String tooltip;

        public SidedConfigButton(int posX, int posY, String tooltip, Direction direction) {
            super(posX, posY, BUTTON_SIZE, BUTTON_SIZE, SidedConfigWidget.this.getValueForDirection(direction), SidedConfigWidget.this::onButtonClick);

            this.tooltip = tooltip;
            this.direction = direction;
        }

        @Override
        public void renderToolTip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            Minecraft.getInstance().font.draw(poseStack, Component.literal(this.tooltip), (float) mouseX, (float) mouseY, 0);
        }

        public Direction getDirection() {
            return direction;
        }
    }
}
