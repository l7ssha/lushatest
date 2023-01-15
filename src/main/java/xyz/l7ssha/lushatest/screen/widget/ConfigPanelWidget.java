package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.screen.LushaContainerScreen;

import java.util.Map;

public abstract class ConfigPanelWidget extends LushaGuiWidget {
    protected final static ResourceLocation textureLocation = new ResourceLocation(LushaTestMod.MOD_ID, "textures/gui/container_panel.png");
    protected final int BUTTON_SIZE = 12;

    protected final Component labelComponent;
    protected final Map<Direction, AccessModeConfig> config;

    public ConfigPanelWidget(final int x, final int y, LushaContainerScreen<?> screen, Component component, Map<Direction, AccessModeConfig> config) {
        super(x, y, screen);

        this.labelComponent = component;
        this.config = config;

        init();
    }

    protected abstract void onSidedButtonClick(SidedConfigButton button, boolean rightClicked);

    protected Component getValueForDirection(Direction direction) {
        return Component.literal(this.config.getOrDefault(direction, AccessModeConfig.NONE).getShortLabel());
    }

    public void init() {
        final int x = this.x - 6;
        final int y = this.y + 16;

        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y, "Up", Direction.UP));
        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y + BUTTON_SIZE, "South", Direction.SOUTH));
        this.children.add(new SidedConfigButton(x - 2 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "Down", Direction.DOWN));
        this.children.add(new SidedConfigButton(x - BUTTON_SIZE, y + BUTTON_SIZE, "East", Direction.EAST));
        this.children.add(new SidedConfigButton(x - 3 * BUTTON_SIZE, y + BUTTON_SIZE, "West", Direction.WEST));
        this.children.add(new SidedConfigButton(x - 3 * BUTTON_SIZE, y + 2 * BUTTON_SIZE, "North", Direction.NORTH));
    }

    protected void onButtonClick(Button button, boolean rightClicked) {
        if (button instanceof SidedConfigButton sidedConfigButton) {
            onSidedButtonClick(sidedConfigButton, rightClicked);
        }
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float tick) {
        bindTexture();
        blit(stack, x - 48, y, 0, 0, 53, 76);

        Minecraft.getInstance().font.draw(stack, this.labelComponent, x - 43, y + 6, 0x0f0f0f);
        super.render(stack, mouseX, mouseY, tick);
    }

    protected void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, textureLocation);
    }

    protected class SidedConfigButton extends Button {
        private final Direction direction;
        private final String tooltip;

        public SidedConfigButton(int posX, int posY, String tooltip, Direction direction) {
            super(posX, posY, BUTTON_SIZE, BUTTON_SIZE, ConfigPanelWidget.this.getValueForDirection(direction), (b) -> {
            });

            this.tooltip = tooltip;
            this.direction = direction;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int rightClickModifier) {
            if (!this.clicked(mouseX, mouseY)) {
                return false;
            }

            this.playDownSound(Minecraft.getInstance().getSoundManager());
            ConfigPanelWidget.this.onButtonClick(this, rightClickModifier > 0);

            return true;
        }

        @Override
        public void renderToolTip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            if (!this.isMouseOver(mouseX, mouseY)) {
                return;
            }

            ConfigPanelWidget.this.screen.renderTooltip(poseStack, Component.literal(this.tooltip), mouseX, mouseY);
        }

        public Direction getDirection() {
            return direction;
        }
    }
}
